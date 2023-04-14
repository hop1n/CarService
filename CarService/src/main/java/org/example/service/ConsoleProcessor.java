package org.example.service;

import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(ConsoleProcessor.class);

    public void initLogs() {
        GarageSlot garageSlot1 = new GarageSlot();
        GarageSlot garageSlot2 = new GarageSlot();
        GarageSlot garageSlot3 = new GarageSlot();
        Repairer repairer1 = new Repairer("Tom");
        Repairer repairer2 = new Repairer("Alex");
        Repairer repairer3 = new Repairer("Jhon");
        Order order1 = new Order(100);
        Order order2 = new Order(100);
        Order order3 = new Order(120);


        repairerService.add(repairer1);
        repairerService.add(repairer3);
        repairerService.add(repairer2);

        garageService.add(garageSlot1);
        garageService.add(garageSlot2);
        garageService.add(garageSlot3);


        orderService.addOrder(order1);
        orderService.assignRepairer(order1, repairer1.getId());
        orderService.assignGarageSlot(order1, garageSlot1.getId());

        orderService.addOrder(order2);
        orderService.assignRepairer(order3, repairer3.getId());
        orderService.assignGarageSlot(order3, garageSlot3.getId());

//        Order order2 = orderService.createOrder(100,garageSlot1.getId());
        orderService.addOrder(order3);
        orderService.assignRepairer(order2, repairer2.getId());
        orderService.assignGarageSlot(order2, garageSlot2.getId());
        orderService.completeOrder(order2.getId());


        logger.info(orderService.getOrders());
    }

    //Method for input processing
    public void processConsole() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String input;
            while (true) {
                logger.info("=====");
                logger.info("Please enter command or type \"help\" for command list. Enter \"exit\" to exit:");

                input = reader.readLine().toLowerCase().trim();

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
                        case "order":
                            processOrder(words);
                            break;
                        case "help":
                            getHelp(words);
                            break;
                        default:
                            logger.warn("Unexpected value: " + words[0]);
                            break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please specify operation for selected object");
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
                    logger.debug("New repairer " + repairerName + " added successfully\n");
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please add repairer's name");
                }
                break;
            case "remove":
                try {
                    repairerService.remove(Integer.parseInt(words[2]));
                } catch (RepairerNotFoundException e) {
                    logger.error(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please enter repairer's ID number");
                }
                break;
            case "printlist":
                logger.info(repairerService);
                break;
            default:
                logger.warn("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for Garage type processing
    public static void processGarage(String[] words) {
        switch (words[1]) {
            case "add":
                garageService.add(new GarageSlot());
                logger.debug("New garage slot ID:" + garageService.getGarageSlots().get(garageService.getGarageSlots().size() - 1).getId() + " added successfully\n" +
                        "Total garage slots: " + garageService.getGarageSlots().size()
                );
                break;
            case "remove":
                try {
                    garageService.remove(Integer.parseInt(words[2]));
                    logger.debug("Garage slot " + Integer.parseInt(words[2]) + " deleted successfully\n" +
                            "Total garage slots: " + garageService.getGarageSlots().size()
                    );
                } catch (GarageNotFoundException e) {
                    logger.error(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please enter garage number");
                } catch (NumberFormatException e) {
                    logger.error("Incorrect garage number");
                }
                break;
            case "printlist":
                logger.info(garageService);
                break;
            default:
                logger.warn("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for Order type processing
    public static void processOrder(String[] words) {
        switch (words[1]) {
            case "create":
                try {
                    orderService.addOrder(new Order(Integer.parseInt(words[3])));
                    logger.debug("New order created successfully");
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please add \"cost amount\"");
                } catch (NumberFormatException e) {
                    logger.error("Incorrect cost, please state a digit");
                } catch (IncorrectCostException e) {
                    logger.error(e.getMessage());
                }
                break;
            case "remove":
                try {
                    orderService.removeOrder(Integer.parseInt(words[3]));
                    logger.debug("Order with ID " + words[3] + " removed successfully");
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please add \"order ID\"");
                } catch (NumberFormatException e) {
                    logger.error("Incorrect order's ID number");
                } catch (OrderNotFoundException e) {
                    logger.error(e.getMessage());
                }
                break;
            case "assign":
                try {
                    if (words[4].equals("repairer")) {
                        orderService.assignRepairer(orderService.getOrderById(Integer.parseInt(words[3])),
                                Integer.parseInt(words[5]));
                        logger.debug("Repairer " + Integer.parseInt(words[5]) + " assigned to Order " + Integer.parseInt(words[3]) + " successfully");
                    } else if (words[4].equals("garage")) {
                        orderService.assignGarageSlot(orderService.getOrderById(Integer.parseInt(words[3])),
                                Integer.parseInt(words[5]));
                        logger.debug("Garage " + Integer.parseInt(words[5]) + " assigned to Order " + Integer.parseInt(words[3]) + " successfully");
                    } else {
                        logger.warn("Cannot recognize assignment");
                    }
                } catch (GarageNotAvailableException | RepairerNotAvailableException | GarageNotFoundException |
                        RepairerNotFoundException e) {
                    logger.error(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Incorrect command: not enough arguments");
                } catch (NumberFormatException e) {
                    logger.error("Incorrect Order, Garage or Repairer ID number");
                } catch (NullPointerException e) {
                    logger.error("Incorrect Order ID");
                }
                break;
            case "complete":
                try {
                    orderService.completeOrder(Integer.parseInt(words[3]));
                    logger.debug("Order " + words[3] + " completed successfully\n");
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please add \"ID\"");
                } catch (OrderNotFoundException | OrderAlreadyCompletedException e) {
                    logger.error(e.getMessage());
                } catch (NumberFormatException e) {
                    logger.error("Incorrect amount, please state a digit");
                }
                break;
            case "get":
                try {
                    logger.info(orderService.getOrderById(Integer.parseInt(words[3])));
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please add \"ID\"");
                } catch (NumberFormatException e) {
                    logger.error("Incorrect order's ID number");
                } catch (OrderNotFoundException e) {
                    logger.error(e.getMessage());
                }
                break;

            case "printlist":
                try {
                    logger.info(orderService.getSortedOrders(words[3]));
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Please add \"type 1-5\"");
                } catch (NumberFormatException e) {
                    logger.error("Incorrect sort type, please state actual number");
                }
                break;

            default:
                logger.warn("Unexpected value: " + words[1]);
                break;
        }
    }

    //Typical commands examples
    public static void getHelp(String[] words) {
        if (words.length < 2) {
            logger.info("Command structure: \"object action arguments\n" +
                    "\tObjects: Repairer, Garage, Order\n" +
                    "\tFor object commands, print \"help object\", for example: \"help repairer\"\n");
        } else if (words[0].equals("help") && words[1].equals("repairer")) {
            logger.info("Object Repairer, actions:  add name, remove id, printlist\n" +
                    "\tExample: Repairer add Piter\n" +
                    "\tExample: Repairer remove 2\n" +
                    "\tExample: Repairer printlist\n");
        } else if (words[0].equals("help") && words[1].equals("garage")) {
            logger.info("Object Garage, actions:  add, remove id, printlist\n" +
                    "\tExample: Garage add\n" +
                    "\tExample: Garage remove 2\n" +
                    "\tExample: Garage printlist\n");
        } else if (words[0].equals("help") && words[1].equals("order")) {
            logger.info("Object Order, actions:  create cost amount, remove id number," +
                    " assign repairer id number, get id number, printlist type number\n" +
                    "\tExample: Order create cost 100\n" +
                    "\tExample: Order remove id 3\n" +
                    "\tExample: Order complete id 3\n" +
                    "\tExample: Order assign id 4 repairer 2\n" +
                    "\tExample: Order assign id 4 garage 2\n" +
                    "\tExample: Order get id 4\n" +
                    "\tExample: Order printlist sort 1\n");
        } else {
            logger.warn("Unexpected value: " + words[1]);
        }
    }
}
