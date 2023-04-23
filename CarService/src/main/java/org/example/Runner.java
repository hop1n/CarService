package org.example;

import org.example.model.Order;
import org.example.repository.GarageRepository;
import org.example.repository.OrderRepository;
import org.example.service.ConsoleProcessor;
import org.example.servlet.HttpInterface;

import java.sql.SQLException;

public class Runner {

    public static void main(String[] args) throws SQLException {
//        ConsoleProcessor consoleProcessor = new ConsoleProcessor();
//        consoleProcessor.initLogs();
//        consoleProcessor.processConsole();

//        HttpInterface httpInterface = new HttpInterface();
//        httpInterface.start();

        GarageRepository garageRepository = new GarageRepository();
        OrderRepository orderRepository = new OrderRepository(garageRepository);

//        orderRepository.getOrders().forEach(System.out::println);
//        System.out.println(orderRepository.removeOrder(1L));
//        orderRepository.getSortedOrders("creation").forEach(System.out::println);
//        orderRepository.getOrders().forEach(System.out::println);
        orderRepository.assignGarageSlot(orderRepository.getOrderById(1L), 3L);
//        orderRepository.getSortedOrders("creation").forEach(System.out::println);
//        System.out.println(garageRepository.getById(3L));

    }
}
