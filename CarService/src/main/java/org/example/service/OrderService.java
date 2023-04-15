package org.example.service;

import org.example.exception.*;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {

    enum Fields {
        CREATION, COMPLETION, COST, FINISHED, PROGRESS, REPAIRER
    }

    private final RepairerService repairerService;
    private final GarageService garageService;
    private int orderCount;
    private List<Order> orders = new ArrayList<>();

    public OrderService(RepairerService repairerService, GarageService garageService) {
        this.repairerService = repairerService;
        this.garageService = garageService;
        orderCount = 0;
    }

    public void addOrder(Order order) {
        if (order.getCost() < 0) throw new IncorrectCostException("Cost shouldn't be less then 0");
        orders.add(order);
        orderCount++;
        order.setId(orderCount);
    }

    public void removeOrder(int id) {
        if (!orders.removeIf(order -> order.getId() == id)) {
            throw new OrderNotFoundException("There is no order with ID%d".formatted(id));
        }
    }

    public void assignRepairer(Order order, int... ids) {
        if (!order.isInProgress()) {
            throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(order.getId()));
        }
        for (int id : ids) {
            if (repairerService.getById(id).getIsAvailable()) {
                order.addRepair(repairerService.getById(id));
                repairerService.getById(id).setIsAvailable(false);
            } else throw new RepairerNotAvailableException("Repairer with ID%d is unavailable".formatted(id));
        }
    }

    public void assignGarageSlot(Order order, int garageId) {
        if (!order.isInProgress()) {
            throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(order.getId()));
        }
        if (garageService.getById(garageId).isAvailable()) {
            if (order.getGarageSlot() != null) {
                order.getGarageSlot().setAvailable(true); // освобождает гараж, который уже был назначен этому заказу
            }
            order.setGarageSlot(garageService.getById(garageId));
            garageService.getById(garageId).setAvailable(false);
        } else throw new GarageNotAvailableException("Garage with ID%d is unavailable".formatted(garageId));
    }

    public void completeOrder(int id) {
        Order order = orders.stream().filter(o -> o.getId() == id).findAny()
                .orElseThrow(() -> new OrderNotFoundException("There is no order with ID%d".formatted(id)));
        if (!order.isInProgress()) {
            throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(id));
        }

        Collection<Repairer> repairers = order.getRepairers();
        if (!repairers.isEmpty()) {
            for (Repairer repairer : repairers) {
                repairer.setIsAvailable(true);
            }
        } else throw new RepairerIsNotAssignedException("Assign a repairer first");

        GarageSlot garageSlot = order.getGarageSlot();
        if (garageSlot != null) {
            order.getGarageSlot().setAvailable(true);
            order.setCompletionDate(LocalDate.now());
        } else throw new GarageIsNotAssignedException("Assign a garage slot first");

        order.setInProgress(false);
    }

    public List<Order> getSortedOrders(String field) {
        try {
            switch (Fields.valueOf(field.toUpperCase())) {
                case CREATION:
                    return orders.stream().sorted(Comparator.comparing(Order::getCreationDate)).collect(Collectors.toList());
                case COMPLETION:
                    return orders.stream().filter(order -> order.getCompletionDate() != null)
                            .sorted(Comparator.comparing(Order::getCompletionDate)).collect(Collectors.toList());
                case COST:
                    return orders.stream().sorted(Comparator.comparing(Order::getCost))
                            .collect(Collectors.toList());
                case PROGRESS:
                    return orders.stream().sorted(Comparator.comparing(o -> !o.isInProgress()))
                            .collect(Collectors.toList());
                case FINISHED:
                    return orders.stream().sorted(Comparator.comparing(Order::isInProgress))
                            .collect(Collectors.toList());
                case REPAIRER:
                    orders.sort(Comparator.comparingInt(o -> o.getRepairers().iterator().next().getId()));
                    return orders;
            }
            return orders;
        } catch (IllegalArgumentException ex) {
            throw new IncorrectSortTypeException("There is no such sort type as: %s".formatted(field));
        }
    }

    public Order getOrderById(int id) {
        return orders.stream().filter(order -> order.getId() == id).findAny()
                .orElseThrow(() -> new OrderNotFoundException("There is no order with ID%d ".formatted(id)));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}