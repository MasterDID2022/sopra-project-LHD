package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Slot;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
/**
 * SlotDAO implementing DAO interface for Slot Object
 */
public class SlotDAO implements DAO<Slot> {

    private final Connection conn;
    //private final PreparedStatement getAll;
    //private final PreparedStatement get;
    //private final PreparedStatement save;
    //private final PreparedStatement update;
    //private final PreparedStatement delete;

    private SlotDAO() throws SQLException {
        conn = Datasource.getInstance().getConnection();
    }

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
    public Slot update(Slot slot) throws IdException {
        return null;
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
