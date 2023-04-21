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
//        return repairers.stream().filter(repairer -> Objects.equals(repairer.getId(), id)).findFirst()
//                .orElseThrow(() -> new RepairerNotFoundException("there is no repairer with id: %d".formatted(id)));
    }

    @Override
    public Repairer add(Repairer repairer) {
        return repository.add(repairer);
    }

    @Override
    public boolean remove(Long id) {
        return repository.remove(id);
//        boolean removeFlag = repairers.removeIf(repairer -> repairer.getId() == id);
//        if (!removeFlag) {
//            throw new RepairerNotFoundException("there is no repairer with id: %d".formatted(id));
//        }
//        return true;
    }

    @Override
    public String toString() {
        return repository.toString();
    }

    public Collection<Repairer> getRepairers() {
        return repository.getRepairers();
    }
}
