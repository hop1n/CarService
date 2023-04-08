package org.example.service;

import org.example.model.Repairer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepairerServiceImpl implements Service<Repairer> {

    private final List<Repairer> repairers = new ArrayList<>();

    @Override
    public void add(Repairer repairer) {
        repairers.add(repairer);
    }

    public boolean isAvailableById(int id) {
        boolean isAvailableToReturn;
        try {
            isAvailableToReturn = repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0).isAvailable();
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("there is no repairer with id=" + id);
        }
        return isAvailableToReturn;
    }

    public Repairer getById(int id) {
        Repairer repairerToReturn;
        try {
            repairerToReturn = repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("there is no repairer with id=" + id);
        }
        return repairerToReturn;
    }

    @Override
    public void remove(int id) {
        try {
            repairers.removeIf(repairer -> repairer.getId() == id);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("there is no repairer with id=" + id);
        }
    }

    @Override
    public String toString() {
        return repairers.toString();
    }
}
