package org.example;

import org.example.Service.GarageService;
import org.example.model.GarageSlot;

public class Runner
{
    public static void main( String[] args )
    {
        GarageSlot garageSlot1 = new GarageSlot();
        GarageSlot garageSlot2 = new GarageSlot();
        System.out.println("ids" + garageSlot1.getId() + garageSlot2.getId());
    }
}
