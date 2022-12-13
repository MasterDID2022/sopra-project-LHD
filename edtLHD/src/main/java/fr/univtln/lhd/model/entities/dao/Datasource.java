package fr.univtln.lhd.model.entities.dao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Datasource {
    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("${database.url}");
        config.setUsername("${database.username}");
        config.setPassword("${database.password}");
        ds = new HikariDataSource(config);
    }
    private Datasource(){}
    public static HikariDataSource getInstance() { return ds; }
}
