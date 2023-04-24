package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.connector.HibernateUtil;
import org.example.exception.*;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;
import org.example.service.OrderService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class OrderRepository {
    EntityManagerFactory entityManagerFactory = HibernateUtil.getEntityManagerFactory();
    private GarageRepository garageRepository;
    private RepairerRepository repairerRepository;

    public OrderRepository(GarageRepository garageRepository, RepairerRepository repairerRepository) {
        this.garageRepository = garageRepository;
        this.repairerRepository = repairerRepository;
    }

    public List<Order> getOrders() {
        EntityManager session = entityManagerFactory.createEntityManager();
        return session.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    public boolean addOrder(Order order) {
        EntityManager session = entityManagerFactory.createEntityManager();
        session.getTransaction().begin();
        try {
            session.persist(order);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return true;
    }

    public Order getOrderById(Long id) {
        EntityManager session = entityManagerFactory.createEntityManager();
        session.getTransaction().begin();
        try {
            Order order = session.find(Order.class, id);
            if (order == null) {
                throw new OrderNotFoundException("There is no order with ID%d".formatted(id));
            } else {
                return order;
            }
        } finally {
            session.close();
        }
    }

    public boolean removeOrder(Long id) {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();

        try {
            transaction.begin();
            Order order = session.find(Order.class, id);
            if (order == null) {
                throw new RepairerNotFoundException("Order with provided id dose not found");
            }
            session.remove(order);
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

    public List<Order> getSortedOrders(String field) {
        EntityManager session = entityManagerFactory.createEntityManager();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<Order> root = criteria.from(Order.class);
        try {
            OrderService.Fields.valueOf(field.toUpperCase());
            criteria.select(root).orderBy(builder.asc(root.get(field)));
            List<Order> orders = session.createQuery(criteria).getResultList();
            return orders;
        } catch (IllegalArgumentException ex) {
            throw new IncorrectSortTypeException("There is no such sort type as: %s".formatted(field));
        } finally {
            session.close();
        }
    }

    public Order assignRepairer(Order order, Long... ids) {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            for (Long repairerId : ids) {
                Repairer repairer = repairerRepository.getById(repairerId);
                if (repairer.getIsAvailable()) {
                    order.addRepair(repairer);
                    repairer.setIsAvailable(false);
                    session.merge(repairer);
                    session.merge(order);
                } else
                    throw new RepairerNotAvailableException("Repairer with ID%d is unavailable".formatted(repairerId));
            }
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException(e);
        } finally {
            session.close();
        }
    }

    public Order assignGarageSlot(Order order, Long garageId) {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            GarageSlot garageSlot = garageRepository.getGarageSlotById(garageId);
            if (garageSlot.isAvailable()) {
                if (order.getGarageSlot() != null) {
                    order.getGarageSlot().setAvailable(true);
                }
                order.setGarageSlot(garageSlot);
                garageSlot.setAvailable(false);
                session.merge(order);
                session.merge(garageSlot);
                transaction.commit();
                return order;
            } else throw new GarageNotAvailableException("Garage with ID%d is unavailable".formatted(garageId));
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException(e);
        } finally {
            session.close();
        }
    }

    public Order completeOrder(Long id) {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            Order order = getOrderById(id);
            if (order == null) {
                throw new OrderNotFoundException("There is no order with ID%d".formatted(id));
            }
            if (!order.isInProgress()) {
                throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(id));
            }
            Collection<Repairer> repairers = order.getRepairers();
            GarageSlot garageSlot = order.getGarageSlot();
            if (!repairers.isEmpty() && garageSlot != null) {
                for (Repairer repairer : repairers) {
                    repairer.setIsAvailable(true);
                    session.merge(repairer);
                }
                garageSlot.setAvailable(true);
                order.setCompletionDate(LocalDate.now());
                order.setInProgress(false);
                session.merge(order);
                session.merge(garageSlot);
                transaction.commit();
                return getOrderById(id);
            } else throw new RepairerOrGarageIsNotAssignedException("You have to assign repairer and garage slot to complete the order");
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

