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
    }

    public boolean removeOrder(Long id) {
        return orderRepository.removeOrder(id);
    }

    public Order  assignRepairer(Order order, Long... ids) {
        return orderRepository.assignRepairer(order, ids);
    }

    public Order assignGarageSlot(Order order, Long garageId) {
        return orderRepository.assignGarageSlot(order, garageId);
    }

    public Order completeOrder(Long id) {
        return orderRepository.completeOrder(id);
    }

    public List<Order> getSortedOrders(String field) {
        return orderRepository.getSortedOrders(field);
    }

    public Order getOrderById(Long id) {
        return orderRepository.getOrderById(id);
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