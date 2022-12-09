package fr.univtln.lhd.model.entities.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DatasourceTest
{
    public static void main(String[] args) {
        String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
        String DB_USER = "postgres";
        String DB_PASS = "assAss&n03&N";
        try (Connection conn = Datasource.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.println("fsjkdfh");
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }
}
