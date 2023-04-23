package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.repository.GarageRepository;
import org.example.model.GarageSlot;
import org.example.settings.GarageSettings;

import java.util.List;

public class GarageService implements Service<GarageSlot> {
    //private final GarageRepository garageRepository = new GarageRepository();
    private final GarageRepository garageRepo = new GarageRepository();

    public GarageService(GarageSettings garageSettings) {
        garageSettings.initializeProperty();
    }

    @Override
    public boolean remove(Long id) {
//        garageRepository.removeGarageSlot(id);
//        if (garageSettings.isChangeable()){
//            garageRepo.deleteGarageSlotById(id);
//        } else {
//            throw new AssignDeprecatedMethod("You can't change the number of garages");
//        }
//        return true;
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
//        return garageRepository.getById(id);
//        return garageSlots.stream().filter(slot -> slot.getId().equals(id)).findFirst()
//                .orElseThrow(() -> new GarageNotFoundException("Garage with such id not found"));
    }

    public List<GarageSlot> getGarageSlots() {
        return garageRepo.getGarageSlots();
//        return garageRepository.getGarageSlots();
    }

    public GarageSlot add(GarageSlot garageSlot) {
//        return garageRepository.addGarageSlot();
//        if (garageSettings.isChangeable()) {
//            garageRepo.addGarageSlot();
//        } else {
//            throw new AssignDeprecatedMethod("You can't change the number of garages");
//        }
//        return garageSlot;
        return garageRepo.addGarageSlot();
    }
}
