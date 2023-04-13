package org.example.service;

import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;

import java.util.ArrayList;
import java.util.Collection;

public class RepairerService implements Service<Repairer> {
    private int repairersCount;
    private Collection<Repairer> repairers = new ArrayList<>();


    public RepairerService() {
        repairersCount = 0;
    }

    public Repairer getById(int id) {
        return repairers.stream().filter(repairer -> repairer.getId() == id).findFirst()
                .orElseThrow(() -> new RepairerNotFoundException("there is no repairer with id: %d".formatted(id)));
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
            throw new RepairerNotFoundException("there is no repairer with id: %d".formatted(id));
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
