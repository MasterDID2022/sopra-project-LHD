package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.user.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminDAO implements DAO<Admin> {
    /**
     * Getter for one Admin
     * @param id numerical long identifier for getting the Admin
     * @return May return one Admin
     */
    @Override
    public Optional<Admin> get(long id) {
        return Optional.empty();
    }

    /**
     * Getter for all Admin
     * @return List of all Admin
     */
    @Override
    public List<Admin> getAll() {
        return null;
    }


    /**
     * Save Admin t to Database
     * @param o Admin object to save
     */
    @Override
    public void save(Admin o) {
        //WIP
    }

    /**
     * Update Data of Admin t
     * @param o Admin t
     * @param params Map of attributes and values
     */
    @Override
    public void update(Admin o, Map params) {
        //WIP
    }

    /**
     * Delete Admin t from Database
     * @param o Admin t
     */
    @Override
    public void delete(Admin o) {
        //WIP
    }
}
