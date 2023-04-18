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
                .addServletWithMapping(new ServletHolder(
                                new AddRepairerServlet(repairerService)),
                        "/add-repairer");
        servletHandler
                .addServletWithMapping(new ServletHolder(
                        new RemoveRepairerServlet(repairerService)),
                "/remove-repairer");

    }
}