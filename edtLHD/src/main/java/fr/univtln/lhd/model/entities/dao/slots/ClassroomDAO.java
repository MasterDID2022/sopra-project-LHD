package fr.univtln.lhd.model.entities.dao.slots;

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
/*
 * ClassroomDAO implementing DAO interface for Classroom Object
 */
public class ClassroomDAO implements DAO<Classroom>
{
    private static final String GET_STMT = "SELECT * FROM CLASSROOMS WHERE ID=?";
    private static final String GET_ALL_STMT = "SELECT * FROM CLASSROOMS";
    private static final String SAVE_STMT = "INSERT INTO CLASSROOMS VALUES (DEFAULT, ?)";
    private static final String UPDATE_STMT = "UPDATE CLASSROOMS SET name=? WHERE ID=?";
    private static final String DELETE_STMT = "DELETE FROM CLASSROOMS WHERE ID=?";

    /**
     * Constructor of ClassroomDAO
     */
    private ClassroomDAO() { }

    /**
     * Factory for ClassroomDAO
     * @return an instance of ClassroomDAO
     */
    public static ClassroomDAO getInstance() { return new ClassroomDAO(); }

    /**
     * Getter for one Classroom
     * @param id numerical long identifier for getting the Classroom
     * @return May return one Classroom instance
     */
    @Override
    public Optional<Classroom> get(long id) throws SQLException {
        Optional<Classroom> result = Optional.empty();

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STMT)
        ){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = Optional.of(
                        Classroom.getInstance( rs.getString("NAME") )
                );
                result.get().setId( rs.getLong("ID") );
            }

        }catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return result;
    }

    /**
     * Getter for all Classrooms
     * @return List of all Classrooms
     */
    @Override
    public List<Classroom> getAll() throws SQLException {
        List<Classroom> classroomList = new ArrayList<>();

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_STMT)
        ){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Classroom classroom = Classroom.getInstance( rs.getString("NAME") );
                classroom.setId( rs.getLong("ID") );
                classroomList.add(classroom);
            }
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return classroomList;
    }

    /**
     * Save Classroom to Database
     * @param classroom Classroom object to save
     */
    @Override
    public void save(Classroom classroom) throws SQLException {
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SAVE_STMT, RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, classroom.getName());
            stmt.executeUpdate();
            ResultSet idSet = stmt.getGeneratedKeys();
            idSet.next();
            classroom.setId(idSet.getLong(1));
        } catch (SQLException  e){
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Update Data of Classroom Table
     * @param classroom Classroom instance to update
     */
    @Override
    public Classroom update(Classroom classroom) throws SQLException {
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_STMT)
        ){
            stmt.setString(1,classroom.getName());
            stmt.setLong(2,classroom.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return classroom;
    }

    /**
     * Delete Classroom instance from Database
     * @param classroom Classroom object to delete
     */
    @Override
    public void delete(Classroom classroom) throws SQLException {
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_STMT)
        ){
            stmt.setLong(1, classroom.getId());
            stmt.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
