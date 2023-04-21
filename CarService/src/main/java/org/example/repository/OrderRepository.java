package org.example.repository;

import org.example.connector.JDBCConfig;
import org.example.exception.GarageNotAvailableException;
import org.example.exception.OrderAlreadyCompletedException;
import org.example.exception.OrderNotFoundException;
import org.example.exception.RepairerNotAvailableException;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;
import org.example.service.OrderService.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class OrderRepository {

    private final GarageRepository garageRepository;
    private final RepairerRepository repairerRepository;

    public OrderRepository(GarageRepository garageRepository, RepairerRepository repairerRepository) {
        this.garageRepository = garageRepository;
        this.repairerRepository = repairerRepository;
    }

    Connection connection;

    {
        try {
            connection = JDBCConfig.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String ORDER_SQL_FIND_BY_ID = "SELECT *  FROM orders WHERE id = ?";
    private static final String ORDER_SQL_SELECT_ALL = "SELECT * FROM orders";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_COST = "SELECT * FROM orders ORDER BY cost DESC";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_CREATION_DATE = "SELECT * FROM orders ORDER BY creation_date DESC";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_COMPLETION_DATE = "SELECT * FROM orders ORDER BY completion_date DESC";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_PROGRESS = "SELECT * FROM orders WHERE inProgress = true";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_FINISHED = "SELECT * FROM orders WHERE inProgress = false";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_REPAIRER = "SELECT * FROM orders WHERE inProgress = false";
    private static final String ORDER_SQL_ADD = "INSERT INTO orders (cost, inProgress, garages_id, creation_date, completion_date) values (?,?,?,?,?)";
    private static final String ORDER_SQL_COMPLETE = "INSERT INTO orders (inProgress, garages_id, completion_date) values (?,?,?)";
    private static final String ORDER_SQL_DELETE_BY_ID = "DELETE FROM orders WHERE id = ?";
    private static final String ORDER_SQL_ASSIGN_GARAGESLOT = "UPDATE orders SET garages_id=? WHERE id=?";
    private static final String ORDER_SQL_SET_GARAGESLOT_ISAVAILABLE = "UPDATE garages SET isAvailable=? WHERE id=?";


    private final Function<ResultSet, Order> orderMapper = resultSet -> {
        LocalDate completionDate;
        GarageSlot garageSlot;
        try {
            Long id = resultSet.getLong("id");
            int cost = resultSet.getInt("cost");
            boolean inProgress = resultSet.getBoolean("inProgress");

            if(resultSet.getObject("garages_id") == null){
                garageSlot = null;
            } else {
                long garageSlotId = resultSet.getLong("garages_id");
                garageSlot = garageRepository.getById(garageSlotId);
            }

            LocalDate creationDate = resultSet.getDate("creation_date").toLocalDate();

            if (resultSet.getDate("completion_date") != null) {
                completionDate = resultSet.getDate("completion_date").toLocalDate();
            } else {
                completionDate = null;
            }

            return new Order(id, cost, inProgress, garageSlot, creationDate, completionDate);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(orderMapper.apply(resultSet));
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean addOrder(Order order) {
        try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_ADD, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getCost());
            statement.setBoolean(2, order.isInProgress());

            if (order.getGarageSlot() != null) {
                statement.setLong(3, order.getGarageSlot().getId());
            } else {
                statement.setObject(3, null);
            }

            statement.setDate(4, Date.valueOf(order.getCreationDate()));

            if (order.getCompletionDate() != null) {
                statement.setDate(5, Date.valueOf(order.getCompletionDate()));
            } else {
                statement.setObject(5, null);
            }
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return true;
    }

    public Order getOrderById(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return orderMapper.apply(resultSet);
                } else {
                    throw new OrderNotFoundException("There is no order with ID%d".formatted(id));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean removeOrder(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_DELETE_BY_ID)) {
            statement.setLong(1, id);
            int rowResult = statement.executeUpdate();

            if (rowResult != 1) {
                throw new SQLException("Can't delete order");
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return true;
    }

    public List<Order> getSortedOrders(String field) {
        String sortSQL = "";
        ArrayList<Order> sortedOrders = new ArrayList<>();
        switch (Fields.valueOf(field.toUpperCase())) {
            case COST -> sortSQL = ORDER_SQL_SELECT_ALL_SORT_BY_COST;
            case CREATION -> sortSQL = ORDER_SQL_SELECT_ALL_SORT_BY_CREATION_DATE;
            case COMPLETION -> sortSQL = ORDER_SQL_SELECT_ALL_SORT_BY_COMPLETION_DATE;
            case PROGRESS -> sortSQL = ORDER_SQL_SELECT_ALL_SORT_BY_PROGRESS;
            case FINISHED -> sortSQL = ORDER_SQL_SELECT_ALL_SORT_BY_FINISHED;
            // case REPAIRER -> sqlSort =;
        }
        try (PreparedStatement statement = connection.prepareStatement(sortSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sortedOrders.add(orderMapper.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return sortedOrders;
    }

    public boolean assignRepairer(Order order, List<Repairer> repairers) {
        for (Repairer repairer : repairers) {

            try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_ASSIGN_REPAIRER, Statement.RETURN_GENERATED_KEYS)) {
                connection.setAutoCommit(false);
                statement.setLong(1, repairer.getId());
                statement.setLong(2, order.getId());

                if (repairer.checkIsAvailable()) {
                    order.addRepair(repairer);
                    repairer.setIsAvailable(false);
                }

                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                try {
                    System.out.println("rollback");
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return true;
    }

    public Order assignGarageSlot(Order order, Long garageId) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ORDER_SQL_ASSIGN_GARAGE_SLOT);
             PreparedStatement statement1 = connection.prepareStatement(ORDER_SQL_SET_GARAGE_SLOT_IS_AVAILABLE)) {

            statement.setLong(1, garageId);
            statement.setLong(2, order.getId());
            statement.executeUpdate();

            if (!order.isInProgress()) {
                throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(order.getId()));
            }
            GarageSlot garageSlot = garageRepository.getById(garageId);
            if (garageSlot.isAvailable()) {
                order.setGarageSlot(garageSlot);
                garageSlot.setAvailable(false);
                statement1.setBoolean(1, false);
                statement1.setLong(2, garageId);
                statement1.executeUpdate();
            } else throw new GarageNotAvailableException("Garage with ID%d is unavailable".formatted(garageId));

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return getOrderById(order.getId());
    }
}

