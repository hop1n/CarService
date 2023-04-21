package org.example.http.servlet;

import org.example.service.RepairerService;

import java.util.Map;

public class RemoveRepairerServlet extends JsonServlet {
    private final RepairerService repairerService;

    public RemoveRepairerServlet(RepairerService repairerService) {
        this.repairerService = repairerService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response
                (repairerService.remove(
                        (Long.parseLong((body.get("id"))))));
    }
}
