package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Group;
import java.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * GroupDAO implementing DAO interface for Group Object
 */
public class GroupDAO implements DAO<Group> {

    /**
     * Getter for one Group
     * @param id numerical long identifier for getting the Group
     * @return May return one Group instance
     */
    @Override
    public Optional<Group> get(long id) {
        //wip
        return Optional.empty();
    }

    /**
     * Getter for all Groups
     * @return List of all Groups
     */
    @Override
    public List<Group> getAll() {
        //wip
        return Collections.emptyList();
    }

    /**
     * Save Group to Database
     * @param group Group object to save
     */
    @Override
    public void save(Group group) {
        //wip
    }

    /**
     * Update Data of Group Table
     * @param group Group instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Group group, Map<String, String> params) {
        //wip
    }

    /**
     * Delete Group instance from Database
     * @param group Group object to delete
     */
    @Override
    public void delete(Group group) {
        //wip
    }
}
