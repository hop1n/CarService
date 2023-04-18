package org.example.servlet;

import org.example.service.OrderService;

import java.util.Map;

public class GetOrdersServlet extends JsonServlet {

    private final OrderService orderService;

    public GetOrdersServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(orderService.getOrders());

    }
}
