package fr.univtln.lhd.model.entities.dao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Datasource {
    private static final HikariDataSource ds;
    static {
        final HikariConfig config = new HikariConfig("src/main/resources/hikari.properties");
        ds = new HikariDataSource(config);
    }
    private Datasource() {
        throw new IllegalStateException("Utility class");
    }
    public static HikariDataSource getInstance() { return ds; }
    public static Connection getConnection() throws SQLException {return ds.getConnection();}
}
