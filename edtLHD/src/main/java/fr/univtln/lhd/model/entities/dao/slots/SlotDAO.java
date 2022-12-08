package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Slot;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SlotDAO implementing DAO interface for Slot Object
 */
public class SlotDAO implements DAO<Slot> {

    /**
     * Getter for one Slot
     * @param id numerical long identifier for getting the Slot
     * @return May return one Slot instance
     */
    @Override
    public Optional<Slot> get(long id) {
        //wip
        return Optional.empty();
    }

    /**
     * Getter for all Slots
     * @return List of all Slots
     */
    @Override
    public List<Slot> getAll() {
        //wip
        return Collections.emptyList();
    }

    /**
     * Save Slot to Database
     * @param slot Slot object to save
     */
    @Override
    public void save(Slot slot) {
        //wip
    }

    /**
     * Update Data of Slot Table
     * @param slot Slot instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Slot slot, Map<String, String> params) {
        //wip
    }

    /**
     * Delete Slot instance from Database
     * @param slot Slot object to delete
     */
    @Override
    public void delete(Slot slot) {
        //wip
    }
}
