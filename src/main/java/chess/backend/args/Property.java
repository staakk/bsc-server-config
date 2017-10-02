package chess.backend.args;

public class Property {

    public final String key;
    public final String value;

    public Property(String property) {
        if (!property.startsWith("--") || property.indexOf('=') < 3 && property.length() >= 5) {
            throw new RuntimeException("Cannot parse property: " + property);
        }

        key = property.substring(2, property.indexOf('='));
        value = property.substring(property.indexOf('=') + 1);
    }
}
