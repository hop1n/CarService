package org.example.model;

import org.example.Service.RepairerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private GarageSlot garageSlot;
    private List<Repairer> repairers = new ArrayList<>();
    private int cost;
    private boolean inProgress;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    private static int orderCount;
    private final int id;

    public Order(int cost, GarageSlot garageSlot) {
        this.garageSlot = garageSlot;
        garageSlot.setAvailable(false);
        this.cost = cost;
        this.creationDate = LocalDateTime.now();
        this.inProgress = true;
        orderCount++;
        id = orderCount;
    }

    public void addRepair(Repairer repairer){
        repairers.add(repairer);
    }

    public GarageSlot getGarageSlot() {
        return garageSlot;
    }

    public void setGarageSlot(GarageSlot garageSlot) {
        this.garageSlot = garageSlot;
    }

    public List<Repairer> getRepairers() {
        return repairers;
    }

    public void setRepairers(List<Repairer> repairers) {
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

    @Override
    public String toString() {

        return "Order{" +
                "id=" + id +
                ", garageSlot=" + garageSlot +
                ", repairer=" + repairers +
                ", cost=" + cost +
                ", inProgress=" + inProgress +
                ", creationDate=" + creationDate +
                ", completionDate=" + completionDate +
                '}';
    }
}
