package org.example.service;

import org.example.exception.*;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleProcessor {

    RepairerService repairerService = new RepairerService();
    GarageService garageService = new GarageService("src/main/resources/application.properties");
    OrderService orderService = new OrderService(repairerService, garageService);
    ReadFileDataService readFileDataService = new ReadFileDataService(repairerService, garageService, orderService);

    public void initLogs() {
        garageService.initializePropertyFromFile();
        try {
            readFileDataService.readFromFile();
        } catch (JsonParsingException e) {
            System.err.println(e.getMessage());
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> readFileDataService.writeToFile()));
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

                input = reader.readLine().toLowerCase().trim();

                if (input.equals("exit")) {
                    readFileDataService.writeToFile();
                    System.out.println("Files was successfully overwritten");
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
                            System.out.println("Unexpected value: " + words[0]);
                            break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please specify operation for selected object");
                }
            }
        } catch (JsonParsingException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method for Repairers type processing
    public void processRepairer(String[] words) {
        switch (words[1]) {
            case "add":
                //Checking for empty name
                try {
                    String repairerName = words[2].substring(0, 1).toUpperCase() + words[2].substring(1);
                    //advice for future: create model instances in Services,
                    //send all required info for model creation to Service
                    Repairer repairer = new Repairer(repairerName);
                    repairerService.add(repairer);
                    System.out.printf("New repairer %s added successfully\n", repairerName);
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please add repairer's name");
                }
                break;
            case "remove":
                try {
                    repairerService.remove(Integer.parseInt(words[2]));
                } catch (RepairerNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please enter repairer's ID number");
                }
                break;
            case "printlist":
                System.out.println(repairerService);
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for Garage type processing
    public void processGarage(String[] words) {
        switch (words[1]) {
            case "add":
                try {
                    garageService.add(new GarageSlot());
                    System.out.printf("New garage slot ID:%d added succesfully\n",
                            garageService.getGarageSlots().get(garageService.getGarageSlots().size() - 1).getId()
                    );
                } catch (AssignDeprecatedMethod e){
                    System.err.println(e.getMessage());
                } finally {
                    System.out.printf("Total garage slots: %d\n",
                            garageService.getGarageSlots().size());
                }
                break;
            case "remove":
                try {
                    garageService.remove(Integer.parseInt(words[2]));
                    System.out.printf("Garage slot %d deleted succesfully\n" +
                                    "Total garage slots: %d\n",
                            Integer.parseInt(words[2]),
                            garageService.getGarageSlots().size()
                    );
                } catch (AssignDeprecatedMethod | GarageNotFoundException e){
                    System.err.println(e.getMessage());
                    System.out.printf("Total garage slots: %d\n",
                            garageService.getGarageSlots().size());
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please enter garage number");
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect garage number");
                }
                break;
            case "printlist":
                System.out.println(garageService);
                break;
            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Method for Order type processing
    public void processOrder(String[] words) {
        switch (words[1]) {
            case "create":
                try {
                    orderService.addOrder(new Order(Integer.parseInt(words[3])));
                    System.out.println("New order created successfully");
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please add \"cost amount\"");
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect cost, please state a digit");
                } catch (IncorrectCostException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "remove":
                try {
                    orderService.removeOrder(Long.parseLong(words[3]));
                    System.out.println("Order with ID " + words[3] + " removed successfully");
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please add \"order ID\"");
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect order's ID number");
                } catch (OrderNotFoundException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "assign":
                try {
                    if (words[4].equals("repairer")) {
                        orderService.assignRepairer(orderService.getOrderById(Long.parseLong(words[3])),
                                Integer.parseInt(words[5]));
                        System.out.printf("Repairer %d assigned to Order %d successfully", Integer.parseInt(words[5]),
                                Integer.parseInt(words[3]));
                    } else if (words[4].equals("garage")) {
                        orderService.assignGarageSlot(orderService.getOrderById(Long.parseLong(words[3])),
                                Integer.parseInt(words[5]));
                        System.out.printf("Garage %d assigned to Order %d successfully", Integer.parseInt(words[5]),
                                Integer.parseInt(words[3]));
                    } else {
                        System.out.println("Cannot recognize assignment");
                    }
                } catch (GarageNotAvailableException | RepairerNotAvailableException | GarageNotFoundException |
                         RepairerNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Incorrect command: not enough arguments");
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect Order, Garage or Repairer ID number");
                } catch (NullPointerException e) {
                    System.err.println("Incorrect Order ID");
                }
                break;
            case "complete":
                try {
                    orderService.completeOrder(Long.parseLong(words[3]));
                    System.out.printf("Order %s completed successfully\n", words[3]);
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please add \"ID\"");
                } catch (OrderNotFoundException | OrderAlreadyCompletedException e) {
                    System.err.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect amount, please state a digit");
                }
                break;
            case "get":
                try {
                    System.out.println(orderService.getOrderById(Long.parseLong(words[3])));
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please add \"ID\"");
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect order's ID number");
                } catch (OrderNotFoundException e) {
                    System.err.println(e.getMessage());
                }
                break;

            case "printlist":
                try {
                    System.out.println(orderService.getSortedOrders(words[3]));
                } catch (IncorrectSortTypeException e) {
                    System.err.println(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Please add type CREATION, COMPLETION, COST, FINISHED, PROGRESS, REPAIRER");
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect sort type, please state actual number");
                }
                break;

            default:
                System.out.println("Unexpected value: " + words[1]);
                break;
        }
    }

    //Typical commands examples
    public void getHelp(String[] words) {
        if (words.length < 2) {
            System.out.println("Command structure: \"object action arguments\"");
            System.out.println("Objects: Repairer, Garage, Order");
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
        } else if (words[0].equals("help") && words[1].equals("order")) {
            System.out.printf("Object Order, actions:  create cost amount, remove id number," +
                    " assign repairer id number, get id number, printlist type number\n" +
                    "\tExample: Order create cost 100\n" +
                    "\tExample: Order remove id 3\n" +
                    "\tExample: Order complete id 3\n" +
                    "\tExample: Order assign id 4 repairer 2\n" +
                    "\tExample: Order assign id 4 garage 2\n" +
                    "\tExample: Order get id 4\n" +
                    "\tExample: Order printlist sort 1\n");
        } else {
            System.out.println("Unexpected value: " + words[1]);
        }
    }
}
