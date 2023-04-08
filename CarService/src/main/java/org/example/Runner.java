package org.example;

import org.example.Service.ConsoleProcessor;

public class Runner {
    static ConsoleProcessor consoleProcessor = new ConsoleProcessor();

    public static void main(String[] args) {

        consoleProcessor.initLogs();
        consoleProcessor.processConsole();
    }
}
