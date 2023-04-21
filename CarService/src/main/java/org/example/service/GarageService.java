package org.example.service;

import org.example.model.GarageSlot;
import org.example.repository.GarageRepository;
import org.example.settings.GarageSettings;

import java.util.List;

public class GarageService implements Service<GarageSlot> {
    private final GarageRepository garageRepository = new GarageRepository();

    public GarageService(GarageSettings garageSettings) {
        garageSettings.initializeProperty();
    }

    @Override
    public boolean remove(Long id) {
        garageRepository.removeGarageSlot(id);
//        if (garageSettings.isChangeable()){
//            boolean isRemoved = garageSlots.removeIf(slot -> slot.getId().equals(id));
//            if (!isRemoved) {
//                throw new GarageNotFoundException("Garage with such id not found");
//            }
//        } else {
//            throw new AssignDeprecatedMethod("You can't change the number of garages");
//        }
        return true;
    }

    @Override
    public GarageSlot getById(Long id) {
        return garageRepository.getById(id);
//        return garageSlots.stream().filter(slot -> slot.getId().equals(id)).findFirst()
//                .orElseThrow(() -> new GarageNotFoundException("Garage with such id not found"));
    }

    public List<GarageSlot> getGarageSlots() {
        return garageRepository.getGarageSlots();
    }

    public GarageSlot add(GarageSlot garageSlot) {
        return garageRepository.addGarageSlot();
//        if (garageSettings.isChangeable()) {
//            garageSlots.add(garageSlot);
//            garageCount++;
//            garageSlot.setId(garageCount);
//        } else {
//            throw new AssignDeprecatedMethod("You can't change the number of garages");
//        }
//        return garageSlot;
//    }
    }
}
