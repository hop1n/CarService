package org.example.servlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.service.GarageService;
import org.example.service.OrderService;
import org.example.service.ReadFileDataService;
import org.example.service.RepairerService;

public class HttpInterface {
    private Server server;

    RepairerService repairerService = new RepairerService();
    GarageService garageService = new GarageService("./src/main/resources/application.properties");
    OrderService orderService = new OrderService(repairerService, garageService);
    ReadFileDataService readFileDataService = new ReadFileDataService(repairerService, garageService, orderService);

    public void start() {
        garageService.initializePropertyFromFile();
        readFileDataService.readFromFile();
        configure();
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Server hasn't started");
            throw new RuntimeException(e);
        }
    }

    private void configure() {
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);

        ServletHandler servletHandler = new ServletHandler();
        addServlets(servletHandler);

        server.setHandler(servletHandler);
        server.setConnectors(new Connector[]{connector});
    }

    private void addServlets(ServletHandler servletHandler) {
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetRepairersServlet(repairerService)),
                        "/get-repairers");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetOrderByIdServlet(orderService)),
                        "/orders/*");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new CreateOrderServlet(orderService)),
                        "/create-order");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new RemoveOrderServlet(orderService)),
                        "/remove-order");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetOrdersServlet(orderService)),
                        "/get-orders");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetSortedOrdersServlet(orderService)),
                        "/get-sorted-orders");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new AssignRepairerOrderServlet(orderService)),
                        "/orders/assign/repairer");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new AssignGarageOrderServlet(orderService)),
                        "/orders/assign/garage");
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new CompleteOrderServlet(orderService)),
                        "/orders/complete");

    }
}