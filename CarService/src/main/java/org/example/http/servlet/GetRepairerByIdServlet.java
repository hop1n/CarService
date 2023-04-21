package org.example.http.servlet;

import org.example.service.RepairerService;

import java.util.Map;

public class GetRepairerByIdServlet extends JsonServlet{
    private final RepairerService repairerService;

    public GetRepairerByIdServlet(RepairerService repairerService) {
        this.repairerService = repairerService;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        long id = Long.parseLong(uri.substring(uri.lastIndexOf('/')+1));
        return new Response(repairerService.getById(id));
    }
}
