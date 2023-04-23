package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.example.connector.HibernateUtil;
import org.example.model.GarageSlot;

import java.util.List;

public class GarageRepository {
    EntityManagerFactory entityManager = HibernateUtil.getEntityManager();

    public GarageSlot addGarageSlot() {
        GarageSlot garageSlot = new GarageSlot();
        EntityManager session = entityManager.createEntityManager();
        session.getTransaction().begin();
        try {
            session.persist(garageSlot);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return garageSlot;
    }

    public void deleteGarageSlotById(Long id) {
        EntityManager session = entityManager.createEntityManager();
        GarageSlot garageSlot = session.find(GarageSlot.class, id);
//        if (garageSlot == null){
//            throw new GarageNotFoundException("Garage with such id not found");
//        }
        try {
            session.getTransaction().begin();
            session.remove(garageSlot);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public GarageSlot getGarageSlotById(Long id) {
        EntityManager session = entityManager.createEntityManager();
        session.getTransaction().begin();
        try {
            GarageSlot garageSlot = session.find(GarageSlot.class, id);
            if (garageSlot == null) {
                throw new EntityNotFoundException("Garage with such id not found");
            } else {
            return garageSlot;
        } }finally {
            session.close();
        }
    }

    public List<GarageSlot> getGarageSlots() {
        EntityManager session = entityManager.createEntityManager();
        return session.createQuery("SELECT e FROM GarageSlot e", GarageSlot.class).getResultList();
    }
}

