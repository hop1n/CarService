package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class Order {
    private GarageSlot garageSlot;
    private final Collection<Repairer> repairers = new ArrayList<>();
    private int cost;
    private boolean inProgress;
    private LocalDate creationDate;
    private LocalDate completionDate;
    private int id;

    public Order() {
        this.creationDate = LocalDate.now();
        this.inProgress = true;

    }

    public Order(int cost) {
        this.cost = cost;
        this.creationDate = LocalDate.now();
        this.inProgress = true;
    }

    public Order(GarageSlot garageSlot, int cost, boolean inProgress, LocalDate creationDate, LocalDate completionDate, int id) {
        this.garageSlot = garageSlot;
        this.cost = cost;
        this.inProgress = inProgress;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.id = id;
    }

    public void addRepair(Repairer repairer) {
        repairers.add(repairer);
    }

    public GarageSlot getGarageSlot() {
        return garageSlot;
    }

    public void setGarageSlot(GarageSlot garageSlot) {
        this.garageSlot = garageSlot;
    }

    public Collection<Repairer> getRepairers() {
        return repairers;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return '\n' + "Order{" +
                "id=" + id + "," + "garageSlot=" + garageSlot + "," +
                "repairers=" + repairers + "," + "cost=" + cost + "," +
                "inProgress=" + inProgress + "," + "creationDate=" + creationDate + "," +
                "completionDate=" + completionDate + '}';
    }
}