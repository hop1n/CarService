package org.example.dto;

import org.example.model.Repairer;
import org.example.service.RepairerService;

import java.util.Collection;

public class RepairerServiceDto {

    private int repairersCount;
    private Collection<Repairer> repairers;

    public RepairerServiceDto(int repairersCount, Collection<Repairer> repairers) {
        this.repairersCount = repairersCount;
        this.repairers = repairers;
    }

    public RepairerServiceDto() {
    }

    public static RepairerServiceDto fromService(RepairerService repairerService){
        return new RepairerServiceDto(repairerService.getRepairersCount(), repairerService.getRepairers());
    }

    public void toService(RepairerService repairerService){
        repairerService.setRepairers(this.repairers);
        repairerService.setRepairersCount(this.repairersCount);
    }

    public int getRepairersCount() {
        return repairersCount;
    }

    public Collection<Repairer> getRepairers() {
        return repairers;
    }

    public void setRepairersCount(int repairersCount) {
        this.repairersCount = repairersCount;
    }

    public void setRepairers(Collection<Repairer> repairers) {
        this.repairers = repairers;
    }
}
