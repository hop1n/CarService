package org.example.model;

public class GarageSlot {
    private int id;
    private boolean isAvailable;

    public GarageSlot() {
        isAvailable = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return '\n'+
                "GarageSlot{" +
                "id=" + id  + ","+
                "isAvailable=" + isAvailable  +
                '}';
    }
}
