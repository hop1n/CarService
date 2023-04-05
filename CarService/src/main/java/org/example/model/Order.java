package org.example.model;

import org.example.Service.RepairerService;

import java.time.LocalDateTime;

public class Order {

    private static final long completionTime = 2;
    private GarageSlot garageSlot;
    private Repairer repairer;
    private int cost;
    private boolean inProgress;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;

    public Order(GarageSlot garageSlot, Repairer repairer, int cost, boolean inProgress, LocalDateTime completionDate) {
        this.garageSlot = garageSlot;
        this.repairer = repairer;
        this.cost = cost;
        this.inProgress = inProgress;
        this.creationDate = LocalDateTime.now();
        this.completionDate = completionDate.plusDays(completionTime);
    }

    public GarageSlot getGarageSlot() {
        return garageSlot;
    }

    public void setGarageSlot(GarageSlot garageSlot) {
        this.garageSlot = garageSlot;
    }

    public Repairer getRepairer() {
        return repairer;
    }

    public void setRepairer(Repairer repairer) {
        this.repairer = repairer;
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

    @Override
    public String toString() {
        return "Order{" +
                "garageSlot=" + garageSlot +
                ", repairer=" + repairer +
                ", cost=" + cost +
                ", inProgress=" + inProgress +
                ", creationDate=" + creationDate +
                ", completionDate=" + completionDate +
                '}';
    }
}
