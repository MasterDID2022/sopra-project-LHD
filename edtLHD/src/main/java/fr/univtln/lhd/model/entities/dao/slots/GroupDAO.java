package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
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
/**
 * GroupDAO implementing DAO interface for Group Object
 */
public class GroupDAO implements DAO<Group> {

    private final Connection conn;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    /**
     * Constructor of GroupDAO, initiate connection and prepared statement
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */
    private GroupDAO() throws SQLException {
        conn = Datasource.getInstance().getConnection();
        this.getAll = conn.prepareStatement("SELECT * FROM GROUPS");
        this.get = conn.prepareStatement("SELECT * FROM GROUPS WHERE ID=?",RETURN_GENERATED_KEYS);
        this.save = conn.prepareStatement("INSERT INTO GROUPS VALUES (DEFAULT, ?)");
        this.update = conn.prepareStatement("UPDATE GROUPS SET NAME=? WHERE ID=?");
        this.delete = conn.prepareStatement("DELETE FROM GROUPS WHERE ID=?");
    }

    /**
     * Factory for GroupDAO
     * @return an instance of GroupDAO
     * @throws SQLException throw a SQLException if there is a problem with the connection or database prepared statement
     */
    public static GroupDAO getInstance() throws SQLException { return new GroupDAO(); }

    /**
     * Getter for one Group
     * @param id numerical long identifier for getting the Group
     * @return May return one Group instance
     */
    @Override
    public Optional<Group> get(long id) {
        Group result=null;
        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                result = Group.getInstance(
                                rs.getString("NAME"));
                result.setId(rs.getLong("ID"));
                result.setStudents(getStudentsOfGroup(result));

            }
        }catch (SQLException e){
            log.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    /**
     * Getter for all Groups
     * @return List of all Groups
     */
    @Override
    public List<Group> getAll() {
        List<Group> groupList = new ArrayList<>();
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Group group;
                        group = Group.getInstance(rs.getString("NAME"));
                        group.setStudents(getStudentsOfGroup(group));
                groupList.add(group);
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        return groupList;
    }
    public  List<Student> getStudentsOfGroup ( final Group group) {
        List<Student> studentList= new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id_user from group_user where id_group= ?")){
            stmt.setLong(1,group.getId());
            log.info(group.getId() + "got");
            stmt.executeQuery();
            ResultSet rs =stmt.getResultSet();
            while (rs.next()) {
                studentList.add(StudentDAO.getInstance().get(rs.getLong(1)).orElseThrow(SQLException::new));
            }
        }
        catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(studentList);
    }
    public List<Group> getGroupOfSlot(final long slotID) {
        List<Group> groupList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id_group from group_slot where id_slot= ?")){
           stmt.setLong(1,slotID);
           ResultSet rs = stmt.executeQuery();
           while (rs.next()){
               groupList.add(get(rs.getLong(1)).orElseThrow(SQLException::new));
           }
       }
       catch (SQLException e ) {
           log.error(e.getMessage());
       }
        return Collections.unmodifiableList(groupList);
    }
    /**
     * Save Group to Database
     * @param group Group object to save
     */
    @Override
    public void save(Group group) {
        try{
            save.setString(1, group.getName());
            save.executeUpdate();
            ResultSet idSet = save.getGeneratedKeys();
            idSet.next();
            group.setId(idSet.getLong(1));
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Group Table
     * @param group Group instance to update
     */
    @Override
    public Group update(Group group) throws IdException {
        try {
            update.setString(1,group.getName());
            update.setLong(2,group.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return group;
    }

    /**
     * Delete Group instance from Database
     * @param group Group object to delete
     */
    @Override
    public void delete(Group group) {
        try {
            delete.setLong(1, group.getId());
            delete.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }
}
