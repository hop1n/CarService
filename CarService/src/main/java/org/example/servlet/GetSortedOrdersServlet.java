package org.example.servlet;

import org.example.service.OrderService;

import java.util.Map;

public class GetSortedOrdersServlet extends JsonServlet {

    private final OrderService orderService;

    public GetSortedOrdersServlet(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(orderService.getSortedOrders(
                parameters.getOrDefault("sort", new String[]{""})[0]));

    }
}
