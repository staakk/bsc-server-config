package chess.backend.command;

import chess.backend.Consts;
import chess.backend.Main;
import chess.backend.args.Arg;
import chess.backend.args.ArgsCount;
import chess.backend.args.Property;
import chess.backend.model.User;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserAdd implements Command {

    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.equals(4, args)
                && Arg.is(args[0], "user")
                && Arg.is(args[1], "add");
    }

    @Override
    public String getDescription() {
        return "\nuser add --username=USERNAME --password=PASSWORD" +
                "\n\tadds user with given USERNAME and PASSWORD";
    }

    @Override
    public void execute(String... args) {
        if (!Arg.isProperty(args[2], "username")) {
            throw new RuntimeException("Expected --username=USERNAME got " + args[2]);
        }
        if (!Arg.isProperty(args[3], "password")) {
            throw new RuntimeException("Expected --password=PASSWORD got " + args[3]);
        }

        Property username = new Property(args[2]);
        Property password = new Property(args[3]);

        User user = new User(username.value, DigestUtils.sha256Hex(password.value));

        List<User> users;
        try {
            users = Main.MAPPER.readValue(Consts.USERS_FILE, Main.MAPPER.getTypeFactory().constructCollectionType(List.class, User.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("Can't read " + Consts.USERS_CFG_PATH + " file.");
            return;
        }

        Optional<User> exitingUser = users.stream()
                .filter(u -> u.getLogin().equals(user.getLogin()))
                .findFirst();

        if (exitingUser.isPresent()) {
            System.out.println("Press Y to overwrite user with login " + user.getLogin());
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if (!response.equals("Y")) {
                return;
            }
            users.remove(exitingUser.get());
        }

        users.add(user);

        ObjectWriter writer = Main.MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(Consts.USERS_FILE, users);
        } catch (IOException e) {
            System.err.print("Can't write to " + Consts.USERS_CFG_PATH + "file.");
        }
        System.out.println("User " + user.getLogin() + " added.");
    }

}
