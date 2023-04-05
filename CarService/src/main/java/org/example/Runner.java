package org.example;

import org.example.Service.GarageService;
import org.example.Service.OrderServiceImpl;
import org.example.Service.RepairerServiceImpl;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Runner {
    static RepairerServiceImpl repairerService = new RepairerServiceImpl();
    static GarageService garageService = new GarageService();
    static OrderServiceImpl orderService = new OrderServiceImpl(repairerService, garageService);


    public static void main( String[] args )
    {
        GarageSlot garageSlot1 = new GarageSlot();
        GarageSlot garageSlot2 = new GarageSlot();
        GarageSlot garageSlot3 = new GarageSlot();
        Repairer repairer1 = new Repairer("Tom");
        Repairer repairer2 = new Repairer("Alex");
        Repairer repairer3 = new Repairer("Jhon");
//        System.out.println("ids" + garageSlot1.getId() + garageSlot2.getId());


        repairerService.add(repairer1);
        repairerService.add(repairer3);
        repairerService.add(repairer2);



        garageService.add(garageSlot1);
        garageService.add(garageSlot2);
        garageService.add(garageSlot3);


        Order order1 = orderService.createOrder(100);
        orderService.assignRepairer(order1, repairer1.getId());
        orderService.assignGarageSlot(order1, garageSlot1.getId());

        Order order3 = orderService.createOrder(100);
        orderService.assignRepairer(order3, repairer3.getId());
        orderService.assignGarageSlot(order3, garageSlot3.getId());

//        Order order2 = orderService.createOrder(100,garageSlot1.getId());
        Order order2 = orderService.createOrder(120);
        orderService.assignRepairer(order2, repairer2.getId());
        orderService.assignGarageSlot(order2, garageSlot2.getId());
        orderService.completeOrder(order2.getId());



        System.out.println(orderService.getSortedOrders(5));


        //Console processing
        String input;
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            while (true) {
                System.out.println("=====");
                System.out.println("Please enter command or type \"help\" for command list. Enter \"exit\" to exit:");

                input = reader.readLine().toLowerCase().trim().replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "");

                if (input.equals("")){
                    System.out.println("");
                }

                if (input.equals("exit")) {
                    //тут будет сохранение в БД перед выходом
                    break;
                }

                String[] words = input.split(" ");

                switch (words[0]) {
                    case "repairer":
                        processRepairer(words);
                        break;
                    case "garage":
                        processGarage(words);
                        break;
                    case "orderlist":
                        processOrderList(words);
                        break;
                    case "order":
                        processOrder(words);
                        break;
                    case "help":
                        getHelp(words);
                        break;
                    default:
                        System.out.println("Unexpected value: " + words[0]);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processRepairer(String[] words) {
        switch (words[1]) {
            case "add":
                repairerService.add(new Repairer(words[2]));
                System.out.printf("New repairer %s added successfully\n", words[2]);
                break;
            case "remove":
                repairerService.remove(Integer.parseInt(words[2]));
                System.out.printf("Repairer %s deleted successfully\n", words[2]);
                break;
            case "printlist":
                repairerService.showSortedList();
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }
    public static void processGarage(String[] words) {
        switch (words[1]) {
            case "add":
                garageService.add(new GarageSlot());
                System.out.printf("New garage slot ID:%d added succesfully\n" +
                        "Total garage slots: %d\n",
                        garageService.getGarageSlots().get(garageService.getGarageSlots().size()-1).getId(),
                        garageService.getGarageSlots().size()
                );
                break;
            case "remove":
                garageService.remove(Integer.parseInt(words[2]));
                System.out.printf("Garage slot %d deleted succesfully\n" +
                        "Total garage slots: %d\n",
                        Integer.parseInt(words[2]),
                        garageService.getGarageSlots().size()
                );
                break;
            case "printlist" :
                garageService.showSorted();
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    public static void processOrderList(String[] words) {
        switch (words[1]) {
            case "add":
                orderService.createOrder(Integer.parseInt(words[3]));
                System.out.println("New order created successfully");
                break;
            case "printlist":
                System.out.println(orderService.getSortedOrders(Integer.parseInt(words[3])));
                break;
            case "get":
                System.out.println(orderService.getOrderById(Integer.parseInt(words[3])));
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    public static void processOrder(String[] words) {
        switch (words[1]) {
            case "create":
                orderService.createOrder(Integer.parseInt(words[3]));
                System.out.println("New order created successfully");
                break;
            case "remove":
                orderService.removeOrder(Integer.parseInt(words[3]));
                break;
            case "assign":
                if (words[4].equals("repairer")){
                    orderService.assignRepairer(orderService.getOrderById(Integer.parseInt(words[3])), Integer.parseInt(words[5]));
                } else if (words[4].equals("garage")){
                    orderService.assignGarageSlot(orderService.getOrderById(Integer.parseInt(words[3])), Integer.parseInt(words[5]));
                } else {
                    System.out.println("Cannot recognize assignment");
                }
            case "complete":
                orderService.completeOrder(Integer.parseInt(words[3]));
            case "get":
                System.out.println(orderService.getOrderById(Integer.parseInt(words[3])));
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    public static void getHelp(String[] words) {
        if (words.length<2) {
            System.out.println("Command structure: \"object action arguments\"");
            System.out.println("Objects: Repairer, Garage, Order, OrderList");
            System.out.println("For object commands, print \"help object\", for example: \"help repairer\"\n");
        }

        if (words[0].equals("help") && words[1].equals("repairer")){
            System.out.printf("Object Repairer, actions:  add name, remove id, printlist\n" +
                    "\tExample: Repairer add Piter\n" +
                    "\tExample: Repairer remove 2\n" +
                    "\tExample: Repairer printlist\n");
        }

        if (words[0].equals("help") && words[1].equals("garage")){
            System.out.printf("Object Garage, actions:  add, remove id, printlist\n" +
                    "\tExample: Garage add\n" +
                    "\tExample: Garage remove 2\n" +
                    "\tExample: Garage printlist\n");
        }

        if (words[0].equals("help") && words[1].equals("orderlist")){
            System.out.printf("Object OrderList, actions:  add cost amount, get id number, printlist type number\n" +
                    "\tExample: Orderlist add cost 100\n" +
                    "\tExample: Orderlist get id 2\n" +
                    "\tExample: Orderlist printlist type 2\n");
        }

        if (words[0].equals("help") && words[1].equals("order")){
            System.out.printf("Object Order, actions:  create cost amount, remove id number, assign repairer id number, get id number, printlist type number\n" +
                    "\tExample: Order create cost 100\n" +
                    "\tExample: Order remove id 3\n" +
                    "\tExample: Order complete id 3\n" +
                    "\tExample: Order assign id 4 repairer 2\n" +
                    "\tExample: Order assign id 4 garage 2\n" +
                    "\tExample: Garage printlist sort 2\n");
        }
    }
}
