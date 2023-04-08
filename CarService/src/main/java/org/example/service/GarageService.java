package org.example.service;

import org.example.model.GarageSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GarageService implements Service<GarageSlot> {
    private final List<GarageSlot> garageSlots = new ArrayList<>();
    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }
    @Override
    public void add(GarageSlot garageSlot) {
        this.garageSlots.add(garageSlot);
    }
    @Override
    public void remove(int id) {
        garageSlots.removeIf(slot -> slot.getId() == id);
    }
    @Override
    public GarageSlot getById(int id){
        return garageSlots.stream().filter(slot -> slot.getId() == id).collect(Collectors.toList()).get(0);
    }
    @Override
    public void sort() {
        garageSlots.sort((s1,s2) -> Boolean.compare(s2.isAvailable(), s1.isAvailable()));
    }
    public void showAvailableSlots() {
        List<GarageSlot> availableGarageSlots = garageSlots.stream().filter(GarageSlot::isAvailable).collect(Collectors.toList());
        System.out.println(availableGarageSlots);
    }
    public void showSorted() {
        List<GarageSlot> sortedGarageSlots = new ArrayList<>(this.garageSlots);
        sortedGarageSlots.sort((s1,s2) -> Boolean.compare(s2.isAvailable(), s1.isAvailable()));
        System.out.println(sortedGarageSlots);
    }
    public void unAvailable(int id){
        garageSlots.stream().filter(slot -> slot.getId() == id).collect(Collectors.toList()).get(0).setAvailable(false);
    }
    public void available(int id){
        garageSlots.stream().filter(slot -> slot.getId() == id).collect(Collectors.toList()).get(0).setAvailable(true);
    }
    public boolean isAvailable(int id){
        return garageSlots.stream().filter(slot -> slot.getId() == id).collect(Collectors.toList()).get(0).isAvailable();
    }
}
