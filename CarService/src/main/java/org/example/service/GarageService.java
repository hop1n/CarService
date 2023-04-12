package org.example.service;

import org.example.exception.AssignDeprecatedMethod;
import org.example.exception.GarageNotFoundException;
import org.example.exception.PropertyNotFound;
import org.example.model.GarageSlot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

public class GarageService implements Service<GarageSlot> {
    public static int garageCount;
    private List<GarageSlot> garageSlots = new ArrayList<>();
    private final String PATH;
    private boolean changeable;
    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }

    public void setGarageSlots(List<GarageSlot> garageSlots) {
        this.garageSlots = garageSlots;
        garageCount = garageSlots.size();
    }

    public GarageService(String path) {
        this.PATH = path;
        initializePropertyFromFile();
        garageCount = 0;
    }

    public GarageService() {
        this.PATH = "";
        this.changeable = true;
        garageCount = 0;
    }

    public void initializePropertyFromFile(){
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(PATH);
            properties.load(fileReader);
        } catch (IOException e) {
            throw new PropertyNotFound("Property file does not exist");
        }
        if (properties.getProperty("changeable_number_of_garages").equals("true")){
            this.changeable = true;
        } else {
            this.changeable = false;
        }
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public boolean getChangeable() {
        return changeable;
    }

    @Override
    public void add(GarageSlot garageSlot){
        if (changeable) {
            garageCount++;
            garageSlots.add(garageSlot);
            garageSlot.setId(garageCount);
        } else {
            throw new AssignDeprecatedMethod("You can't change the number of garages");
        }
    }

    @Override
    public void remove(int id){
        if (changeable){
            boolean isRemoved;
            isRemoved = garageSlots.removeIf(slot -> slot.getId() == id);
            if (!isRemoved) {
                throw new GarageNotFoundException("Garage with such id not found");
            }
        } else {
            throw new AssignDeprecatedMethod("You can't change the number of garages");
        }
    }

    @Override
    public GarageSlot getById(int id) {
        GarageSlot garageToReturn;
        try {
            garageToReturn = garageSlots.stream().filter(slot -> slot.getId() == id).findFirst().get();
        } catch (NoSuchElementException e) {
            throw new GarageNotFoundException("Garage with such id not found");
        }
        return garageToReturn;
    }

    @Override
    public String toString() {
        return "GarageService{" +
                "garageSlots=" + garageSlots +
                '}';
    }
}
