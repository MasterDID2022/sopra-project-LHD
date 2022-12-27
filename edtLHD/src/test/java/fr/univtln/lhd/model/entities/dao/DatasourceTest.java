package fr.univtln.lhd.model.entities.dao;

import fr.univtln.lhd.model.entities.users.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fr.univtln.lhd.model.entities.dao.Datasource.getConnection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
class DatasourceTest {
    Student E1 = Student.of("Dupont", "Martin", "martin.dupont@lhd.org");
    String insertSQLStmt = "insert into users values (DEFAULT,(?),(?),(?),'password') " +
            "on conflict (lower(email)) do update set name = excluded.name ";
    /* The 'on conflict' clause is there to forcibly induce an operation on the row (here a useless update),
       So that the test work whether the test user exists or not.
     */
    String selectSQLStmt = "select id from users where email=(?)";

    @Test
    @Order(1)
    void shouldNotBeDefaultConstructable () {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            Constructor<Datasource> m = Datasource.class.getDeclaredConstructor(); // constructor without args
            m.setAccessible(true);
            m.newInstance();
        });
    }

    @Test
    @Order(2)
    void shouldOpenAndCloseConnection () {
        Assertions.assertDoesNotThrow(() -> getConnection().close());
    }

    @Test
    @Order(3)
    void insertGetTest () {
        long userId = -1;
        long fetchedUserid;
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertSQLStmt, RETURN_GENERATED_KEYS)) {
            stmt.setString(1, E1.getName());
            stmt.setString(2, E1.getFname());
            stmt.setString(3, E1.getEmail());

            int affected = stmt.executeUpdate();
            Assertions.assertEquals(1, affected);
            ResultSet rs = stmt.getGeneratedKeys();
            Assertions.assertTrue(rs.next()); // Do rs.next() only once, and check. If true, something has been found
            userId = rs.getLong(1);

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(selectSQLStmt)
        ) {
            stmt.setString(1, E1.getEmail());
            ResultSet rs = stmt.executeQuery();
            Assertions.assertTrue(rs.next());
            fetchedUserid = rs.getLong(1);
            Assertions.assertEquals(userId, fetchedUserid);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}