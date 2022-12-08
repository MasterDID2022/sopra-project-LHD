package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.user.Lecturer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LecturerDAO implements DAO<Lecturer> {
    /**
     * Getter for one Lecturer
     * @param id numerical long identifier for getting the Lecturer
     * @return May return one Lecturer
     */
    @Override
    public Optional<Lecturer> get(long id) {
        return Optional.empty();
    }

    /**
     * Getter for all Lecturer
     * @return List of all Lecturer
     */
    @Override
    public List<Lecturer> getAll() {
        return null;
    }


    /**
     * Save Lecturer t to Database
     * @param o Lecturer object to save
     */
    @Override
    public void save(Lecturer o) {
        //WIP
    }

    /**
     * Update Data of Lecturer t
     * @param o Lecturer t
     * @param params Map of attributes and values
     */
    @Override
    public void update(Lecturer o, Map params) {
        //WIP
    }

    /**
     * Delete Lecturer t from Database
     * @param o Lecturer t
     */
    @Override
    public void delete(Lecturer o) {
        //WIP
    }
}
