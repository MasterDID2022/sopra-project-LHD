package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Group;
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
    private static final String GET_SLOT_GROUP_STMT = "SELECT id_group from group_slot where id_slot= ?";
    private static final String GET_ALL_GROUP_SLOT_STMT = "SELECT * from group_slot";
    private static final String SAVE_STMT = "INSERT INTO GROUPS VALUES (DEFAULT, ?)";
    private static final String SAVE_SLOT_STMT = "INSERT INTO GROUP_SLOT VALUES (?, ?)";
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
            }
        }catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        } catch (IdException e){
            log.error(e.getMessage());
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
                groupList.add(group);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IdException e) {
            log.error(e.getMessage());
        }
        return groupList;
    }


    public Set<Group> getGroupOfSlot(final long slotID) throws SQLException {
        Set<Group> groupList = new HashSet<>();

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_SLOT_GROUP_STMT)
        ) {
            stmt.setLong(1, slotID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                groupList.add(
                        get(rs.getLong(1)).orElseThrow(SQLException::new)
                );
           }
       }
       catch (SQLException e) {
           log.error(e.getMessage());
           throw e;
       }

        return Collections.unmodifiableSet(groupList);
    }

    /**
     * Get all group associated to a Slot (via table group_slot)
     * @return List of Group entity
     * @throws SQLException if an error occurs during the query
     */
    public List<Group> getAllGroupSlot() throws SQLException {
        List<Group> groupList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_GROUP_SLOT_STMT)
        ){
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                groupList.add( get( rs.getLong("id_group") ).orElseThrow(SQLException::new) );
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return groupList;
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
        } catch (IdException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Save into join table group_slot, considering both are already saved in their respective tables
     * Should only be used by SlotDAO
     *
     * @param slotId  Slot entity id to save
     * @param groupId Group entity id to save
     * @throws SQLException thrown Exception in case of Errors, especially if one of the two ids doesn't exist in entity tables
     */
    public void save ( final long slotId, final long[] groupId ) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_SLOT_STMT)
        ) {
            batchUpdate(slotId, groupId, conn, stmt);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static void batchUpdate ( long slotId, long[] groupId, Connection conn, PreparedStatement stmt ) throws SQLException {
        conn.setAutoCommit(false); // we need to commit after the execute batch
        for (final long id : groupId) {
            stmt.setLong(1, id);
            stmt.setLong(2, slotId);
            stmt.addBatch();
            stmt.clearParameters();
        }
        stmt.executeBatch();
        conn.commit();
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
