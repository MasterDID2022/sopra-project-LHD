package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Subject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SubjectDAO implementing DAO interface for Subject Object
 */
public class SubjectDAO implements DAO<Subject> {

    /**
     * Getter for one Subject
     * @param id numerical long identifier for getting the Subject
     * @return May return one Subject instance
     */
    @Override
    public Optional<Subject> get(long id) {
        //wip
        return Optional.empty();
    }

    /**
     * Getter for all Subjects
     * @return List of all Subjects
     */
    @Override
    public List<Subject> getAll() {
        //wip
        return Collections.emptyList();
    }

    /**
     * Save Subject to Database
     * @param subject Subject object to save
     */
    @Override
    public void save(Subject subject) {
        //wip
    }

    /**
     * Update Data of Subject Table
     * @param subject Subject instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Subject subject, Map<String, String> params) {
        //wip
    }

    /**
     * Delete Subject instance from Database
     * @param subject Subject object to delete
     */
    @Override
    public void delete(Subject subject) {
        //wip
    }
}
