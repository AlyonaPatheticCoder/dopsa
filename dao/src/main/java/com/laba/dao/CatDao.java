package com.laba.dao;

import com.laba.DaoException;
import com.laba.entity.Cat;
import java.util.List;

/**
 * DAO interface with CRUD operations on Cats.
 * */
public interface CatDao {

    /**
     * Saves a new cat.
     *
     * @param cat a cat object - must not be null
     * @throws IllegalArgumentException  - if cat is null
     * @throws DaoException - if database error occurs
     */
    void save(Cat cat);

    /**
     * Finds a cat by id.
     *
     * @param id the id of the cat - must not be null
     * @return Cat or null if not found
     * @throws IllegalArgumentException - if id is null
     * @throws DaoException - if database error occurs
     */
    Cat findById(Long id);

    /**
     * Finds all cats.
     *
     * @return a list of all cats or empty if none found
     * @throws DaoException - if a database error occurs
     */
    List<Cat> findAll();

    /**
     * Finds cats by name, case-insensitive.
     *
     * @param name the name of a cat, must not be null
     * @return a list of matching cats or empty if none found
     * @throws IllegalArgumentException - if name is null
     * @throws DaoException - if a database error occurs
     */
    List<Cat> findByName(String name);

    /**
     * Finds cats by their owner's name, case-insensitive.
     *
     * @param ownerName the owner's name, must not be null
     * @return a list of matching cats or empty if none found
     * @throws IllegalArgumentException - if ownerName is null
     * @throws DaoException - if a database error occurs
     */
    List<Cat> findByOwnerName(String ownerName);

    /**
     * Finds cats by their owner's id.
     *
     * @param ownerId the owner's id, must not be null
     * @return a list of matching cats or empty if none found
     * @throws IllegalArgumentException - if ownerId is null
     * @throws DaoException - if a database error occurs
     */
    List<Cat> findByOwnerId(Long ownerId);

    /**
     * Finds cats by cat name and owner's name, case-insensitive.
     *
     * @param name the cat's name, must not be null
     * @param ownerName the owner's, must not be null
     * @return a list of matching cats or empty if none found
     * @throws IllegalArgumentException - if name or ownerName is null
     * @throws DaoException - if a database error occurs
     */
    List<Cat> findByNameAndOwner(String name, String ownerName);

    /**
     * Updates an existing cat.
     *
     * @param cat a cat object, must not be null
     * @throws IllegalArgumentException - if cat is null
     * @throws DaoException - if a database error occurs
     */
    void update(Cat cat);

    /**
     * Deletes a cat.
     *
     * @param cat a cat object, must not be null
     * @throws IllegalArgumentException - if cat or cat.id is null
     * @throws DaoException - if a database error occurs or cat not found
     */
    void delete(Cat cat);
}