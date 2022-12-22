package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Subject;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j

/*
  SubjectDAO implementing DAO interface for Subject Object
 */
public class SubjectDAO implements DAO<Subject> {

    private static final String GETALL_STMT = "SELECT * FROM SUBJECTS";
    private static final String GET_STMT = "SELECT * FROM SUBJECTS WHERE ID=?";
    private static final String SAVE_STMT = "INSERT INTO SUBJECTS VALUES (DEFAULT, ?, ?)";
    private static final String UPDATE_STMT = "UPDATE SUBJECTS SET NAME=?, HOUR_COUNT_MAX=? WHERE ID=?";
    private static final String DELETE_STMT = "DELETE FROM SUBJECTS WHERE ID=?";

    /**
     * Constructor of SubjectDao, initiate connection and prepared statement
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */


    /**
     * Factory for SubjectDao
     *
     * @return an instance of SubjectDAO
     */
    public static SubjectDAO getInstance () {
        return new SubjectDAO();
    }

    /**
     * Getter for one Subject
     * @param id numerical long identifier for getting the Subject
     * @return May return one Subject instance
     */
    @Override
    public Optional<Subject> get(long id ) {
        Subject result = null;
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STMT)
        ) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = Subject.getInstance(
                        rs.getString("NAME"),
                        rs.getFloat("HOUR_COUNT_MAX"));
                result.setId( rs.getLong("ID") );
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            //throw e;
        } catch (IdException e){
            log.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    /**
     * Getter for all Subjects
     * @return List of all Subjects
     */
    @Override
    public List<Subject> getAll () {
        List<Subject> subjectList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GETALL_STMT);
             ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Subject s = Subject.getInstance(
                        rs.getString("NAME"),
                        rs.getFloat("HOUR_COUNT_MAX"));
                s.setId( rs.getLong("ID") );
                subjectList.add(s);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            //throw e;
        } catch (IdException e){
            log.error(e.getMessage());
        }
        return subjectList;
    }

    /**
     * Save Subject to Database
     * @param subject Subject object to save
     */
    @Override
    public void save(Subject subject ) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_STMT, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, subject.getName());
            stmt.setFloat(2, subject.getHourCountMax());
            stmt.executeUpdate();
            ResultSet idSet = stmt.getGeneratedKeys();
            idSet.next();
            subject.setId(idSet.getLong(1));
        } catch (SQLException e) {
            log.error(e.getMessage());
            //throw e;
        } catch (IdException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Subject Table
     * @param subject Subject instance to update
     */

    @Override
    public Subject update(Subject subject ) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STMT)
        ) {
            stmt.setString(1, subject.getName());
            stmt.setFloat(2, subject.getHourCountMax());
            stmt.setLong(3, subject.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return subject;
    }

    /**
     * Delete Subject instance from Database
     * @param subject Subject object to delete
     */
    @Override
    public void delete(Subject subject ) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STMT)
        ) {
            stmt.setLong(1, subject.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            //throw e;
        }
    }
}
