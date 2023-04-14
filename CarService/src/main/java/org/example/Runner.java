package org.example;

import org.apache.log4j.BasicConfigurator;
import org.example.service.ConsoleProcessor;

public class Runner {
    static ConsoleProcessor consoleProcessor = new ConsoleProcessor();

    public static void main(String[] args) {

        BasicConfigurator.configure();
        consoleProcessor.initLogs();
        consoleProcessor.processConsole();
    }
}
