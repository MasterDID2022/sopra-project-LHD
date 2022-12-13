package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.user.Student;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        conn = initConnection();

        this.getAll = conn.prepareStatement("SELECT * FROM GROUPS");
        this.get = conn.prepareStatement("SELECT * FROM GROUPS WHERE ID=?");

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
        Optional<Group> result = Optional.empty();

        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();

            if (rs.next()) {
                result = Optional.of(
                        Group.getInstance(
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
     * Getter for all Groups
     * @return List of all Groups
     */
    @Override
    public List<Group> getAll() {
        List<Group> groupList = new ArrayList<>();

        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                groupList.add(
                        Group.getInstance(
                                rs.getLong("ID"),
                                rs.getString("NAME"))
                );
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        return groupList;
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
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Group Table
     * @param group Group instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Group group, Map<String, String> params) {
        try {
            update.setString(1, group.getName());
            update.setLong(2, group.getId());
            update.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
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
