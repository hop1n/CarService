package org.example.service;

import org.example.model.Repairer;
import org.example.repository.RepairerRepository;

import java.util.Collection;

public class RepairerService implements Service<Repairer> {
    private final RepairerRepository repository = new RepairerRepository();

    public RepairerService() {
    }

    @Override
    public Repairer getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public Repairer add(Repairer repairer) {
        return repository.add(repairer);
    }

    @Override
    public boolean remove(Long id) {
        return repository.remove(id);
    }

    @Override
    public String toString() {
        return repository.toString();
    }

    public Collection<Repairer> getRepairers() {
        return repository.getRepairers();
    }
}
