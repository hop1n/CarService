package org.example.model;

public class Repairer {
    private static int repairersCount;
    private final int id;
    private final String name;
    private boolean isAvailable;

    public Repairer(String name) {
        this.name = name;
        id = repairersCount;
        repairersCount++;
        isAvailable = true;
    }

    public static int getRepairersCount() {
        return repairersCount;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
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
        return "Repairer{" +
                "name='" + name + '\'' +
                ", isAvailable=" + isAvailable +
                ", id=" + id +
                '}';
    }
}
