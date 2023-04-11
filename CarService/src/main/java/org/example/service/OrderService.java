package org.example.service;

import org.example.exception.*;
import org.example.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(int cost);

    void removeOrder(int id);

    private final RepairerServiceImpl repairerService;
    private final GarageService garageService;
    private final List<Order> orders = new ArrayList<>();

    void assignGarageSlot (Order order, int id);

    void completeOrder (int id);

    public void removeOrder(int id) {
        if (!orders.removeIf(order -> order.getId() == id)) {
            throw new OrderNotFoundException("There is no order with ID " + id);
        }
    }

    public void assignRepairer(Order order, int... ids) {
        for (int id : ids) {
            if (repairerService.getById(id).isAvailable()) {
                order.addRepair(repairerService.getById(id));
                repairerService.getById(id).setIsAvailable(false);
            } else throw new RepairerNotAvailableException("Repairer with ID " + id + " is unavailable");
        }
    }

    public void assignGarageSlot(Order order, int id) {
        if (garageService.getById(id).isAvailable()) {
            if (order.getGarageSlot() != null) {
                order.getGarageSlot().setAvailable(true); // освобождает гараж, который уже был назначен этому заказу
            }
            order.setGarageSlot(garageService.getById(id));
            garageService.getById(id).setAvailable(false);
        } else throw new GarageNotAvailableException("Garage with ID " + id + " is unavailable");
    }

    public void completeOrder(int id) {
        Order order = orders.stream().filter(o -> o.getId() == id).findAny()
                .orElseThrow(() -> new OrderNotFoundException("There is no order with ID " + id));
        if (!order.isInProgress()) {
            throw new OrderAlreadyCompletedException("Order with ID " + id + " already completed");
        }
        order.setInProgress(false);
        Collection<Repairer> repairers = order.getRepairers();
        for (Repairer repairer : repairers) {
            repairer.setIsAvailable(true);
        }
        order.getGarageSlot().setAvailable(true);
        order.setCompletionDate(LocalDate.now());
    }

    public Order getOrderById(int id) {
        return orders.stream().filter(order -> order.getId() == id).findAny()
                .orElseThrow(() -> new OrderNotFoundException("There is no order with ID " + id));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Order> getSortedOrders(String field) {
        if (Fields.checkValue(field) == null) {
            throw new IncorrectSortTypeException("There is no sort type: " + field);
        }
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
    }
}