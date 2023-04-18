package org.example.servlet;

import org.example.model.Order;
import org.example.service.OrderService;

import java.util.Map;

public class AssignGarageOrderServlet extends JsonServlet {
    private final OrderService orderService;

    public AssignGarageOrderServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response(orderService.assignGarageSlot(orderService.getOrderById(
                Long.parseLong(body.get("id"))), Long.parseLong(body.get("garageId"))));
    }
}
