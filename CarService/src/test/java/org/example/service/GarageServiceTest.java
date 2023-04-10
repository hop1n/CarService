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
    GarageSlot garageSlot1 = new GarageSlot();
    GarageSlot garageSlot2 = new GarageSlot();
    GarageSlot garageSlot3 = new GarageSlot();
    @BeforeEach
    public void setUp(){
        garageSlot1.setId(1);
        garageSlots.add(garageSlot1);
        garageSlot2.setId(2);
        garageSlot2.setAvailable(false);
        garageSlots.add(garageSlot2);
        garageSlot3.setId(3);
        garageSlots.add(garageSlot3);
        garageService.setGarageSlots(garageSlots);
    }

    @Test
    public void addGarageSlot(){
        GarageSlot garageSlot4 = new GarageSlot();
        garageService.add(garageSlot4);
        Assertions.assertEquals(4, garageService.getGarageSlots().size());
        Assertions.assertTrue(garageService.getGarageSlots().contains(garageSlot4));
        Assertions.assertEquals(garageService.toString(), "GarageService{garageSlots=[\n" +
                "GarageSlot{id=1,isAvailable=true}, \n" +
                "GarageSlot{id=2,isAvailable=false}, \n" +
                "GarageSlot{id=3,isAvailable=true}, \n" +
                "GarageSlot{id=4,isAvailable=true}]}");
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
    void remove() {
        int oldSize = garageService.getGarageSlots().size();
        garageService.remove(1);
        Assertions.assertEquals(oldSize - 1, garageService.getGarageSlots().size());
        Assertions.assertFalse(garageService.getGarageSlots().contains(garageSlot1));
    }

    @Test
    void removeException(){
        Assertions.assertThrows(GarageNotFoundException.class, () -> garageService.remove(10));
    }
}
