package org.example.connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCConfig {

    private static final HikariDataSource dataSource;

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    //Database URL and JDBC Driver
    private static final String URL = "jdbc:mysql://localhost:3306/carservice";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static{
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setDriverClassName(DRIVER);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
