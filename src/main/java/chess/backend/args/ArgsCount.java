package chess.backend.args;

public class ArgsCount {

    public static boolean equals(int count, String... args) {
        return args.length == count;
    }

    public static boolean gte(int count, String... args) {
        return args.length >= count;
    }

}
