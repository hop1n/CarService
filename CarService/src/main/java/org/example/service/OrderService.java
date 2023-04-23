package org.example.service;

import org.example.exception.*;
import org.example.model.Order;
import org.example.model.Repairer;
import org.example.repository.GarageRepository;
import org.example.repository.OrderRepository;
import org.example.repository.RepairerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    public enum Fields {
        CREATION, COMPLETION, COST, FINISHED, PROGRESS, REPAIRER
    }

    private final GarageRepository garageRepository = new GarageRepository();
    private final RepairerRepository repairerRepository = new RepairerRepository();
    private final OrderRepository orderRepository = new OrderRepository(garageRepository, repairerRepository);
    public OrderService() {
    }

    private RepairerService repairerService;
    private GarageService garageService;
    private Long orderCount;
    private List<Order> orders = new ArrayList<>();

    public OrderService(RepairerService repairerService, GarageService garageService) {
        this.repairerService = repairerService;
        this.garageService = garageService;
        orderCount = 0L;
    }

    public Boolean addOrder(Order order) {
        return orderRepository.addOrder(order);
//        if (order.getCost() < 0) throw new IncorrectCostException("Cost shouldn't be less then 0");
//        orders.add(order);
//        orderCount++;
//        order.setId(orderCount);
//        return order;
    }

    public boolean removeOrder(Long id) {
        return orderRepository.removeOrder(id);
//        if (!orders.removeIf(order -> order.getId() == id)) {
//            throw new OrderNotFoundException("There is no order with ID%d".formatted(id));
//        }
//        return true;
    }

    public Order  assignRepairer(Order order, Long... ids) {
        for (Long id : ids) {
            orderRepository.assignRepairer(order, id);
//            if (repairerService.getById(id).getIsAvailable()) {
//                order.addRepair(repairerService.getById(id));
//                repairerService.getById(id).setIsAvailable(false);
//            } else throw new RepairerNotAvailableException("Repairer with ID%d is unavailable".formatted(id));
        }
        return order;
    }

    public Order assignGarageSlot(Order order, Long garageId) {
        return orderRepository.assignGarageSlot(order, garageId);
//        if (garageService.getById(garageId).isAvailable()) {
//            if (order.getGarageSlot() != null) {
//                order.getGarageSlot().setAvailable(true);
//            }
//            order.setGarageSlot(garageService.getById(garageId));
//            garageService.getById(garageId).setAvailable(false);
//        } else throw new GarageNotAvailableException("Garage with ID%d is unavailable".formatted(garageId));
//        return order;
    }


    public Order completeOrder(Long id) {
        return orderRepository.completeOrder(id);
//        Order order = orders.stream().filter(o -> o.getId() == id).findAny()
//                .orElseThrow(() -> new OrderNotFoundException("There is no order with ID%d".formatted(id)));
//        if (!order.isInProgress()) {
//            throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(id));
//        }
//        Collection<Repairer> repairers = order.getRepairers();
//        GarageSlot garageSlot = order.getGarageSlot();
//        if (!repairers.isEmpty() && garageSlot != null) {
//            for (Repairer repairer : repairers) {
//                repairer.setIsAvailable(true);
//            }
//            order.getGarageSlot().setAvailable(true);
//            order.setCompletionDate(LocalDate.now());
//        } else throw new RepairerOrGarageIsNotAssignedException ("You have to assign repairer" +
//                " and garage slot to complete the order");
//        order.setInProgress(false);
    }

    public List<Order> getSortedOrders(String field) {
        return orderRepository.getSortedOrders(field);
//        try {
//            switch (Fields.valueOf(field.toUpperCase())) {
//                case CREATION:
//                    return orders.stream().sorted(Comparator.comparing(Order::getCreationDate)).collect(Collectors.toList());
//                case COMPLETION:
//                    return orders.stream().filter(order -> order.getCompletionDate() != null)
//                            .sorted(Comparator.comparing(Order::getCompletionDate)).collect(Collectors.toList());
//                case COST:
//                    return orders.stream().sorted(Comparator.comparing(Order::getCost))
//                            .collect(Collectors.toList());
//                case PROGRESS:
//                    return orders.stream().sorted(Comparator.comparing(o -> !o.isInProgress()))
//                            .collect(Collectors.toList());
//                case FINISHED:
//                    return orders.stream().sorted(Comparator.comparing(Order::isInProgress))
//                            .collect(Collectors.toList());
//                case REPAIRER:
//                    orders.sort(Comparator.comparingLong(o -> o.getRepairers().iterator().next().getId()));
//                    return orders;
//            }
//            return orders;
//        } catch (IllegalArgumentException ex) {
//            throw new IncorrectSortTypeException("There is no such sort type as: %s".formatted(field));
//        }
    }

    public Order getOrderById(Long id) {
        return orderRepository.getOrderById(id);
//        return orders.stream().filter(order -> order.getId() == id).findAny()
//                .orElseThrow(() -> new OrderNotFoundException("There is no order with ID%d ".formatted(id)));
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}