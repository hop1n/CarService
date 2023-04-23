package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repairers")
public class Repairer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isAvailable;

    public Repairer(String name) {
        this.name = name;
        isAvailable = true;
    }

    public Repairer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public String getName() {
        return name;
    }

    public boolean checkIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return '\n' +
                "Repairer{" +
                "name='" + name + '\'' + "," +
                "isAvailable=" + isAvailable + "," +
                "id=" + id + "," +
                '}';
    }
}
