package org.example.http.servlet;

import org.example.model.GarageSlot;
import org.example.service.GarageService;

import java.util.Map;

public class AddGarageSlotServlet extends JsonServlet{
    private final GarageService garageService;

    public AddGarageSlotServlet(GarageService garageService) {
        this.garageService = garageService;
    }

    @Override
    Response post(String uri, Map<String, String> parameters) {
        GarageSlot garageSlot = new GarageSlot();
        return new Response(garageService.add(garageSlot));
    }
}
