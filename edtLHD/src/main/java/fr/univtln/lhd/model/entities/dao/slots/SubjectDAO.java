package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Subject;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
/**
 * SubjectDAO implementing DAO interface for Subject Object
 */
public class SubjectDAO implements DAO<Subject> {

    private final Connection conn;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    private SubjectDAO() throws SQLException {
        this.conn = initConnection();

        this.get = conn.prepareStatement("SELECT * FROM SUBJECT WHERE ID=?");
        this.getAll = conn.prepareStatement("SELECT * FROM SUBJECT");
        this.save = conn.prepareStatement("INSERT INTO SUBJECT VALUES (DEFAULT, ?, ?)");
        this.update = conn.prepareStatement("UPDATE SUBJECT SET NAME=?, HOUR_COUNT_MAX=? WHERE ID=?");
        this.delete = conn.prepareStatement("DELETE FROM SUBJECT WHERE ID=?");
    }

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
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Subject Table
     * @param subject Subject instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Subject subject, Map<String, String> params) {

        try {
            update.setString(1, subject.getName());
            update.setFloat(2, subject.getHourCountMax());
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Delete Subject instance from Database
     * @param subject Subject object to delete
     */
    @Override
    public void delete(Subject subject) {

        try {
            delete.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }
}
