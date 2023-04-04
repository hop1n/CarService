package org.example.model;

public class Repairer {
    private final String name;
    private boolean isAvailable;

    public Repairer(String name) {
        this.name = name;
        isAvailable = false;
    }

    public String getName() {
        return name;
    }

    public boolean checkStatus() {
        return isAvailable;
    }

    public void setStatus(boolean status) {
        isAvailable = status;
    }

    @Override
    public String toString() {
        return "Repairer{" +
                "name='" + name + '\'' +
                ", status=" + isAvailable +
                '}';
    }
}
