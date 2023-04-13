package org.example.dto;

import org.example.model.Order;
import org.example.service.OrderService;

import java.util.List;

public class OrderServiceDto {

    private int orderCount;
    private List<Order> orders;

    public OrderServiceDto(int orderCount, List<Order> orders) {
        this.orderCount = orderCount;
        this.orders = orders;
    }

    public OrderServiceDto() {
    }

    public static OrderServiceDto fromService(OrderService orderService){
        return new OrderServiceDto(orderService.getOrderCount(), orderService.getOrders());
    }

    public void toService(OrderService orderService){
        orderService.setOrders(this.orders);
        orderService.setOrderCount(this.orderCount);
    }

    public int getOrderCount() {
        return orderCount;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
