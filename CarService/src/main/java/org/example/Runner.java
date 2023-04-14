package org.example;

import org.example.service.ConsoleProcessor;

public class Runner {

    public static void main(String[] args) {
        ConsoleProcessor consoleProcessor = new ConsoleProcessor();
        consoleProcessor.initLogs();
        consoleProcessor.processConsole();
    }
}
