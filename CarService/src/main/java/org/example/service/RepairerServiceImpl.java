package org.example.service;

import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RepairerServiceImpl implements Service<Repairer> {
    private int repairersCount;
    private Collection<Repairer> repairers = new ArrayList<>();


    public RepairerServiceImpl() {
        repairersCount = 0;
    }

    public Repairer getById(int id) {
        //optimize like in GarageService.getById()

        Repairer repairerToReturn;
        try {
            repairerToReturn = repairers.stream().filter(repairer -> repairer.getId() == id).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            //Use MessageFormat instead of string concatination
            throw new RepairerNotFoundException("there is no repairer with id=" + id);
        }
        return repairerToReturn;
    }

    @Override
    public void add(Repairer repairer) {
        repairers.add(repairer);
        repairersCount++;
        repairer.setId(repairersCount);
    }

    @Override
    public void remove(int id) {
        boolean removeFlag = repairers.removeIf(repairer -> repairer.getId() == id);
        if (!removeFlag) {
            //Use MessageFormat instead of string concatination
            throw new RepairerNotFoundException("there is no repairer with id=" + id);
        }
    }

    @Override
    public String toString() {
        return repairers.toString();
    }

    public Collection<Repairer> getRepairers() {
        return repairers;
    }

    public void setRepairers(Collection<Repairer> repairers) {
        this.repairers = repairers;
    }

    public int getRepairersCount() {
        return repairersCount;
    }

    public void setRepairersCount(int repairersCount) {
        this.repairersCount = repairersCount;
    }
}
