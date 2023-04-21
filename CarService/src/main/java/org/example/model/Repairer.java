package org.example.model;

public class Repairer {
    private Long id;
    private final String name;
    private boolean isAvailable;

    public Repairer() {
        this.name = "";
        isAvailable = true;
    }

    public Repairer(String name) {
        this.name = name;
        isAvailable = true;
    }

    public Repairer(Long id, String name, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public String getName() {
        return name;
    }

    public boolean checkIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return '\n' +
                "Repairer{" +
                "name='" + name + '\'' + "," +
                "isAvailable=" + isAvailable + "," +
                "id=" + id + "," +
                '}';
    }
}
