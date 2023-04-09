package org.example.service;

import org.example.exception.*;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleProcessor {

    static RepairerServiceImpl repairerService = new RepairerServiceImpl();
    static GarageService garageService = new GarageService();
    static OrderService orderService = new OrderService(repairerService, garageService);

    //Initial database logs creation
    public void initLogs() {
        GarageSlot garageSlot1 = new GarageSlot();
        GarageSlot garageSlot2 = new GarageSlot();
        GarageSlot garageSlot3 = new GarageSlot();
        Repairer repairer1 = new Repairer("Tom");
        Repairer repairer2 = new Repairer("Alex");
        Repairer repairer3 = new Repairer("Jhon");


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


        System.out.println(orderService.getOrders());
    }

    //Method for input processing
    public void processConsole() {

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String input;


            while (true) {
                System.out.println("=====");
                System.out.println("Please enter command or type \"help\" for command list. Enter \"exit\" to exit:");

                input = reader.readLine().toLowerCase().trim().replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "");

                if (input.equals("exit")) {
                    break;
                }

                String[] words = input.split(" ");

                try {
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
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please specify operation for selected object");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Method for Repairers type processing
    public static void processRepairer(String[] words) {
        switch (words[1]) {
            case "add":
                //Checking for empty name
                try {
                    String repairerName = words[2].substring(0, 1).toUpperCase() + words[2].substring(1);
                    repairerService.add(new Repairer(repairerName));
                    System.out.printf("New repairer %s added successfully\n", repairerName);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add repairer's name");
                }
                break;

            case "remove":
                try {
                    repairerService.remove(Integer.parseInt(words[2]));
                    System.out.printf("Repairer %s deleted successfully\n", words[2]);
                } catch (IndexOutOfBoundsException e){
                    System.out.println("Please enter repairer's ID number");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect repairer's ID number");
                }
                break;

            case "printlist":
                repairerService.showSortedList();
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for Garage type processing
    public static void processGarage(String[] words) {
        switch (words[1]) {
            case "add":
                garageService.add(new GarageSlot());
                System.out.printf("New garage slot ID:%d added succesfully\n" +
                                "Total garage slots: %d\n",
                        garageService.getGarageSlots().get(garageService.getGarageSlots().size() - 1).getId(),
                        garageService.getGarageSlots().size()
                );
                break;

            case "remove":
                try {
                    garageService.remove(Integer.parseInt(words[2]));
                    System.out.printf("Garage slot %d deleted succesfully\n" +
                                    "Total garage slots: %d\n",
                            Integer.parseInt(words[2]),
                            garageService.getGarageSlots().size()
                    );
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please enter garage number");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect garage number");
                }
                break;

            case "printlist":
                garageService.showSorted();
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for OrderList type processing
    public static void processOrderList(String[] words) {
        switch (words[1]) {
            case "add":
                try {
                    orderService.createOrder(Integer.parseInt(words[3]));
                    System.out.println("New order created successfully");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add \"cost amount\"");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect amount, please state a digit");
                } catch (IllegalArgumentException e) {
                        System.out.println("Incorrect amount, should be >= 0");
                }

                break;

            case "printlist":
                try {
                    System.out.println(orderService.getSortedOrders((words[3])));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "get":
                try {
                    System.out.println(orderService.getOrderById(Integer.parseInt(words[3])));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add \"order ID\"");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect order ID, please state actual number");
                }
                break;

            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for Order type processing
    public static void processOrder(String[] words) {
        switch (words[1]) {
            case "create":
                try {
                    orderService.createOrder(Integer.parseInt(words[3]));
                    System.out.println("New order created successfully");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add \"cost amount\"");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect cost, please state a digit");
                }catch (IncorrectCostException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "remove":
                try {
                    orderService.removeOrder(Integer.parseInt(words[3]));
                    System.out.println("Order with ID " + words[3] + " removed successfully");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add \"order ID\"");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect order's ID number");
                } catch (OrderNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "assign":
                try {
                    if (words[4].equals("repairer")) {
                        orderService.assignRepairer(orderService.getOrderById(Integer.parseInt(words[3])), Integer.parseInt(words[5]));
                    } else if (words[4].equals("garage")) {
                        orderService.assignGarageSlot(orderService.getOrderById(Integer.parseInt(words[3])), Integer.parseInt(words[5]));
                    } else {
                        System.out.println("Cannot recognize assignment");
                    }
                } catch (RepairerNotAvailableException | GarageNotAvailableException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect Order ID number or Repairer ID number");
                } catch (NullPointerException e) {
                    System.out.println("Incorrect Order ID");
                }
                break;

            case "complete":
                try {
                    orderService.completeOrder(Integer.parseInt(words[3]));
                    System.out.printf("Order %s completed successfully\n", words[3]);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add \"ID\"");
                } catch (OrderNotFoundException | OrderAlreadyCompletedException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect amount, please state a digit");
                }
                break;

            case "get":
                try {
                    System.out.println(orderService.getOrderById(Integer.parseInt(words[3])));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Please add \"ID\"");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect order's ID number");
                } catch (OrderNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;

            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Typical commands examples
    public static void getHelp(String[] words) {
        if (words.length < 2) {
            System.out.println("Command structure: \"object action arguments\"");
            System.out.println("Objects: Repairer, Garage, Order, OrderList");
            System.out.println("For object commands, print \"help object\", for example: \"help repairer\"\n");
        } else if (words[0].equals("help") && words[1].equals("repairer")) {
            System.out.printf("Object Repairer, actions:  add name, remove id, printlist\n" +
                    "\tExample: Repairer add Piter\n" +
                    "\tExample: Repairer remove 2\n" +
                    "\tExample: Repairer printlist\n");
        } else if (words[0].equals("help") && words[1].equals("garage")) {
            System.out.printf("Object Garage, actions:  add, remove id, printlist\n" +
                    "\tExample: Garage add\n" +
                    "\tExample: Garage remove 2\n" +
                    "\tExample: Garage printlist\n");
        } else if (words[0].equals("help") && words[1].equals("orderlist")) {
            System.out.printf("Object OrderList, actions:  add cost amount, get id number, printlist type number\n" +
                    "\tExample: Orderlist add cost 100\n" +
                    "\tExample: Orderlist get id 2\n" +
                    "\tExample: Orderlist printlist type 2\n");
        } else if (words[0].equals("help") && words[1].equals("order")) {
            System.out.printf("Object Order, actions:  create cost amount, remove id number, assign repairer id number, get id number, printlist type number\n" +
                    "\tExample: Order create cost 100\n" +
                    "\tExample: Order remove id 3\n" +
                    "\tExample: Order complete id 3\n" +
                    "\tExample: Order assign id 4 repairer 2\n" +
                    "\tExample: Order assign id 4 garage 2\n" +
                    "\tExample: Garage printlist sort 2\n");
        } else {
            System.out.println("Unexpected value: "+words[1]);
        }
    }
}