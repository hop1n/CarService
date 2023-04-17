package org.example.service;

import org.example.exception.AssignDeprecatedMethod;
import org.example.exception.GarageNotFoundException;
import org.example.exception.PropertyNotFound;
import org.example.model.GarageSlot;

import java.io.*;
import java.util.*;

public class GarageService implements Service<GarageSlot> {
    private List<GarageSlot> garageSlots = new ArrayList<>();
    private final String path;
    private boolean changeable;
    private int garageCount;

    public GarageService(String path) {
        this.path = path;
        garageCount = 0;
    }

    public GarageService() {
        this.path = "";
        this.changeable = true;
        garageCount = 0;
    }

    public void initializePropertyFromFile(){
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(path);
            properties.load(fileReader);
        } catch (IOException e) {
            throw new PropertyNotFound("Property file does not exist");
        }
        this.changeable = properties.getProperty("changeable_number_of_garages").equals("true");
    }

    @Override
    public void remove(int id) {
        if (changeable){
            boolean isRemoved = garageSlots.removeIf(slot -> slot.getId() == id);
            if (!isRemoved) {
                throw new GarageNotFoundException("Garage with such id not found");
            }
        } else {
            throw new AssignDeprecatedMethod("You can't change the number of garages");
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
        this.garageCount = garageSlots
                .stream()
                .max(Comparator.comparing(GarageSlot::getId))
                .map(GarageSlot::getId)
                .orElse(0);
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public boolean getChangeable() {
        return changeable;
    }

    public void add(GarageSlot garageSlot){
        if (changeable) {
            garageSlots.add(garageSlot);
            garageCount++;
            garageSlot.setId(garageCount);
        } else {
            throw new AssignDeprecatedMethod("You can't change the number of garages");
        }
    }
}
