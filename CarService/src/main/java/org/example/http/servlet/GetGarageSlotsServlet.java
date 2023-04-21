package org.example.http.servlet;

import org.example.service.GarageService;

import java.util.Map;

public class GetGarageSlotsServlet extends JsonServlet{
    private final GarageService garageService;

    public GetGarageSlotsServlet(GarageService garageService) {
        this.garageService = garageService;
    }
    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(garageService.getGarageSlots());
    }
}
