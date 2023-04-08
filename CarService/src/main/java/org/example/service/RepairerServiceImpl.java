package org.example.service;

import org.example.exceprions.RepairerNotFoundException;
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

    public Repairer getById(int id) {
        Repairer repairerToReturn;
        try {
            repairerToReturn = repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new RepairerNotFoundException("there is no repairer with id=" + id);
        }
        return repairerToReturn;
    }

    @Override
    public void remove(int id) {
        boolean removeFlag;
        removeFlag = repairers.removeIf(repairer -> repairer.getId() == id);
        if (!removeFlag) {
            throw new RepairerNotFoundException("invalid id");
        }
    }

    @Override
    public String toString() {
        return repairers.toString();
    }
}
