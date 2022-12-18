package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.users.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;

@Slf4j
class DatasourceTest {
    Student E1 = Student.of("Dupont", "Martin", "martin.dupont@lhd.org");
    String insertsqlstmt = "insert into users values (DEFAULT,(?),(?),(?),'password') " +
            "on conflict (lower(email)) do update set name = excluded.name ";
    /* The 'on conflict' clause is there to forcibly induce an operation on the row (here a useless update),
       So that the test work whether the test user exists or not.
     */
    String selectsqlstmt = "select id from users where email=(?)";

    @Test
    void insertGetTest() {
        long userId = -1;
        long fetchedUserid;
        try (
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertsqlstmt, Statement.RETURN_GENERATED_KEYS)) {
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
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(selectsqlstmt)
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