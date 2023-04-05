package org.example.model;

public class GarageSlot {
    private static int garagesCount;
    private final int id;
    private boolean isAvailable;

    public GarageSlot() {
        isAvailable = true;
        garagesCount++;
        id = garagesCount;
    }
    public int getId() {
        return id;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    @Override
    public String toString() {
        return "GarageSlot{" +
                "id=" + id +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
