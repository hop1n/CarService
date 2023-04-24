package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.repository.GarageRepository;
import org.example.model.GarageSlot;
import org.example.settings.GarageSettings;

import java.util.List;

public class GarageService implements Service<GarageSlot> {
    private final GarageRepository garageRepo = new GarageRepository();

    public GarageService(GarageSettings garageSettings) {
        garageSettings.initializeProperty();
    }

    @Override
    public boolean remove(Long id) {
        garageRepo.deleteGarageSlotById(id);
        return true;
    }

    @Override
    public GarageSlot getById(Long id) {
        GarageSlot garageSlot = garageRepo.getGarageSlotById(id);
        if (garageSlot == null){
                throw new EntityNotFoundException("Garage with such id not found");
            }
        return garageSlot;
    }

    public List<GarageSlot> getGarageSlots() {
        return garageRepo.getGarageSlots();
    }

    public GarageSlot add(GarageSlot garageSlot) {
        return garageRepo.addGarageSlot();
    }
}
