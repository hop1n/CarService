package org.example.http.servlet;

import org.example.service.GarageService;

import java.util.Map;

public class RemoveGarageSlotServlet extends JsonServlet{
    private final GarageService garageService;

    public RemoveGarageSlotServlet(GarageService garageService) {
        this.garageService = garageService;
    }
    @Override
    Response post(String uri, Map<String, String> parameters) {
        long id = Long.parseLong(uri.substring(uri.lastIndexOf('/')+1));
        return new Response(garageService.remove(id));
    }
}
