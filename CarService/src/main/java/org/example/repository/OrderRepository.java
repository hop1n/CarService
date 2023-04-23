package org.example.repository;

import org.example.connector.JDBCConfig;
import org.example.exception.*;
import org.example.model.GarageSlot;
import org.example.model.Order;
import org.example.model.Repairer;
import org.example.service.OrderService.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrderRepository {

    private GarageRepository garageRepository;
    private RepairerRepository repairerRepository;

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

    private static final String ORDER_SQL_SELECT_ALL = "SELECT * FROM orders as o left join repairers_in_orders as ro on o.id = ro.orders_id";
    private static final String ORDER_SQL_FIND_BY_ID = ORDER_SQL_SELECT_ALL + " WHERE id = ?";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_COST = ORDER_SQL_SELECT_ALL + " ORDER BY cost DESC";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_CREATION_DATE = ORDER_SQL_SELECT_ALL + " ORDER BY creation_date DESC";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_COMPLETION_DATE = ORDER_SQL_SELECT_ALL + " ORDER BY completion_date DESC";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_PROGRESS = ORDER_SQL_SELECT_ALL + " WHERE inProgress = true";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_FINISHED = ORDER_SQL_SELECT_ALL + " WHERE inProgress = false";
    private static final String ORDER_SQL_SELECT_ALL_SORT_BY_REPAIRER = ORDER_SQL_SELECT_ALL + " ORDER BY repairers_id DESC";
    private static final String ORDER_SQL_ADD = "INSERT INTO orders (cost, inProgress, garages_id, creation_date, completion_date) values (?,?,?,?,?)";
    private static final String ORDER_SQL_DELETE_BY_ID = "DELETE FROM orders WHERE id = ?";
    private static final String ORDER_SQL_ASSIGN_REPAIRERS_IN_ORDERS = "INSERT INTO repairers_in_orders (repairers_id, orders_id) values (?, ?)";
    private static final String ORDER_SQL_SET_GARAGESLOT_ISAVAILABLE = "UPDATE garages SET isAvailable=? WHERE id=?";
    private static final String ORDER_SQL_ASSIGN_GARAGESLOT = "UPDATE orders SET garages_id=? WHERE id=?";
    private static final String ORDER_SQL_SET_REPAIRER_ISAVAILABLE = "UPDATE repairers SET isAvailable=? WHERE id=?";
    private static final String ORDER_SQL_COMPLETE = "UPDATE orders SET inProgress=?, completion_date=? WHERE id=?";


    private final Function<ResultSet, Order> orderMapper = resultSet -> {
        LocalDate completionDate;
        GarageSlot garageSlot;
        try {
            Long id = resultSet.getLong("id");
            int cost = resultSet.getInt("cost");
            boolean inProgress = resultSet.getBoolean("inProgress");

            if (resultSet.getObject("garages_id") == null) {
                garageSlot = null;
            } else {
                long garageSlotId = resultSet.getLong("garages_id");
                garageSlot = garageRepository.getGarageSlotById(garageSlotId);
            }

            LocalDate creationDate = resultSet.getDate("creation_date").toLocalDate();

            if (resultSet.getDate("completion_date") != null) {
                completionDate = resultSet.getDate("completion_date").toLocalDate();
            } else {
                completionDate = null;
            }

            Collection<Repairer> repairers = new ArrayList<>();
            if (findRepairers(resultSet).isPresent()) {
                repairers.add(findRepairers(resultSet).get());
            }

            return new Order(id, cost, inProgress, garageSlot, creationDate, completionDate, repairers);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    public Optional<Repairer> findRepairers(ResultSet resultSet) {
        Repairer repairer;
        try {
            if (resultSet.getString("repairers_id") != null) {
                repairer = repairerRepository.getById(resultSet.getLong("repairers_id"));
                return Optional.ofNullable(repairer);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
            case REPAIRER -> sortSQL = ORDER_SQL_SELECT_ALL_SORT_BY_REPAIRER;
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

    public Order assignRepairer(Order order, Long... ids) {
        for (Long repairerId : ids) {
            try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_ASSIGN_REPAIRERS_IN_ORDERS)) {
                statement.setLong(1, repairerId);
                statement.setLong(2, order.getId());
                statement.executeUpdate();

                Repairer repairer = repairerRepository.getById(repairerId);
                if (repairer.checkIsAvailable()) {
                    setRepairerIsAvailable(repairer, false);
                } else throw new RepairerNotAvailableException("Repairer with ID%d is unavailable".formatted(repairer.getId()));
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return getOrderById(order.getId());
    }

    public Order assignGarageSlot(Order order, Long garageId) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ORDER_SQL_ASSIGN_GARAGESLOT)) {
            statement.setLong(1, garageId);
            statement.setLong(2, order.getId());
            statement.executeUpdate();

            GarageSlot garageSlot = garageRepository.getGarageSlotById(garageId);
            if (garageSlot.isAvailable()) {
                setGarageIsAvailable(garageSlot, false);
                return getOrderById(order.getId());
            } else throw new GarageNotAvailableException("Garage with ID%d is unavailable".formatted(garageId));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Order completeOrder(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ORDER_SQL_COMPLETE)) {

            Order order = getOrderById(id);
            if (!order.isInProgress()) {
                throw new OrderAlreadyCompletedException("Order with ID%d already completed".formatted(id));
            }
            statement.setBoolean(1, false);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setLong(3, id);
            statement.executeUpdate();

            Collection<Repairer> repairers = order.getRepairers();
            GarageSlot garageSlot = garageRepository.getGarageSlotById(order.getGarageSlot().getId());
            if (!repairers.isEmpty() && garageSlot != null) {
                for (Repairer repairer : repairers) {
                    setRepairerIsAvailable(repairer, true);
                }
                setGarageIsAvailable(garageSlot, true);
            } else throw new RepairerOrGarageIsNotAssignedException("You have to assign repairer" +
                    " and garage slot to complete the order");
            return getOrderById(order.getId());
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setGarageIsAvailable(GarageSlot garageSlot, Boolean isAvailable) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ORDER_SQL_SET_GARAGESLOT_ISAVAILABLE)) {
            statement.setBoolean(1, isAvailable);
            statement.setLong(2, garageSlot.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setRepairerIsAvailable(Repairer repairer, boolean isAvailable) {
        try (PreparedStatement statement = connection.prepareStatement(ORDER_SQL_SET_REPAIRER_ISAVAILABLE)) {
            statement.setBoolean(1, isAvailable);
            statement.setLong(2, repairer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}

