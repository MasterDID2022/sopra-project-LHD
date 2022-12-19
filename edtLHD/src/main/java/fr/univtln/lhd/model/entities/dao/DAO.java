package fr.univtln.lhd.model.entities.dao;

import fr.univtln.lhd.exceptions.IdException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Dao interface implemented by every DAO class (interacting with Database)
 * @param <T>
 */
public interface DAO<T> {

    /**
     * Getter for one Entity
     * @param id numerical long identifier for getting the Entity 
     * @return May return one Entity
     */
    Optional<T> get(long id) throws SQLException;

    /**
     * Getter for all Entities
     * @return List of all Entities
     */
    List<T> getAll() throws SQLException;

    /**
     * Save Entity t to Database
     * @param t Entity object to save
     */
    void save(T t) throws  SQLException;

    /**
     * Update Data of Entity t
     * @param t Entity t
     */
    T update(T t) throws IdException,SQLException;

    /**
     * Delete Entity t from Database
     * @param t Entity t
     */
    void delete(T t) throws  SQLException;
}
