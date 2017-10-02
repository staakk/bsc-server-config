package chess.backend.model;

public class Engine {

    private String name;
    private String path;
    private String description;

    public Engine() {
    }

    public Engine(String name, String path, String description) {
        this.name = name;
        this.path = path;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "\nname:\t\t" + name +
                "\npath:\t\t" + path +
                "\ndescription:\t\t" + description;
    }
}
