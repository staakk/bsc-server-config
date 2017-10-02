package chess.backend.args;

public class Arg {

    public static boolean is(String arg, String expected) {
        return arg.equals(expected);
    }

    public static boolean isProperty(String arg, String expected) {
        return arg.startsWith("--" + expected + "=") && arg.length() >= 5;
    }
}
