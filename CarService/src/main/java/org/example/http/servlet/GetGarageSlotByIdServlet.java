package org.example.http.servlet;

import org.example.service.GarageService;

import java.util.Map;

public class GetGarageSlotByIdServlet extends JsonServlet{
    private final GarageService garageService;

    public GetGarageSlotByIdServlet(GarageService garageService) {
        this.garageService = garageService;
    }
    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        long id = Long.parseLong(uri.substring(uri.lastIndexOf('/')+1));
        return new Response(garageService.getById(id));
    }
}
