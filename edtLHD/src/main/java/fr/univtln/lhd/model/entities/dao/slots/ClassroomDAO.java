package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Classroom;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ClassroomDAO implementing DAO interface for Classroom Object
 */
public class ClassroomDAO implements DAO<Classroom>
{

    /**
     * Getter for one Classroom
     * @param id numerical long identifier for getting the Classroom
     * @return May return one Classroom instance
     */
    @Override
    public Optional<Classroom> get(long id) {
        //wip
        return Optional.empty();
    }

    /**
     * Getter for all Classrooms
     * @return List of all Classrooms
     */
    @Override
    public List<Classroom> getAll() {
        //wip
        return Collections.emptyList();
    }

    /**
     * Save Classroom to Database
     * @param classroom Classroom object to save
     */
    @Override
    public void save(Classroom classroom) {
        //wip
    }

    /**
     * Update Data of Classroom Table
     * @param classroom Classroom instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Classroom classroom, Map<String, String> params) {
        //wip
    }

    /**
     * Delete Classroom instance from Database
     * @param classroom Classroom object to delete
     */
    @Override
    public void delete(Classroom classroom) {
        //wip
    }
}
