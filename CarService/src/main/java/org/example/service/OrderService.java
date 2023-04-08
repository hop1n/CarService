package org.example.service;

import org.example.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(int cost);

    void removeOrder(int id);

    void assignRepairer (Order order, int... id);

    void assignGarageSlot (Order order, int id);

    void completeOrder (int id);

    Order getOrderById(int id);

    List<Order> getOrders();

    List<Order> getSortedOrders(int sortType);
}
