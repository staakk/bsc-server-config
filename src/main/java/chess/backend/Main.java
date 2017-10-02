package chess.backend;

import chess.backend.command.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class Main {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final Command[] COMMANDS = {
            new UserList(),
            new UserAdd(),
            new UserRemove(),
            new EngineList(),
            new EngineAdd(),
            new EngineRemove(),
            new Help()
    };

    public static void main(String[] args) {
        long count = Arrays.stream(COMMANDS)
                .filter(command -> command.checkArgs(args))
                .peek(command -> command.execute(args))
                .count();
        if (count == 0) {
            System.out.print("Unknown command: ");
            Arrays.stream(args)
                    .forEach(System.out::print);
            System.out.println();
            System.out.println("Here is the list of available commands: ");
            new Help().execute();
        }
    }


}
