package chess.backend.command;

public interface Command {

    boolean checkArgs(String... args);

    String getDescription();

    void execute(String... args);

}
