package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.dao.users.StudentDAO;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.users.Student;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
/*
 * GroupDAO implementing DAO interface for Group Object
 */
public class GroupDAO implements DAO<Group> {

    private static final String GET_STMT = "SELECT * FROM GROUPS WHERE ID=?";
    private static final String GET_ALL_STMT = "SELECT * FROM GROUPS";
    private static final String GET_STUDENT_GROUP_STMT = "SELECT id_user from group_user where id_group= ?";
    private static final String GET_SLOT_GROUP_STMT = "SELECT id_group from group_slot where id_slot= ?";
    private static final String SAVE_STMT = "INSERT INTO GROUPS VALUES (DEFAULT, ?)";
    private static final String UPDATE_STMT = "UPDATE GROUPS SET NAME=? WHERE ID=?";
    private static final String DELETE_STMT = "DELETE FROM GROUPS WHERE ID=?";

    /**
     * Constructor of GroupDAO
     */
    private GroupDAO() { }

    /**
     * Factory for GroupDAO
     * @return an instance of GroupDAO
     */
    public static GroupDAO getInstance() { return new GroupDAO(); }

    /**
     * Getter for one Group
     * @param id numerical long identifier for getting the Group
     * @return May return one Group instance
     */
    @Override
    public Optional<Group> get(long id) throws SQLException {
        Optional<Group> result = Optional.empty();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STMT)
        ){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Group.getInstance(rs.getString("NAME"))
                );
                result.get().setId( rs.getLong("ID") );
                //result.get().setStudents( getStudentsOfGroup(result.get()) );
            }
        }catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return result;
    }

    /**
     * Getter for all Groups
     * @return List of all Groups
     */
    @Override
    public List<Group> getAll() throws SQLException {
        List<Group> groupList = new ArrayList<>();

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_STMT)
        ){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Group group = Group.getInstance(rs.getString("NAME"));
                group.setId( rs.getLong("ID") );
                group.setStudents(getStudentsOfGroup(group));
                groupList.add(group);
            }
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return groupList;
    }
    public  List<Student> getStudentsOfGroup ( final Group group) throws SQLException {
        List<Student> studentList= new ArrayList<>();

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STUDENT_GROUP_STMT)
        ){
            stmt.setLong(1,group.getId());
            stmt.executeQuery();
            ResultSet rs =stmt.getResultSet();
            while (rs.next()) {
                studentList.add(
                        StudentDAO.getInstance().get(rs.getLong(1)).orElseThrow(SQLException::new)
                );
            }
        }
        catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }

        return Collections.unmodifiableList(studentList);
    }
    public List<Group> getGroupOfSlot(final long slotID) throws SQLException {
        List<Group> groupList = new ArrayList<>();

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_SLOT_GROUP_STMT)
        ){
           stmt.setLong(1,slotID);
           ResultSet rs = stmt.executeQuery();
           while (rs.next()){
               groupList.add(
                       get(rs.getLong(1)).orElseThrow(SQLException::new)
               );
           }
       }
       catch (SQLException e) {
           log.error(e.getMessage());
           throw e;
       }

       return Collections.unmodifiableList(groupList);
    }
    /**
     * Save Group to Database
     * @param group Group object to save
     */
    @Override
    public void save(Group group) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_STMT, RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, group.getName());
            stmt.executeUpdate();
            ResultSet idSet = stmt.getGeneratedKeys();
            idSet.next();
            group.setId(idSet.getLong(1));
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Update Data of Group Table
     * @param group Group instance to update
     */
    @Override
    public Group update(Group group) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STMT)
        ){
            stmt.setString(1,group.getName());
            stmt.setLong(2,group.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return group;
    }

    /**
     * Delete Group instance from Database
     * @param group Group object to delete
     */
    @Override
    public void delete(Group group) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STMT)
        ){
            stmt.setLong(1, group.getId());
            stmt.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
