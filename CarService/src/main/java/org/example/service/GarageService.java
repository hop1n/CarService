package org.example.service;

import org.example.exception.GarageNotFoundException;
import org.example.model.GarageSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GarageService implements Service<GarageSlot> {
    public static int garageCount;
    private List<GarageSlot> garageSlots = new ArrayList<>();

    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }

    public void setGarageSlots(List<GarageSlot> garageSlots) {
        this.garageSlots = garageSlots;
    }

    @Override
    public void add(GarageSlot garageSlot) {
        garageCount++;
        garageSlots.add(garageSlot);
        garageSlot.setId(garageCount);
    }

    @Override
    public void remove(int id) {
        boolean isRemoved;
        isRemoved = garageSlots.removeIf(slot -> slot.getId() == id);
        if (!isRemoved){
            throw new GarageNotFoundException("Garage with such id not found");
        }
    }

    @Override
    public GarageSlot getById(int id){
        GarageSlot garageToReturn;
        try {
            garageToReturn = garageSlots.stream().filter(slot -> slot.getId() == id).findFirst().get();
        } catch (NoSuchElementException e){
            throw new GarageNotFoundException("Garage with such id not found");
        }
        return garageToReturn;
    }

    public List<GarageSlot> showSorted() {
        List<GarageSlot> sortedGarageSlots = new ArrayList<>(this.garageSlots);
        sortedGarageSlots.sort((s1,s2) -> Boolean.compare(s2.isAvailable(), s1.isAvailable()));
        return sortedGarageSlots;
    }

}
