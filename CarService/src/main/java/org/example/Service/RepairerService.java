package org.example.Service;

import org.example.model.Repairer;

import java.util.ArrayList;
import java.util.List;

public class RepairerService implements RepairerDAO{

    private final List<Repairer> repairers = new ArrayList<>();

    public RepairerService() {
    }

    public List<Repairer> getRepairers() {
        return repairers;
    }
}
