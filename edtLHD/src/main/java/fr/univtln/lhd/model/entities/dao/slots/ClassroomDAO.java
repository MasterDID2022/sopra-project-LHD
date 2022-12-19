package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Classroom;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
        this.conn = Datasource.getConnection();
        this.get = this.conn.prepareStatement("SELECT * FROM CLASSROOMS WHERE ID=?");
        this.getAll = this.conn.prepareStatement("SELECT * FROM CLASSROOMS");
        this.save = this.conn.prepareStatement("INSERT INTO CLASSROOMS VALUES (DEFAULT, ?)",RETURN_GENERATED_KEYS);
        this.update = this.conn.prepareStatement("UPDATE CLASSROOMS SET name=? WHERE ID=?");
        this.delete = this.conn.prepareStatement("DELETE FROM CLASSROOMS WHERE ID=?");
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
                        Classroom.getInstance( rs.getString("NAME") )
                );
                result.get().setId( rs.getLong("ID") );
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
                Classroom classroom = Classroom.getInstance( rs.getString("NAME") );
                classroom.setId( rs.getLong("ID") );
                classroomList.add(classroom);
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
            ResultSet id_set = save.getGeneratedKeys();
            id_set.next();
            classroom.setId(id_set.getLong(1));
        } catch (SQLException  e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Classroom Table
     * @param classroom Classroom instance to update
     */
    @Override
    public Classroom update(Classroom classroom)  {
        try {
            update.setString(1,classroom.getName());
            update.setLong(2,classroom.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return classroom;
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
