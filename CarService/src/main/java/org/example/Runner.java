package org.example;

import org.example.model.Order;
import org.example.model.Repairer;
import org.example.repository.GarageRepository;
import org.example.repository.OrderRepository;
import org.example.repository.RepairerRepository;
//import org.example.service.ConsoleProcessor;
import org.example.http.HttpInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class Runner {

    public static void main(String[] args) throws SQLException {
//        ConsoleProcessor consoleProcessor = new ConsoleProcessor();
//        consoleProcessor.initLogs();
//        consoleProcessor.processConsole();

        HttpInterface httpInterface = new HttpInterface();
        httpInterface.start();

//        GarageRepository garageRepository = new GarageRepository();
//        RepairerRepository repairerRepository = new RepairerRepository();
//        OrderRepository orderRepository = new OrderRepository(garageRepository, repairerRepository);

//        orderRepository.getOrders().forEach(System.out::println);
//        System.out.println(orderRepository.removeOrder(1L));
//        orderRepository.getSortedOrders("repairer").forEach(System.out::println);
//        orderRepository.getOrders().forEach(System.out::println);

//        Order order = orderRepository.assignGarageSlot(orderRepository.getOrderById(1L), 3L);
//        System.out.println(order);
//
//        Repairer r2 = repairerRepository.getById(2L);
//        Order order = orderRepository.assignRepairer(orderRepository.getOrderById(1L), 2L);
//        System.out.println(order);

        //NullPointerException при доабвлении новых repairers - устранен!
//        Repairer newRepairer = repairerRepository.add(new Repairer("Blablabla"));
//        Repairer r4 = new Repairer("Jhon");
//        orderRepository.assignRepairer(orderRepository.getOrderById(1L), newRepairer);

//        System.out.println(orderRepository.completeOrder(1L));
//        System.out.println(orderRepository.getOrderById(11L));


//        orderRepository.getSortedOrders("creation").forEach(System.out::println);
//        System.out.println(garageRepository.getById(1L));

//        Order order = new Order(122);
//        orderRepository.addOrder(order);

//        System.out.println(orderRepository.removeOrder(1L));
    }
}
