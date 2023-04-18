package org.example.dto;

import org.example.model.Order;
import org.example.service.OrderService;

import java.util.List;

public class OrderServiceDto {

    private Long orderCount;
    private List<Order> orders;

    public OrderServiceDto(Long orderCount, List<Order> orders) {
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

    public Long getOrderCount() {
        return orderCount;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
