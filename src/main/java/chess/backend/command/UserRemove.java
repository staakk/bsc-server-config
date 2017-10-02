package chess.backend.command;

import chess.backend.Consts;
import chess.backend.Main;
import chess.backend.args.Arg;
import chess.backend.args.ArgsCount;
import chess.backend.args.Property;
import chess.backend.model.User;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserRemove implements Command {

    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.equals(4, args)
                && Arg.is(args[0], "user")
                && Arg.is(args[1], "rm");
    }

    @Override
    public String getDescription() {
        return "\nuser rm --username=USERNAME" +
                "\n\tremoves user with given USERNAME";
    }

    @Override
    public void execute(String... args) {
        if (!Arg.isProperty(args[2], "username")) {
            throw new RuntimeException("Expected --username=USERNAME got " + args[2]);
        }

        Property username = new Property(args[2]);

        List<User> users;
        try {
            users = Main.MAPPER.readValue(Consts.USERS_FILE, Main.MAPPER.getTypeFactory().constructCollectionType(List.class, User.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("Can't read users.json file.");
            return;
        }

        Optional<User> exitingUser = users.stream()
                .filter(u -> u.getLogin().equals(username.value))
                .findFirst();

        if (!exitingUser.isPresent()) {
            System.out.println("User " + username.value + " doesn't exists.");
            return;
        }

        users.remove(exitingUser.get());

        ObjectWriter writer = Main.MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(Consts.USERS_FILE, users);
        } catch (IOException e) {
            System.err.print("Can't write to file users.json");
        }

        System.out.println("User " + username.value + " removed.");
    }
}
