package org.example.service;

import org.example.exception.GarageNotFoundException;
import org.example.model.GarageSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class GarageServiceTest {
    GarageService garageService = new GarageService();
    List<GarageSlot> garageSlots = new ArrayList<>();
    @BeforeEach
    public void setUp(){
        GarageSlot garageSlot1 = new GarageSlot();
        garageSlot1.setId(1);
        garageSlots.add(garageSlot1);
        GarageSlot garageSlot2 = new GarageSlot();
        garageSlot2.setId(2);
        garageSlot2.setAvailable(false);
        garageSlots.add(garageSlot2);
        GarageSlot garageSlot3 = new GarageSlot();
        garageSlot3.setId(3);
        garageSlots.add(garageSlot3);
        garageService.setGarageSlots(garageSlots);
    }

    @Test
    public void addGarageSlot(){
        GarageSlot garageSlot4 = new GarageSlot();
        garageSlot4.setId(4);
        garageService.add(garageSlot4);
        Assertions.assertEquals(4, garageService.getGarageSlots().size());
    }

    @Test
    public void getById() {
        GarageSlot garageToReturn = garageService.getById(2);
        Assertions.assertEquals(garageService.getGarageSlots().get(1), garageToReturn);
    }

    @Test
    public void getByIdException() throws GarageNotFoundException{
        Assertions.assertThrows(GarageNotFoundException.class, () -> garageService.getById(10));
    }

    @Test
    public void showSorted(){
        garageService.getById(2).setAvailable(false);
        List<GarageSlot> sortedGarageSlots = garageService.showSorted();
        Assertions.assertEquals(sortedGarageSlots.indexOf(garageService.getById(1)), garageService.getGarageSlots().indexOf(garageService.getById(1)));
        Assertions.assertEquals(sortedGarageSlots.indexOf(garageService.getById(2)), garageService.getGarageSlots().indexOf(garageService.getById(3)));
        Assertions.assertEquals(sortedGarageSlots.indexOf(garageService.getById(3)), garageService.getGarageSlots().indexOf(garageService.getById(2)));
    }

    @Test
    void remove() {
        int oldSize = garageService.getGarageSlots().size();
        garageService.remove(1);
        Assertions.assertEquals(oldSize - 1, garageService.getGarageSlots().size());
    }

    @Test
    void removeException(){
        Assertions.assertThrows(GarageNotFoundException.class, () -> garageService.remove(10));
    }
}
