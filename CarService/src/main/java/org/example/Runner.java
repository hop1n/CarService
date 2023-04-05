package org.example;

import org.example.Service.GarageService;
import org.example.Service.OrderServiceImpl;
import org.example.Service.RepairerServiceImpl;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;

public class Runner
{
    public static void main( String[] args )
    {
        GarageSlot garageSlot1 = new GarageSlot();
        GarageSlot garageSlot2 = new GarageSlot();
        Repairer repairer1 = new Repairer("Tom");
        Repairer repairer2 = new Repairer("Alex");
        Repairer repairer3 = new Repairer("Jhon");
//        System.out.println("ids" + garageSlot1.getId() + garageSlot2.getId());

        RepairerServiceImpl repairerService = new RepairerServiceImpl();
        repairerService.add(repairer1);
        repairerService.add(repairer3);
        repairerService.add(repairer2);


        GarageService garageService = new GarageService();
        garageService.add(garageSlot1);
        garageService.add(garageSlot2);
        OrderServiceImpl orderService = new OrderServiceImpl(repairerService, garageService);

        Order order1 = orderService.createOrder(100,garageSlot1.getId());
        orderService.assignRepairer(order1, repairer1.getId());

        Order order3 = orderService.createOrder(100,garageSlot1.getId());
        orderService.assignRepairer(order3, repairer3.getId());

//        Order order2 = orderService.createOrder(100,garageSlot1.getId());
        Order order2 = orderService.createOrder(120, garageSlot2.getId());
        orderService.assignRepairer(order2, repairer2.getId());
        orderService.completeOrder(order2.getId());



        System.out.println(orderService.getSortedOrders(5));
    }
}
