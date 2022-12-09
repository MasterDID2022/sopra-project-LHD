package fr.univtln.lhd.model.entities.dao;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Datasource {
    private final HikariDataSource ds;

    private static String DB_URL = "jdbc:postgresql://localhost:5432/lhd";
    private static String USER = "postgres";
    private static String PASS = "assAss&n03&N";

    private Datasource ( String url, String user, String password ) {
        ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
    }

    public static Connection getConnection(String url, String user, String password) throws SQLException { return new Datasource(url, user, password).ds.getConnection(); }
    public static Connection getConnection(String url) throws SQLException { return getConnection(url, USER, PASS); }

    public static Connection getConnection() throws SQLException { return getConnection(DB_URL, USER, PASS); }
}
