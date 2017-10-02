package chess.backend.command;

import chess.backend.Consts;
import chess.backend.Main;
import chess.backend.args.Arg;
import chess.backend.args.ArgsCount;
import chess.backend.model.User;

import java.io.IOException;
import java.util.List;

public class UserList implements Command {
    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.equals(2, args)
                && Arg.is(args[0], "user")
                && Arg.is(args[1], "list");
    }

    @Override
    public String getDescription() {
        return "\nuser list" +
                "\n\tlists available users";
    }

    @Override
    public void execute(String... args) {
        List<User> users;
        try {
            users = Main.MAPPER.readValue(Consts.USERS_FILE, Main.MAPPER.getTypeFactory().constructCollectionType(List.class, User.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("Can't read " + Consts.USERS_CFG_PATH + " file.");
            return;
        }
        System.out.println("Available users:");
        users.stream()
                .forEach(System.out::println);
    }
}
