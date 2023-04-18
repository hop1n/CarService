package org.example.servlet;

import org.example.service.OrderService;

import java.util.Map;

public class GetOrderByIdServlet extends JsonServlet {

    private final OrderService orderService;

    public GetOrderByIdServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        Long id = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
        return new Response(orderService.getOrderById(id));
    }
}
