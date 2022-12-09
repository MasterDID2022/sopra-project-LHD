package fr.univtln.lhd.model.entities.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DatasourceTest
{
    public static void main(String[] args) {
        try (Connection conn = Datasource.getConnection()) {
            System.out.println("fsjkdfh");
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }
}
