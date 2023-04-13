package org.example.service;

import org.example.exception.GarageNotFoundException;
import org.example.model.GarageSlot;

import java.util.ArrayList;
import java.util.List;

public class GarageService implements Service<GarageSlot> {
    public int garageCount;
    private List<GarageSlot> garageSlots = new ArrayList<>();

    public GarageService() {
        garageCount = 0;
    }

    @Override
    public void add(GarageSlot garageSlot) {
        garageSlots.add(garageSlot);
        garageCount++;
        garageSlot.setId(garageCount);
    }

    @Override
    public void remove(int id) {
        boolean isRemoved = garageSlots.removeIf(slot -> slot.getId() == id);
        if (!isRemoved) {
            throw new GarageNotFoundException("Garage with such id not found");
        }
    }

    @Override
    public GarageSlot getById(int id) {
        return garageSlots.stream().filter(slot -> slot.getId() == id).findFirst()
                .orElseThrow(() -> new GarageNotFoundException("Garage with such id not found"));
    }

    @Override
    public String toString() {
        return "GarageService{" +
                "garageSlots=" + garageSlots +
                '}';
    }

    public int getGarageCount() {
        return garageCount;
    }

    public void setGarageCount(int garageCount) {
        this.garageCount = garageCount;
    }

    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }

    public void setGarageSlots(List<GarageSlot> garageSlots) {
        this.garageSlots = garageSlots;
    }
}
