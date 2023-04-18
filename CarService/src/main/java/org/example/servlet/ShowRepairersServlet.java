package org.example.servlet;

import org.eclipse.jetty.server.Response;
import org.example.service.RepairerService;

import java.util.Map;

public class ShowRepairersServlet extends JsonServlet {
    private final RepairerService repairerService;
    public ShowRepairersServlet(RepairerService repairerService) {
        this.repairerService = repairerService;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(repairerService.getRepairers());
    }
}
