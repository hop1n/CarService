package org.example.Service;

import org.example.model.GarageSlot;

import java.util.ArrayList;
import java.util.List;

public class GarageService implements GarageDAO {
    private List<GarageSlot> garageSlots = new ArrayList<>();


    public List<GarageSlot> getGarageSlots() {
        return garageSlots;
    }
}
