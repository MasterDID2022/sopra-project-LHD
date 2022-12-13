package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Subject;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
/**
 * ClassroomDAO implementing DAO interface for Classroom Object
 */
public class ClassroomDAO implements DAO<Classroom>
{
    private final Connection conn;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    /**
     * Constructor of ClassroomDAO, initiate connection and prepared statement
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */
    private ClassroomDAO() throws SQLException {
        this.conn = Datasource.getInstance().getConnection();

        this.getAll = conn.prepareStatement("SELECT * FROM CLASSROOMS");
        this.get = conn.prepareStatement("SELECT * FROM CLASSROOMS WHERE ID=?");
        this.save = conn.prepareStatement("INSERT INTO CLASSROOMS VALUES (DEFAULT, ?)");
        this.update = conn.prepareStatement("UPDATE CLASSROOMS SET NAME=? WHERE ID=?");
        this.delete = conn.prepareStatement("DELETE FROM CLASSROOMS WHERE ID=?");
    }

    /**
     * Factory for ClassroomDAO
     * @return an instance of ClassroomDAO
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */
    public static ClassroomDAO getInstance() throws SQLException { return new ClassroomDAO(); }

    /**
     * Getter for one Classroom
     * @param id numerical long identifier for getting the Classroom
     * @return May return one Classroom instance
     */
    @Override
    public Optional<Classroom> get(long id) {
        Optional<Classroom> result = Optional.empty();

        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();

            if (rs.next()) {
                result = Optional.of(
                        Classroom.getInstance(
                                rs.getLong("ID"),
                                rs.getString("NAME"))
                );
            }

        }catch (SQLException e){
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all Classrooms
     * @return List of all Classrooms
     */
    @Override
    public List<Classroom> getAll() {
        List<Classroom> classroomList = new ArrayList<>();

        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                classroomList.add(
                        Classroom.getInstance(
                                rs.getLong("ID"),
                                rs.getString("NAME"))
                );
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        return classroomList;
    }

    /**
     * Save Classroom to Database
     * @param classroom Classroom object to save
     */
    @Override
    public void save(Classroom classroom) {
        try{
            save.setString(1, classroom.getName());
            save.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Classroom Table
     * @param classroom Classroom instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Classroom classroom, Map<String, String> params) {
        try {
            update.setString(1, classroom.getName());
            update.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Delete Classroom instance from Database
     * @param classroom Classroom object to delete
     */
    @Override
    public void delete(Classroom classroom) {
        try {
            delete.setLong(1, classroom.getId());
            delete.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }
}
