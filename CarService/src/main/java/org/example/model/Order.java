package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Order {
    private GarageSlot garageSlot;
    private Collection<Repairer> repairers = new ArrayList<>();
    private int cost;
    private boolean inProgress;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    private  int id;

    public Order() {
        this.creationDate = LocalDateTime.now();
        this.inProgress = true;
    }

    public Order(int cost) {
        this.cost = cost;
        this.creationDate = LocalDateTime.now();
        this.inProgress = true;
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

    public void setRepairers(Collection<Repairer> repairers) {
        this.repairers = repairers;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
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