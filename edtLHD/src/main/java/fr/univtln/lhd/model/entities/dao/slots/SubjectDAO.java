package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Subject;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j

/*
  SubjectDAO implementing DAO interface for Subject Object
 */
public class SubjectDAO implements DAO<Subject> {

    private final Connection conn;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    /**
     * Constructor of SubjectDao, initiate connection and prepared statement
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */
    public SubjectDAO() throws SQLException {
        this.conn = Datasource.getInstance().getConnection();
        this.get = conn.prepareStatement("SELECT * FROM SUBJECTS WHERE ID=?");
        this.getAll = conn.prepareStatement("SELECT * FROM SUBJECTS",RETURN_GENERATED_KEYS);
        this.save = conn.prepareStatement("INSERT INTO SUBJECTS VALUES (DEFAULT, ?, ?)");
        this.update = conn.prepareStatement("UPDATE SUBJECTS SET NAME=?, HOUR_COUNT_MAX=? WHERE ID=?");
        this.delete = conn.prepareStatement("DELETE FROM SUBJECTS WHERE ID=?");
    }

    /**
     * Factory for SubjectDao
     * @return an instance of SubjectDAO
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */
    public static SubjectDAO getInstance() throws SQLException { return new SubjectDAO(); }

    /**
     * Getter for one Subject
     * @param id numerical long identifier for getting the Subject
     * @return May return one Subject instance
     */
    @Override
    public Optional<Subject> get(long id) {
        Optional<Subject> result = Optional.empty();
        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();

            if (rs.next()) {
                result = Optional.of(
                        Subject.getInstance(
                        rs.getLong("ID"),
                        rs.getString("NAME"),
                        rs.getFloat("HOUR_COUNT_MAX"))
                );
            }
        }catch (SQLException e){
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all Subjects
     * @return List of all Subjects
     */
    @Override
    public List<Subject> getAll() {
        List<Subject> subjectList = new ArrayList<>();
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Subject s = Subject.getInstance(
                        rs.getLong("ID"),
                        rs.getString("NAME"),
                        rs.getFloat("HOUR_COUNT_MAX"));
                subjectList.add(s);
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        return subjectList;
    }

    /**
     * Save Subject to Database
     * @param subject Subject object to save
     */
    @Override
    public void save(Subject subject) {
        try{
            save.setString(1, subject.getName());
            save.setFloat(2, subject.getHourCountMax());
            save.executeUpdate();
            ResultSet id_set = save.getGeneratedKeys();
            id_set.next();
            subject.setId(id_set.getLong(1));
        } catch (SQLException|IdException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Subject Table
     * @param subject Subject instance to update
     */

    @Override
    public Subject update(Subject subject) throws IdException {
        try {
            update.setString(1,subject.getName());
            update.setFloat(2,subject.getHourCountMax());
            update.setLong(3,subject.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return subject;
    }

    /**
     * Delete Subject instance from Database
     * @param subject Subject object to delete
     */
    @Override
    public void delete(Subject subject) {
        try {
            delete.setLong(1, subject.getId());
            delete.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }
}
