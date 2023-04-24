package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "garages_id", referencedColumnName = "id")
    private GarageSlot garageSlot;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "repairers_in_orders",
            joinColumns = @JoinColumn(name = "repairers_id"),
            inverseJoinColumns = @JoinColumn(name = "orders_id"))
    private Collection<Repairer> repairers = new ArrayList<>();
    private int cost;
    private boolean inProgress;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column(name = "completion_date")
    private LocalDate completionDate;


    public Order() {
        this.creationDate = LocalDate.now();
        this.inProgress = true;

    }

    public Order(int cost) {
        this.cost = cost;
        this.creationDate = LocalDate.now();
        this.inProgress = true;
    }

    public Order(Long id, int cost, boolean inProgress, GarageSlot garageSlot,
                 LocalDate creationDate, LocalDate completionDate, Collection<Repairer> repairers) {
        this.id = id;
        this.cost = cost;
        this.inProgress = inProgress;
        this.garageSlot = garageSlot;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.repairers = repairers;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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