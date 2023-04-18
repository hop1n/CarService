package org.example.servlet;

import org.example.model.Order;
import org.example.service.OrderService;

import java.util.Map;

public class CompleteOrderServlet extends JsonServlet {
    private final OrderService orderService;

    public CompleteOrderServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response(orderService.completeOrder(Long.parseLong(body.get("id"))));
    }
}
