package fr.univtln.lhd.model.entities.dao;

import fr.univtln.lhd.exceptions.IdException;

import java.util.List;
import java.util.Optional;

/**
 * Dao interface implemented by every Dao class (interacting with Database)
 * @param <T>
 */
public interface DAO<T> {

    /**
     * Getter for one Entity
     * @param id numerical long identifier for getting the Entity 
     * @return May return one Entity
     */
    Optional<T> get(long id);

    /**
     * Getter for all Entities
     * @return List of all Entities
     */
    List<T> getAll();

    /**
     * Save Entity t to Database
     * @param t Entity object to save
     */
    void save(T t);

    /**
     * Update Data of Entity t
     * @param t Entity t
     */
    T update(T t) throws IdException;

    /**
     * Delete Entity t from Database
     * @param t Entity t
     */
    void delete(T t);
}
