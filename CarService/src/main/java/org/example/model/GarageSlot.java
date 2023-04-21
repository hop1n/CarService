package org.example.model;

public class GarageSlot {
    private Long id;
    private boolean isAvailable;

    public GarageSlot() {
        isAvailable = true;
    }

    public GarageSlot(Long id, boolean isAvailable) {
        this.id = id;
        this.isAvailable = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return '\n' +
                "GarageSlot{" +
                "id=" + id + "," +
                "isAvailable=" + isAvailable +
                '}';
    }
}
