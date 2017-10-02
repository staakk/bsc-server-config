package chess.backend.command;

import chess.backend.Main;
import chess.backend.args.Arg;
import chess.backend.args.ArgsCount;

import java.util.Arrays;

public class Help implements Command {
    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.equals(1, args)
                && Arg.is(args[0], "help");
    }

    @Override
    public String getDescription() {
        return "\nhelp" +
                "\n\treturns list of available commands";
    }

    @Override
    public void execute(String... args) {
        Arrays.stream(Main.COMMANDS)
                .sorted((o1, o2) -> o1.getDescription().compareTo(o2.getDescription()))
                .map(Command::getDescription)
                .forEach(System.out::println);
    }
}
