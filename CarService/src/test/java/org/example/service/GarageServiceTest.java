package org.example.service;

import org.example.exception.AssignDeprecatedMethod;
import org.example.exception.GarageNotFoundException;
import org.example.exception.PropertyNotFound;
import org.example.model.GarageSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class GarageServiceTest {
    private GarageService garageService;
    List<GarageSlot> garageSlots = new ArrayList<>();
    GarageSlot garageSlot1 = new GarageSlot();
    GarageSlot garageSlot2 = new GarageSlot();
    GarageSlot garageSlot3 = new GarageSlot();

    @BeforeEach
    void setUp() {
        garageService = new GarageService();
        garageService.setChangeable(true);
        garageSlot2.setAvailable(false);
        garageService.add(garageSlot1);
        garageService.add(garageSlot2);
        garageService.add(garageSlot3);
    }

    @Test
    void getGarageCount(){
        Assertions.assertEquals(3, garageService.getGarageCount());
    }

    @Test
    void getGarageSlots(){
        Assertions.assertEquals("[\n" +
                "GarageSlot{id=1,isAvailable=true}, \n" +
                "GarageSlot{id=2,isAvailable=false}, \n" +
                "GarageSlot{id=3,isAvailable=true}]", garageService.getGarageSlots().toString());
    }
    @Test
    void addGarageSlot() {
        GarageSlot garageSlot4 = new GarageSlot();
        garageService.add(garageSlot4);
        Assertions.assertEquals(4, garageService.getGarageSlots().size());
        Assertions.assertTrue(garageService.getGarageSlots().contains(garageSlot4));
        Assertions.assertEquals("GarageService{garageSlots=[\n" +
                "GarageSlot{id=1,isAvailable=true}, \n" +
                "GarageSlot{id=2,isAvailable=false}, \n" +
                "GarageSlot{id=3,isAvailable=true}, \n" +
                "GarageSlot{id=4,isAvailable=true}]}", garageService.toString());
    }

    @Test
    void initializePropertyFromFileException(){
        GarageService garageService2 = new GarageService("non-existent_file_path");
        Assertions.assertThrows(PropertyNotFound.class, () -> garageService2.initializePropertyFromFile());
    }

    @Test
    void initializePropertyFromFile() throws IOException {
        Properties properties = new Properties();
        String path = "../CarService/src/main/resources/application.properties";
        FileReader fileReader = new FileReader(path);
        properties.load(fileReader);
        boolean changeable = properties.getProperty("changeable_number_of_garages").equals("true");
        GarageService garageService2 = new GarageService("../CarService/src/main/resources/application.properties");
        garageService2.initializePropertyFromFile();
        Assertions.assertEquals(garageService2.getChangeable(), changeable);
    }

    @Test
    void setGarageSlots(){
        garageSlots.add(garageSlot1);
        garageSlots.add(garageSlot2);
        garageSlots.add(garageSlot3);
        garageService.setGarageSlots(garageSlots);
        Assertions.assertEquals(garageService.getGarageSlots(), garageSlots);
    }

    @Test
    void setGarageCount(){
        garageService.setGarageCount(5L);
        Assertions.assertEquals(garageService.getGarageCount(), 5);
    }
    @Test
    void addGarageSlotException(){
        GarageSlot garageSlot4 = new GarageSlot();
        garageService.setChangeable(false);
        Assertions.assertThrows(AssignDeprecatedMethod.class, () -> garageService.add(garageSlot4));
    }

    @Test
    void getById() {
        GarageSlot garageToReturn = garageService.getById(2L);
        Assertions.assertEquals(garageService.getGarageSlots().get(1), garageToReturn);
    }

    @Test
    void getByIdGarageNotFoundException() throws GarageNotFoundException {
        Assertions.assertThrows(GarageNotFoundException.class, () -> garageService.getById(10L));
    }

    @Test
    void remove() {
        int oldSize = garageService.getGarageSlots().size();
        if (garageService.getChangeable()) {
            garageService.remove(1L);
            Assertions.assertEquals(oldSize - 1, garageService.getGarageSlots().size());
            Assertions.assertFalse(garageService.getGarageSlots().contains(garageSlot1));
        } else {
            Assertions.assertThrows(AssignDeprecatedMethod.class, () -> garageService.remove(1L));
        }
    }

    @Test
    void removeAssignDeprecatedMethodException(){
        garageService.setChangeable(false);
        Assertions.assertThrows(AssignDeprecatedMethod.class, () -> garageService.remove(1L));
    }

    @Test
    void removeGarageNotFoundException() {
        Assertions.assertThrows(GarageNotFoundException.class, () -> garageService.remove(10L));
    }
}
