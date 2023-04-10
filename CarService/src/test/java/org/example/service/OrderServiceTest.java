package org.example.service;

import org.example.exception.*;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    private static final Order ORDER1 = new Order(100);
    private static final Order ORDER2 = new Order(180);
    private static final Order ORDER3 = new Order(150);
    private static final Repairer REPAIRER1 = new Repairer("Peter");
    private static final Repairer REPAIRER2 = new Repairer("Ivan");
    private static final Repairer REPAIRER3 = new Repairer("Kolya");
    private static final Repairer REPAIRER4 = new Repairer("Sasha");
    private static final Repairer REPAIRER5 = new Repairer("Olya");
    private static final GarageSlot GARAGE_SLOT = new GarageSlot();
    private static final GarageSlot GARAGE_SLOT1 = new GarageSlot();


    private RepairerServiceImpl repairerService;
    private GarageService garageService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        repairerService = new RepairerServiceImpl();
        garageService = new GarageService();
        orderService = new OrderService(repairerService, garageService);
        orderService.addOrder(ORDER1);
        orderService.addOrder(ORDER2);
        orderService.addOrder(ORDER3);
    }

    @Test
    void addOrderTest() {
        assertThat(orderService.getOrders()).hasSize(3);
    }

    @Test
    void getOrderByIdTest() {
        assertThat(orderService.getOrderById(ORDER2.getId()).getId()).isEqualTo(ORDER2.getId());
    }

    @Test
    void throwExceptionGetOrderSorterTest() {
        assertThrows(IncorrectSortTypeException.class, () -> orderService.getSortedOrders(null));
    }

    @Test
    void removeOrderTest() {
        orderService.removeOrder(ORDER1.getId());
        assertThat(orderService.getOrders()).size().isEqualTo(2);
    }

    @Test
    void removeOrderNotExistingEntityTest() {
        assertThrows(OrderNotFoundException.class, () -> orderService.removeOrder(100500));
    }

    @Test
    void assignRepairerTest() {
        repairerService.add(REPAIRER4);
        orderService.assignRepairer(ORDER3, REPAIRER4.getId());

        assertThat(orderService.getOrders().get(ORDER3.getId() - 1).getRepairers()).contains(REPAIRER4);
    }

    @Test
    void assignGarageSlotTest() {
        garageService.add(GARAGE_SLOT);
        orderService.assignGarageSlot(ORDER3, GARAGE_SLOT.getId());

        assertThat(orderService.getOrders().get(ORDER3.getId() - 1).getGarageSlot()).isEqualTo(GARAGE_SLOT);
    }

    @Test
    void assignGarageSlotExcTest() {
        garageService.add(GARAGE_SLOT1);
        GARAGE_SLOT1.setAvailable(false);
        assertThrows(GarageNotAvailableException.class, () -> orderService.assignGarageSlot(ORDER2,
                GARAGE_SLOT1.getId()));
    }

    @Test
    void assignRepairerExcTest() {
        repairerService.add(REPAIRER5);
        REPAIRER5.setIsAvailable(false);
        assertThrows(RepairerNotAvailableException.class, () -> orderService.assignRepairer(ORDER2,
                REPAIRER5.getId()));
    }

    @Test
    void completeOrder() {
        garageService.add(GARAGE_SLOT);
        orderService.assignGarageSlot(ORDER2, GARAGE_SLOT.getId());
        orderService.completeOrder(ORDER2.getId());

        assertTrue(ORDER2.getGarageSlot().isAvailable());
        assertFalse(ORDER2.isInProgress());
        assertTrue(ORDER2.getRepairers().stream().allMatch(Repairer::isAvailable));
    }

    @Test
    void addOrderExcTest() {
        Order order = new Order(-1);
        assertThrows(IncorrectCostException.class, () -> orderService.addOrder(order));
    }

    @Test
    void completeOrderExcTest() {
        ORDER1.setInProgress(false);
        assertThrows(OrderAlreadyCompletedException.class, () -> orderService.completeOrder(ORDER1.getId()));
    }

    @Test
    void getSortedOrders() {
        ORDER1.setInProgress(false);
        ORDER2.setCreationDate(LocalDate.parse("2023-04-07"));
        ORDER2.setCompletionDate(LocalDate.parse("2023-04-09"));
        ORDER3.setCompletionDate(LocalDate.parse("2023-04-10"));
        repairerService.add(REPAIRER1);
        repairerService.add(REPAIRER2);
        repairerService.add(REPAIRER3);

        orderService.assignRepairer(ORDER3, REPAIRER1.getId());
        orderService.assignRepairer(ORDER1, REPAIRER2.getId());
        orderService.assignRepairer(ORDER2, REPAIRER3.getId());


        assertAll(
                () -> {
                    List<Order> ordersSorted = new ArrayList<>();
                    ordersSorted = orderService.getSortedOrders("FINISHED");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-10,completionDate=null}, \n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-10,completionDate=2023-04-10}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("PROGRESS");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-10,completionDate=2023-04-10}, \n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-10,completionDate=null}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("COST");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-10,completionDate=null}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-10,completionDate=2023-04-10}, \n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-07,completionDate=2023-04-09}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("CREATION");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-10,completionDate=null}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-10,completionDate=2023-04-10}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("COMPLETION");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-10,completionDate=2023-04-10}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("REPAIRER");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-10,completionDate=2023-04-10}, \n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-10,completionDate=null}, \n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-07,completionDate=2023-04-09}]");
                }
        );
    }


}
