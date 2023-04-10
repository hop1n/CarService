package org.example.service;

import org.example.model.Order;
import org.example.model.Repairer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private final RepairerServiceImpl repairerService;
    private final GarageService garageService;

    private final List<Order> orders = new ArrayList<>();

    public OrderServiceImpl(RepairerServiceImpl repairerService, GarageService garageService) {
        this.repairerService = repairerService;
        this.garageService = garageService;
    }

    @Override
    public Order createOrder(int cost) {
        Order order = new Order(cost);
        orders.add(order);
        return order;
    }

    @Override
    public void removeOrder(int id) {
        orders.removeIf(order -> order.getId() == id);
    }


    @Override
    public void assignRepairer(Order order, int... ids) {
        for (int id : ids) {
            order.addRepair(repairerService.getById(id));
            repairerService.getById(id).setIsAvailable(false);
        }
    }

    @Override
    public void assignGarageSlot(Order order, int id) {
        order.setGarageSlot(garageService.getById(id));
        garageService.getById(id).setAvailable(false);
    }

    @Override
    public void completeOrder(int id) {
        Order order = orders.stream().filter(o -> o.getId() == id).findAny().orElse(null);
        if (order != null) {
            order.setInProgress(false);
            List<Repairer> repairers = order.getRepairers();
            for (Repairer repairer : repairers) {
                repairer.setIsAvailable(true);
            }
            order.getGarageSlot().setAvailable(true);
            order.setCompletionDate(LocalDateTime.now());
        }
    }

    @Override
    public Order getOrderById(int id) {
        return orders.stream().filter(order -> order.getId() == id).findAny().orElse(null);
    }

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public List<Order> getSortedOrders(int sortType) {
        switch (sortType) {
            case 1:
                return orders.stream().sorted(Comparator.comparing(Order::getCreationDate))
                        .collect(Collectors.toList());
            case 2:
                return orders.stream().filter(order -> order.getCompletionDate() != null)
                        .sorted(Comparator.comparing(Order::getCompletionDate))
                        .collect(Collectors.toList());
            case 3:
                return orders.stream().sorted(Comparator.comparing(Order::getCost))
                        .collect(Collectors.toList());
            case 4:
                return orders.stream().sorted(Comparator.comparing(Order::isInProgress))
                        .collect(Collectors.toList());
            case 5:
                orders.sort(Comparator.comparingInt(o -> o.getRepairers().get(0).getId()));
                return orders;
        }
        return orders;
    }

}

