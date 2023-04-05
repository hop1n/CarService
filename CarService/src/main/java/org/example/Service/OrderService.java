package org.example.Service;

import org.example.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(int cost, int garageSlotId);

    void removeOrder(int id);

    void assignRepairer (Order order, int... id);

    void assignGarageSlot (int id);

    void completeOrder (int id);

    Order getOrderById(int id);

    List<Order> getOrders();

    List<Order> getSortedOrders(int sortType);
}
