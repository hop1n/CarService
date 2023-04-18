package org.example.service;

import org.example.exception.AssignDeprecatedMethod;
import org.example.exception.GarageNotFoundException;
import org.example.model.GarageSlot;
import org.example.settings.GarageSettings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GarageService implements Service<GarageSlot> {
    private List<GarageSlot> garageSlots = new ArrayList<>();
    private final GarageSettings garageSettings;
    private Long garageCount;

    public GarageService(GarageSettings garageSettings) {
        this.garageSettings = garageSettings;
        this.garageSettings.initializeProperty();
        garageCount = 0L;
    }

    @Override
    public boolean remove(Long id) {
        if (garageSettings.isChangeable()){
            boolean isRemoved = garageSlots.removeIf(slot -> slot.getId().equals(id));
            if (!isRemoved) {
                throw new GarageNotFoundException("Garage with such id not found");
            }
        } else {
            throw new AssignDeprecatedMethod("You can't change the number of garages");
        }
        return true;
    }

    @Override
    public GarageSlot getById(Long id) {
        return garageSlots.stream().filter(slot -> slot.getId().equals(id)).findFirst()
                .orElseThrow(() -> new GarageNotFoundException("Garage with such id not found"));
    }

    @Override
    public String toString() {
        return "GarageService{" +
                "garageSlots=" + garageSlots +
                '}';
    }

    public Long getGarageCount() {
        return garageCount;
    }

    public void setGarageCount(Long garageCount) {
        this.garageCount = garageCount;
    }

    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }

    public void setGarageSlots(List<GarageSlot> garageSlots) {
        this.garageSlots = garageSlots;
        this.garageCount = garageSlots
                .stream()
                .max(Comparator.comparing(GarageSlot::getId))
                .map(GarageSlot::getId)
                .orElse(0L);
    }

    public GarageSlot add(GarageSlot garageSlot){
        if (garageSettings.isChangeable()) {
            garageSlots.add(garageSlot);
            garageCount++;
            garageSlot.setId(garageCount);
        } else {
            throw new AssignDeprecatedMethod("You can't change the number of garages");
        }
        return garageSlot;
    }
}
