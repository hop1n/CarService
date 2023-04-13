package org.example.dto;

import org.example.model.GarageSlot;
import org.example.service.GarageService;

import java.util.List;

public class GarageServiceDto {

    public  int garageCount;
    private List<GarageSlot> garageSlots;

    public GarageServiceDto(int garageCount, List<GarageSlot> garageSlots) {
        this.garageCount = garageCount;
        this.garageSlots = garageSlots;
    }

    public GarageServiceDto() {
    }

    public static GarageServiceDto fromService(GarageService garageService){
        return new GarageServiceDto(garageService.getGarageCount(), garageService.getGarageSlots());
    }

    public void toService(GarageService garageService){
        garageService.setGarageSlots(this.garageSlots);
        garageService.setGarageCount(this.garageCount);
    }

    public int getGarageCount() {
        return garageCount;
    }

    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }

    public void setGarageCount(int garageCount) {
        this.garageCount = garageCount;
    }

    public void setGarageSlots(List<GarageSlot> garageSlots) {
        this.garageSlots = garageSlots;
    }
}
