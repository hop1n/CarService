package org.example.Service;

import org.example.exceptions.GarageNotFoundException;
import org.example.model.GarageSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GarageService implements Service<GarageSlot> {
    public static int garageCount;
    private final List<GarageSlot> garageSlots = new ArrayList<>();

    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }

    @Override
    public void add(GarageSlot garageSlot) {
        garageCount++;
        garageSlots.add(garageSlot);
        garageSlot.setId(garageCount);
    }

    @Override
    public void remove(int id) {
        garageSlots.removeIf(slot -> slot.getId() == id);
    }

    @Override
    public GarageSlot getById(int id){
        GarageSlot garageToReturn;
        try {
            garageToReturn = garageSlots.stream().filter(slot -> slot.getId() == id).findFirst().get();
        } catch (IndexOutOfBoundsException e){
            throw new GarageNotFoundException("Garage with such id not found");
        }
        return garageToReturn;
    }

    public List<GarageSlot> showSorted() {
        List<GarageSlot> sortedGarageSlots = new ArrayList<>(this.garageSlots);
        sortedGarageSlots.sort((s1,s2) -> Boolean.compare(s2.isAvailable(), s1.isAvailable()));
        return sortedGarageSlots;
    }

    public void available(int id){
        garageSlots.stream().filter(slot -> slot.getId() == id).collect(Collectors.toList()).get(0).setAvailable(true);
    }

}
