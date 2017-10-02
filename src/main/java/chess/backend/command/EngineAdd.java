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
import java.util.Scanner;

public class EngineAdd implements Command {

    @Override
    public boolean checkArgs(String... args) {
        return ArgsCount.gte(4, args)
                && Arg.is(args[0], "engine")
                && Arg.is(args[1], "add");
    }

    @Override
    public String getDescription() {
        return "\nengine add --name=NAME --path=PATH [--description==DESCRIPTION]" +
                "\n\tadds engine with given NAME and PATH. If engine has the same NAME and different PATH" +
                "\n\tor same PATH and different NAME as one of the existing engines it will be added as new" +
                "\n\tengine. DESCRIPTION is optional";
    }

    @Override
    public void execute(String... args) {
        if (!Arg.isProperty(args[2], "name")) {
            throw new RuntimeException("Expected --name=NAME got " + args[2]);
        }
        if (!Arg.isProperty(args[3], "path")) {
            throw new RuntimeException("Expected --path=PATH got " + args[3]);
        }
        if (args.length >= 5 && !Arg.isProperty(args[4], "description")) {
            System.err.print("Expected --description=DESCRIPTION got " + args[4]);
            return;
        }

        String name = new Property(args[2]).value;
        String path = new Property(args[3]).value;
        String description = "";
        if (args.length >= 5) {
            description = new Property(args[4]).value;
        }

        Engine engine = new Engine(name, path, description);

        List<Engine> engines;
        try {
            engines = Main.MAPPER.readValue(Consts.ENGINES_FILE, Main.MAPPER.getTypeFactory().constructCollectionType(List.class, Engine.class));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("Can't read " + Consts.ENGINES_CFG_PATH + " file.");
            return;
        }

        Optional<Engine> existingEngine = engines.stream()
                .filter(e -> e.getName().equals(engine.getName()) && e.getPath().equals(engine.getPath()))
                .findFirst();

        if (existingEngine.isPresent()) {
            System.out.println("Press Y to overwrite engine with name " + engine.getName() + " and path " + engine.getPath());
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if (!response.equals("Y")) {
                return;
            }
            engines.remove(existingEngine.get());
        }

        engines.add(engine);

        ObjectWriter writer = Main.MAPPER.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(Consts.ENGINES_FILE, engines);
        } catch (IOException e) {
            System.err.print("Can't write to file " + Consts.ENGINES_CFG_PATH);
        }
        System.out.println("Engine " + engine.getName() + " added.");

    }

}
