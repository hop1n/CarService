package org.example.service;

import org.example.model.Repairer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RepairerServiceImpl implements Service<Repairer> {

    private final List<Repairer> repairers = new ArrayList<>();

    public RepairerServiceImpl() {
    }

    public List<Repairer> getRepairers() {
        return repairers;
    }


    @Override
    public void add(Repairer repairer) {
        repairers.add(repairer);
    }

    public boolean isAvailableById(int id){
        return repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0).isAvailable();
    }

    public Repairer getById(int id){
        return repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0);
    }

    public void showSortedList(){
        List<Repairer> sortedRepairers = new ArrayList<>(repairers);
        sortedRepairers.sort(Comparator.comparing(Repairer::getName));
        System.out.println(sortedRepairers);
    }

    public void sortById(){
        repairers.sort(Comparator.comparing(Repairer::getId));
    }

    @Override
    public void remove(int id) {
        repairers.removeIf(repairer -> repairer.getId() == id); // потеститть
    }

    @Override
    public void sort() {
        repairers.sort(Comparator.comparing(Repairer::getName));
    }

    @Override
    public String toString() {
        return repairers.toString();
    }
}
