package org.example.repository;

import jakarta.persistence.*;
import org.example.connector.HibernateUtil;
import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;

import java.util.List;

public class RepairerRepository {
    EntityManagerFactory entityManagerFactory = HibernateUtil.getEntityManagerFactory();

    public List<Repairer> getRepairers() {
        EntityManager session = entityManagerFactory.createEntityManager();

        try {
            TypedQuery<Repairer> query = session.createQuery("SELECT r FROM Repairer r", Repairer.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public Repairer add(Repairer repairer) {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            session.persist(repairer);
            transaction.commit();
            return repairer;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException(e);
        } finally {
            session.close();
        }
    }

    public Repairer getById(Long id){
        EntityManager session = entityManagerFactory.createEntityManager();

        try {
            Repairer repairer = session.find(Repairer.class, id);
            if (repairer == null) {
                throw new RepairerNotFoundException("repairer with provided id dose not found");
            }
            return repairer;
        } finally {
            session.close();
        }
    }

    public boolean remove(Long id) {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();

        try {
            transaction.begin();
            Repairer repairer = session.find(Repairer.class, id);
            if (repairer == null) {
                throw new RepairerNotFoundException("repairer with provided id dose not found");
            }
            session.remove(repairer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException(e);
        } finally {
            session.close();
        }
    }
}
