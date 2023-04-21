package org.example.repository;

import org.example.connector.JDBCConfig;
import org.example.exception.GarageNotFoundException;
import org.example.model.GarageSlot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GarageRepository {
    private static final String SQL_FIND_BY_ID = "SELECT id, isAvailable FROM carservice.garages WHERE id = (?);";
    private static final String SQL_SELECT_ALL = "SELECT * FROM carservice.garages";
    private static final String SQL_ADD_GARAGE_SLOT = "INSERT INTO carservice.garages (isAvailable) VALUES ((?));";
    private static final String SQL_REMOVE_GARAGE_SLOT = "DELETE FROM carservice.garages WHERE id = (?);";
    private static final Function<ResultSet, GarageSlot> garageMapper = resultSet -> {
        try {
            Long id = resultSet.getLong("id");
            boolean isAvailable = resultSet.getBoolean("isAvailable");
            return new GarageSlot(id, isAvailable);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    public List<GarageSlot> getGarageSlots() {
        List<GarageSlot> garageSlots = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    garageSlots.add(garageMapper.apply(resultSet));
                }
            }
            return garageSlots;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public GarageSlot getById(Long garageSlotId) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, garageSlotId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return garageMapper.apply(resultSet);
                } else {
                    throw new GarageNotFoundException("Garage with this id doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public GarageSlot addGarageSlot(){
        GarageSlot garageSlot = new GarageSlot();
        try (Connection connection = JDBCConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_ADD_GARAGE_SLOT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBoolean(1, garageSlot.isAvailable());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return getById(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("id cannot be found");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void removeGarageSlot(Long garageSlotId){
        try (Connection connection = JDBCConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_REMOVE_GARAGE_SLOT, Statement.RETURN_GENERATED_KEYS)){
            statement.setLong(1, garageSlotId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
