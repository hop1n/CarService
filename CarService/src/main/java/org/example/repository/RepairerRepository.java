package org.example.repository;

import org.example.connector.JDBCConfig;
import org.example.exception.RepairerNotFoundException;
import org.example.model.Repairer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RepairerRepository {

    private static final String SQL_SELECT_ALL = "SELECT * FROM repairers";
    private static final String SQL_ADD = "INSERT INTO carservice.repairers(name, isAvailable) values (?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT id, name, isAvailable from repairers where id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM repairers WHERE id = ?";

    private static final Function<ResultSet, Repairer> repairerMapper = resultSet -> {
        try {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            boolean isAvailable= resultSet.getBoolean("isAvailable");
            return new Repairer(id, name, isAvailable);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };


    public List<Repairer> getRepairers() {
        List<Repairer> repairers = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    repairers.add(repairerMapper.apply(resultSet));
                }
            }
            return repairers;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Repairer add(Repairer repairer) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ADD,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, repairer.getName());
            statement.setBoolean(2, repairer.getIsAvailable());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return getById(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("id cannot be found");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Repairer getById(Long id){
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return repairerMapper.apply(resultSet);
                } else {
                    throw new RepairerNotFoundException("repairer with provided id dose not found");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public boolean remove(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't delete book");
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return true;
    }
}
