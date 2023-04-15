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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    private static final Order ORDER1 = new Order(90);
    private static final Order ORDER2 = new Order(180);
    private static final Order ORDER3 = new Order(150);
    private static final Order ORDER4 = new Order(200);
    private static final Repairer REPAIRER1 = new Repairer("Peter");
    private static final Repairer REPAIRER2 = new Repairer("Ivan");
    private static final Repairer REPAIRER3 = new Repairer("Kolya");
    private static final Repairer REPAIRER4 = new Repairer("Sasha");
    private static final Repairer REPAIRER5 = new Repairer("Olya");
    private static final GarageSlot GARAGE_SLOT = new GarageSlot();
    private static final GarageSlot GARAGE_SLOT1 = new GarageSlot();

    private RepairerService repairerService;
    private GarageService garageService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        repairerService = new RepairerService();
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
        assertThatThrownBy(() -> orderService.getSortedOrders("wrong value")).isInstanceOf(IncorrectSortTypeException.class); // when null - dos not work
    }

    @Test
    void removeOrderTest() {
        orderService.removeOrder(ORDER1.getId());
        assertThat(orderService.getOrders()).size().isEqualTo(2);
    }

    @Test
    void removeOrderNotExistingEntityTest() {
        assertThatThrownBy(() -> orderService.removeOrder(100500)).isInstanceOf(OrderNotFoundException.class);
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
        assertThatThrownBy(() -> orderService.assignGarageSlot(ORDER2,
                GARAGE_SLOT1.getId())).isInstanceOf(GarageNotAvailableException.class);
    }

    @Test
    void assignRepairerExcTest() {
        repairerService.add(REPAIRER5);
        REPAIRER5.setIsAvailable(false);
        assertThatThrownBy(() -> orderService.assignRepairer(ORDER3,
                REPAIRER5.getId())).isInstanceOf(RepairerNotAvailableException.class);
    }

    @Test
    void completeOrderTest() {
        orderService.addOrder(ORDER4);
        garageService.add(GARAGE_SLOT);
        repairerService.add(REPAIRER4);
        orderService.assignGarageSlot(ORDER4, GARAGE_SLOT.getId());
        orderService.assignRepairer(ORDER4, REPAIRER4.getId());
        orderService.completeOrder(ORDER4.getId());

        assertTrue(ORDER4.getGarageSlot().isAvailable());
        assertFalse(ORDER4.isInProgress());
        assertTrue(ORDER4.getRepairers().stream().allMatch(Repairer::getIsAvailable));
    }

    @Test
    void addOrderExcTest() {
        Order order = new Order(-1);
        assertThatThrownBy(() -> orderService.addOrder(order)).isInstanceOf(IncorrectCostException.class);
    }

    @Test
    void completeOrderExcTest() {
        ORDER1.setInProgress(false);
        assertThatThrownBy(() -> orderService.completeOrder(ORDER1.getId())).isInstanceOf(OrderAlreadyCompletedException.class);
    }

    @Test
    void getOrderCountTest(){
        assertThat(orderService.getOrders().size()).isEqualTo(orderService.getOrderCount());
    }

    @Test
    void setOrderTest(){
        List <Order> ordersForAdded = new ArrayList<>();
        Order order4 = new Order();
        Order order5 = new Order();
        ordersForAdded.add(order4);
        ordersForAdded.add(order5);
        orderService.setOrders(ordersForAdded);

        assertThat(orderService.getOrders().size()).isEqualTo(2);
    }

    @Test
    void setOrderCountTest(){
        List <Order> ordersForAdded = new ArrayList<>();
        Order order4 = new Order();
        Order order5 = new Order();
        ordersForAdded.add(order4);
        ordersForAdded.add(order5);
        orderService.setOrders(ordersForAdded);
        orderService.setOrderCount(2);
        assertThat(orderService.getOrders().size()).isEqualTo(orderService.getOrderCount());
    }

    @Test
    void getSortedOrdersSortingTest() {
        ORDER1.setCreationDate(LocalDate.parse("2023-04-07"));
        ORDER1.setCompletionDate(LocalDate.parse("2023-04-09"));
        ORDER2.setCreationDate(LocalDate.parse("2023-04-06"));
        ORDER2.setCompletionDate(LocalDate.parse("2023-04-08"));
        ORDER3.setCreationDate(LocalDate.parse("2023-04-09"));
        ORDER3.setCompletionDate(LocalDate.parse("2023-04-11"));
        ORDER1.setCost(100);
        repairerService.add(REPAIRER1);
        repairerService.add(REPAIRER2);
        repairerService.add(REPAIRER3);

        ORDER1.setInProgress(true);
        orderService.assignRepairer(ORDER3, REPAIRER1.getId());
        orderService.assignRepairer(ORDER1, REPAIRER2.getId());
        orderService.assignRepairer(ORDER2, REPAIRER3.getId());
        ORDER1.setInProgress(false);

        assertAll(
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("FINISHED");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-06,completionDate=2023-04-08}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-09,completionDate=2023-04-11}]");
                },
        () -> {
            List<Order> ordersSorted;
            ordersSorted = orderService.getSortedOrders("PROGRESS");

            assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                    "Order{id=2,garageSlot=null,repairers=[\n" +
                    "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-06,completionDate=2023-04-08}, \n" +
                    "Order{id=3,garageSlot=null,repairers=[\n" +
                    "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-09,completionDate=2023-04-11}, \n" +
                    "Order{id=1,garageSlot=null,repairers=[\n" +
                    "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-07,completionDate=2023-04-09}]");
        },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("COST");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-09,completionDate=2023-04-11}, \n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-06,completionDate=2023-04-08}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("CREATION");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-06,completionDate=2023-04-08}, \n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-09,completionDate=2023-04-11}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("COMPLETION");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-06,completionDate=2023-04-08}, \n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-09,completionDate=2023-04-11}]");
                },
                () -> {
                    List<Order> ordersSorted;
                    ordersSorted = orderService.getSortedOrders("REPAIRER");

                    assertThat(ordersSorted.toString()).isEqualTo("[\n" +
                            "Order{id=3,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Peter',isAvailable=false,id=1,}],cost=150,inProgress=true,creationDate=2023-04-09,completionDate=2023-04-11}, \n" +
                            "Order{id=1,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Ivan',isAvailable=false,id=2,}],cost=100,inProgress=false,creationDate=2023-04-07,completionDate=2023-04-09}, \n" +
                            "Order{id=2,garageSlot=null,repairers=[\n" +
                            "Repairer{name='Kolya',isAvailable=false,id=3,}],cost=180,inProgress=true,creationDate=2023-04-06,completionDate=2023-04-08}]");
                }
               );
    }
}
