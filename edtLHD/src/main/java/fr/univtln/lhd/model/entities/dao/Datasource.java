package fr.univtln.lhd.model.entities.dao;

import com.zaxxer.hikari.HikariDataSource;

public class Datasource {
      private final HikariDataSource ds;
    private Datasource ( String url ) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        this.ds = ds;
    }


    public static Datasource createDatasource ( String url ) {
        return new Datasource(url);
    }
}
