package org.example.servlet;

import org.example.service.OrderService;

import java.util.Map;

public class AssignRepairerOrderServlet extends JsonServlet {
    private final OrderService orderService;

    public AssignRepairerOrderServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response post(String uri, Map<String, String> body) {
        return new Response(orderService.assignRepairer(orderService
                .getOrderById(Long.parseLong(body.get("id"))), Integer.parseInt(body.get("repairerId"))));
    }
}
