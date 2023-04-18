package org.example.servlet;

import org.example.model.Order;
import org.example.service.OrderService;

import java.util.Map;

public class CreateOrderServlet extends JsonServlet {

    private final OrderService orderService;

    public CreateOrderServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response(orderService.addOrder(new Order(Integer.parseInt(body.get("cost")))));
    }

}
