package org.example.service;

import org.example.model.Order;
import org.example.model.Repairer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderServiceImpl {

    enum Fields {
        CREATION, COMPLETION, COST, FINISHED, PROGRESS, REPAIR
    }

    private final RepairerServiceImpl repairerService;
    private final GarageService garageService;

    private final List<Order> orders = new ArrayList<>();

    public OrderServiceImpl(RepairerServiceImpl repairerService, GarageService garageService) {
        this.repairerService = repairerService;
        this.garageService = garageService;
    }

    public Order createOrder(int cost) {
        if (cost < 0) throw new IllegalArgumentException("Cost shouldn't be less then 0");
        Order order = new Order(cost);
        orders.add(order);
        return order;
    }


    public void removeOrder(int id) {
        if (id < 0) throw new IllegalArgumentException("Cost shouldn't be less then 0");
        for (Order order : orders) {
            if (order.getId() == id) {
                orders.remove(order);
            } else throw new NoSuchElementException("There is no such ID");
        }
    }


    public void assignRepairer(Order order, int... ids) {
        for (int id : ids) {
            if (id < 0) throw new IllegalArgumentException("ID shouldn't be less then 0");
            order.addRepair(repairerService.getById(id));
            repairerService.getById(id).setIsAvailable(false);
        }
    }


    public void assignGarageSlot(Order order, int id) {
        if (id < 0) throw new IllegalArgumentException("ID shouldn't be less then 0");
        order.setGarageSlot(garageService.getById(id));
        garageService.getById(id).setAvailable(false);
    }


    public void completeOrder(int id) {
        Order order = orders.stream().filter(o -> o.getId() == id).findAny().orElse(null);
        if (order != null) {
            order.setInProgress(false);
            Collection<Repairer> repairers = order.getRepairers();
            for (Repairer repairer : repairers) {
                repairer.setIsAvailable(true);
            }
            order.getGarageSlot().setAvailable(true);
            order.setCompletionDate(LocalDateTime.now());
        } else throw new IllegalArgumentException ("There is no order with such ID");
    }


    public Order getOrderById(int id) {
        if (id < 0) throw new IllegalArgumentException("ID shouldn't be less then 0");
        return orders.stream().filter(order -> order.getId() == id).findAny().orElse(null);
    }


    public List<Order> getOrders() {
        return orders;
    }


    public List<Order> getSortedOrders(Fields fields) {
        switch (fields) {
            case CREATION:
                return orders.stream().sorted(Comparator.comparing(Order::getCreationDate))
                        .collect(Collectors.toList());
            case COMPLETION:
                return orders.stream().filter(order -> order.getCompletionDate() != null)
                        .sorted(Comparator.comparing(Order::getCompletionDate))
                        .collect(Collectors.toList());
            case COST:
                return orders.stream().sorted(Comparator.comparing(Order::getCost))
                        .collect(Collectors.toList());
            case PROGRESS:
                return orders.stream().sorted(Comparator.comparing(Order::isInProgress))
                        .collect(Collectors.toList());
            case FINISHED:
                return orders.stream().sorted(Comparator.comparing(x -> !x.isInProgress()))
                        .collect(Collectors.toList());
            case REPAIR:
                orders.sort(Comparator.comparingInt(o -> o.getRepairers()
                        .iterator().next().getId()));
                return orders;
        }
        return orders;
    }

}

