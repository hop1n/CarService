package org.example;

import org.example.servlet.HttpInterface;

public class Runner {

    public static void main(String[] args){
        HttpInterface httpInterface = new HttpInterface();
        httpInterface.start();
    }
}
