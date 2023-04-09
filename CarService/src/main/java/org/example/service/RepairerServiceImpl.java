package org.example.service;

import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.constants.ExceptionMessage.INVALID_REPAIRER_ID;

public class RepairerServiceImpl implements Service<Repairer> {

    private static int repairersCount;
    private final List<Repairer> repairers = new ArrayList<>();

    public List<Repairer> getRepairers() {
        return repairers;
    }

    public Repairer getById(int id) {
        Repairer repairerToReturn;
        try {
            repairerToReturn = repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new RepairerNotFoundException(INVALID_REPAIRER_ID + id);
        }
        return repairerToReturn;
    }

    @Override
    public void add(Repairer repairer) {
        repairersCount++;
        repairers.add(repairer);
        repairer.setId(repairersCount);
    }

    @Override
    public void remove(int id) {
        boolean removeFlag;
        removeFlag = repairers.removeIf(repairer -> repairer.getId() == id);
        if (!removeFlag) {
            throw new RepairerNotFoundException(INVALID_REPAIRER_ID + id);
        }
    }

    @Override
    public String toString() {
        return repairers.toString();
    }
}
