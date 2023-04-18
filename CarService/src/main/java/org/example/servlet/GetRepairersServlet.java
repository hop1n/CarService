package org.example.servlet;

import org.example.service.RepairerService;

import java.util.Map;

public class GetRepairersServlet extends JsonServlet {
    private final RepairerService repairerService;
    public GetRepairersServlet(RepairerService repairerService) {
        this.repairerService = repairerService;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(repairerService.getRepairers());
    }
}
