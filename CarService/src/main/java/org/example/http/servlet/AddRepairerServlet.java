package org.example.http.servlet;

import org.example.model.Repairer;
import org.example.service.RepairerService;

import java.util.Map;

public class AddRepairerServlet extends JsonServlet {
    private final RepairerService repairerService;

    public AddRepairerServlet(RepairerService repairerService) {
        this.repairerService = repairerService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response
                (repairerService.add
                        (new Repairer(body.get("name"))));
    }
}
