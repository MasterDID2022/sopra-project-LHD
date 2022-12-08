package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.user.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentDAO implements DAO<Student> {
    /**
     * Getter for one Student
     * @param id numerical long identifier for getting the Student
     * @return May return one Student
     */
    @Override
    public Optional<Student> get(long id) {
        return Optional.empty();
    }

    /**
     * Getter for all Student
     * @return List of all Student
     */
    @Override
    public List<Student> getAll() {
        return null;
    }


    /**
     * Save Student t to Database
     * @param o Student object to save
     */
    @Override
    public void save(Student o) {
        //WIP
    }

    /**
     * Update Data of Student t
     * @param o Student t
     * @param params Map of attributes and values
     */
    @Override
    public void update(Student o, Map params) {
        //WIP
    }

    /**
     * Delete Student t from Database
     * @param o Student t
     */
    @Override
    public void delete(Student o) {
        //WIP
    }
}
