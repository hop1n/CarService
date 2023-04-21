package org.example.http.servlet;

import org.example.service.OrderService;

import java.util.Map;

public class RemoveOrderServlet extends JsonServlet {

    private final OrderService orderService;

    public RemoveOrderServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response(orderService.removeOrder(Long.parseLong(body.get("id"))));
    }

}
