package org.example.service;

import org.example.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    private static final Order ORDER_1 = new Order(100);
    private static final Order ORDER_2 = new Order(120);

    private RepairerServiceImpl repairerService;
    private GarageService garageService;
    private OrderService orderService;

    @BeforeEach
    void prepare() {
        orderService = new OrderService(repairerService, garageService);
        orderService.addOrder(ORDER_1);
        orderService.addOrder(ORDER_2);
    }

    @Test
    void ordersEmptyIfNoOrderAdded() {
        assertTrue(orderService.getOrders().isEmpty());
    }

    @Test
    void addOrderTest() {
        assertThat(orderService.getOrders()).hasSize(2);
    }

    @Test
    void getOrderByIdTest() {
        assertThat(orderService.getOrders().get(1)).isEqualTo(ORDER_2);
    }

    @Test
    void throwExceptionIfOrderSorterIsNull() {
        assertThrows(NullPointerException.class, () -> orderService.getSortedOrders(null));
    }

    @AfterEach
    void deleteDateAfterUsing() {
        System.out.println("After each has worked, date was deleted.");
    }


}
