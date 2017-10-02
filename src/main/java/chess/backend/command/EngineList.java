package chess.backend.command;

import chess.backend.Consts;
import chess.backend.Main;
import chess.backend.args.Arg;
import chess.backend.args.ArgsCount;
import chess.backend.model.Engine;

import java.io.IOException;
import java.util.List;

public class EngineList implements Command {
    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.equals(2, args)
                && Arg.is(args[0], "engine")
                && Arg.is(args[1], "list");
    }

    @Override
    public String getDescription() {
        return "\nengine list" +
                "\n\tlists available engines";
    }

    @Override
    public void execute(String... args) {
        List<Engine> engines;
        try {
            engines = Main.MAPPER.readValue(Consts.ENGINES_FILE, Main.MAPPER.getTypeFactory().constructCollectionType(List.class, Engine.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("Can't read " + Consts.ENGINES_CFG_PATH + " file.");
            return;
        }
        System.out.println("Available engines:");
        engines.stream()
                .forEach(System.out::println);
    }
}
