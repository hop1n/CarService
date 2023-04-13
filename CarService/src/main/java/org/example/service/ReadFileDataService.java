package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.GarageServiceDto;
import org.example.dto.OrderServiceDto;
import org.example.dto.RepairerServiceDto;
import org.example.exception.FileNotFoundException;
import org.example.exception.JsonParsingException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class ReadFileDataService {
    RepairerServiceImpl repairerService;
    GarageService garageService;
    OrderService orderService;

    private final String REPAIRERS_PATH = "src" + File.separator + "repairers-storage.json";
    private final String ORDERS_PATH = "src" + File.separator + "orders-storage.json";
    private final String GARAGES_PATH = "src" + File.separator + "garages-storage.json";

    private final ObjectMapper objectMapper;


    public ReadFileDataService(RepairerServiceImpl repairerService,
                               GarageService garageService, OrderService orderService) {
        this.repairerService = repairerService;
        this.garageService = garageService;
        this.orderService = orderService;

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void readFromFile() {
        GarageServiceDto garageServiceDto = new GarageServiceDto();
        RepairerServiceDto repairerServiceDto = new RepairerServiceDto();
        OrderServiceDto orderServiceDto = new OrderServiceDto();
       try {
           if (new File(REPAIRERS_PATH).length() != 0) {
               repairerServiceDto = objectMapper.readValue(Paths.get(REPAIRERS_PATH).toFile(), RepairerServiceDto.class);
           }
           if (new File(GARAGES_PATH).length() != 0) {
               garageServiceDto = objectMapper.readValue(Paths.get(GARAGES_PATH).toFile(), GarageServiceDto.class);
           }
           if (new File(ORDERS_PATH).length() != 0) {
               orderServiceDto = objectMapper.readValue(Paths.get(ORDERS_PATH).toFile(), OrderServiceDto.class);
           }
       } catch (IOException ex){
           throw new JsonParsingException(ex.getMessage(), ex);
       }
        garageServiceDto.toService(garageService);
        repairerServiceDto.toService(repairerService);
        orderServiceDto.toService(orderService);
    }

    public void writeToFile() {
        String repairerServiceJson = parseToJson(RepairerServiceDto.fromService(repairerService));
        String garageServiceJson = parseToJson(GarageServiceDto.fromService(garageService));
        String orderServiceJson = parseToJson(OrderServiceDto.fromService(orderService));

        writeJsonStringToFile(repairerServiceJson, REPAIRERS_PATH);
        writeJsonStringToFile(garageServiceJson, GARAGES_PATH);
        writeJsonStringToFile(orderServiceJson, ORDERS_PATH);
    }

    private <T> String parseToJson (T object) {
        final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        try {
            return objectWriter.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new JsonParsingException(
                    MessageFormat.format("Parsing from {0} to Json failed", object.getClass().getName()),
                    ex);
        }
    }

    private void writeJsonStringToFile(String jsonString, String path) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            throw new FileNotFoundException("required file dose not exist");
        }
    }
}
