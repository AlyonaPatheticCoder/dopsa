package com.laba.dao;

import com.laba.DaoException;
import com.laba.entity.Owner;
import java.util.List;

/**
 * DAO interface with CRUD operations on Owners.
 */
public interface OwnerDao {

    /**
     * Saves a new owner.
     *
     * @param owner an owner object - must not be null
     * @throws IllegalArgumentException - if owner is null
     * @throws DaoException - if database error occurs
     */
    void save(Owner owner);

    /**
     * Updates an existing owner.
     *
     * @param owner an owner object - must not be null
     * @throws IllegalArgumentException - if owner is null
     * @throws DaoException - if a database error occurs
     */
    void update(Owner owner);

    /**
     * Deletes an owner.
     *
     * @param owner an owner object - must not be null
     * @throws IllegalArgumentException - if owner or owner.id is null
     * @throws DaoException - if a database error occurs or owner not found
     */
    void delete(Owner owner);

    /**
     * Finds an owner by id.
     *
     * @param id the id of the owner - must not be null
     * @return Owner or null if not found
     * @throws IllegalArgumentException - if id is null
     * @throws DaoException - if a database error occurs
     */
    Owner findById(Long id);

    /**
     * Finds all owners.
     *
     * @return a list of all owners or empty if none found
     * @throws DaoException - if a database error occurs
     */
    List<Owner> findAll();

    /**
     * Finds owners by name, case-insensitive.
     *
     * @param name the name of an owner - must not be null
     * @return a list of matching owners or empty if none found
     * @throws IllegalArgumentException - if name is null
     * @throws DaoException - if a database error occurs
     */
    List<Owner> findByName(String name);

    /**
     * Finds owners by a cat's name, case-insensitive.
     *
     * @param catName the name of the cat - must not be null
     * @return a list of matching owners or empty if none found
     * @throws IllegalArgumentException - if catName is null
     * @throws DaoException - if a database error occurs
     */
    List<Owner> findByCatName(String catName);

    /**
     * Finds an owner by a cat's id.
     *
     * @param catId the id of the cat - must not be null
     * @return Owner or null if not found
     * @throws IllegalArgumentException - if catId is null
     * @throws DaoException - if a database error occurs
     */
    Owner findByCatId(Long catId);
}