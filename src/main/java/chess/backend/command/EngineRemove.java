package chess.backend.command;

import chess.backend.Consts;
import chess.backend.Main;
import chess.backend.args.Arg;
import chess.backend.args.ArgsCount;
import chess.backend.args.Property;
import chess.backend.model.Engine;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EngineRemove implements Command {

    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.equals(4, args)
                && Arg.is(args[0], "engine")
                && Arg.is(args[1], "rm");
    }

    @Override
    public String getDescription() {
        return "\nengine rm --name=NAME --path=PATH" +
                "\n\tremoves engine with given NAME and PATH";
    }

    @Override
    public void execute(String... args) {
        if (!Arg.isProperty(args[2], "name")) {
            throw new RuntimeException("Expected --name=NAME got " + args[2]);
        }
        if (!Arg.isProperty(args[3], "path")) {
            throw new RuntimeException("Expected --path=PATH got " + args[3]);
        }

        String name = new Property(args[2]).value;
        String path = new Property(args[3]).value;

        List<Engine> engines;
        try {
            engines = Main.MAPPER.readValue(Consts.ENGINES_FILE, Main.MAPPER.getTypeFactory().constructCollectionType(List.class, Engine.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("Can't read " + Consts.ENGINES_CFG_PATH + " file.");
            return;
        }

        Optional<Engine> existingEngine = engines.stream()
                .filter(e -> e.getName().equals(name) || e.getPath().equals(path))
                .findFirst();

        if (!existingEngine.isPresent()) {
            System.err.println("Engine with name " + name + " and path " + path + " doesn't exists.");
            return;
        }

        engines.remove(existingEngine.get());

        ObjectWriter writer = Main.MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(Consts.ENGINES_FILE, engines);
        } catch (IOException e) {
            System.err.print("Can't write to file " + Consts.ENGINES_CFG_PATH);
        }
        System.out.println("Engine " + name + " removed.");

    }

}
