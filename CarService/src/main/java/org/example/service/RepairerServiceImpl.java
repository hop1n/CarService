package org.example.service;

import com.google.gson.Gson;
import org.example.exception.FileNotFoundException;
import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RepairerServiceImpl implements Service<Repairer> {

    private static int repairersCount;
    private final Collection<Repairer> REPAIRERS = new ArrayList<>();
    private final String SEPARATOR = File.separator;
    private final String PATH =  "src" + SEPARATOR + "repairers-storage.json";
    private final Gson GSON = new Gson();

    public RepairerServiceImpl() {
        repairersCount = 0;
    }

    public Collection<Repairer> getRepairers() {
        return REPAIRERS;
    }

    public Repairer getById(int id) {
        Repairer repairerToReturn;
        try {
            repairerToReturn = REPAIRERS.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new RepairerNotFoundException("there is no repairer with id=" + id);
        }
        return repairerToReturn;
    }

    public void writeToFile(Repairer repairer) {
        String s = GSON.toJson(repairer);
        try (FileWriter writer = new FileWriter(PATH)) {
            writer.append(s);
        } catch (IOException e)  {
            throw new FileNotFoundException("required file dose not exist");
        }
    }

    public void readFromFile(String path){
        System.out.println(GSON.fromJson(path, Repairer.class));
    }

    @Override
    public void add(Repairer repairer) {
        repairersCount++;
        REPAIRERS.add(repairer);
        repairer.setId(repairersCount);
    }

    @Override
    public void remove(int id) {
        boolean removeFlag;
        removeFlag = REPAIRERS.removeIf(repairer -> repairer.getId() == id);
        if (!removeFlag) {
            throw new RepairerNotFoundException("there is no repairer with id=" + id);
        }
    }

    @Override
    public String toString() {
        return REPAIRERS.toString();
    }
}
