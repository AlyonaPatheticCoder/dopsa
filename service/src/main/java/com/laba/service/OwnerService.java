package com.laba.service;

import com.laba.entity.Cat;
import com.laba.entity.Owner;
import java.util.List;

/**
 * Service interface for managing Owners and their Cats.
 */
public interface OwnerService {

    /**
     * Saves a new owner.
     *
     * @param owner the owner to save, must not be null
     * @throws IllegalArgumentException - if owner is null
     */
    void saveOwner(Owner owner);

    /**
     * Updates an existing owner.
     *
     * @param owner the owner to update, must not be null
     * @throws IllegalArgumentException if owner is null
     */
    void updateOwner(Owner owner);

    /**
     * Deletes an owner.
     *
     * @param owner the owner to delete, must not be null
     * @throws IllegalArgumentException - if owner is null
     */
    void deleteOwner(Owner owner);

    /**
     * Finds an owner by id.
     *
     * @param id the owner's id, must not be null
     * @return the owner or null if not found
     * @throws IllegalArgumentException - if id is null
     */
    Owner getOwnerById(Long id);

    /**
     * Returns all owners.
     *
     * @return list of all owners or empty if none found
     */
    List<Owner> getAllOwners();

    /**
     * Finds owners by name (case-insensitive).
     *
     * @param name the owner's name, must not be null
     * @return list of matching owners or empty if none found
     * @throws IllegalArgumentException - if name is null
     */
    List<Owner> findOwnersByName(String name);

    /**
     * Finds the owner of a specific cat.
     *
     * @param catId the cat's id, must not be null
     * @return the owner or null if not found
     * @throws IllegalArgumentException - if catId is null
     */
    Owner findOwnerByCatId(Long catId);

    /**
     * Finds owners who have cats with a specific name (case-insensitive).
     *
     * @param catName the cat's name, must not be null
     * @return list of owners or empty if none found
     * @throws IllegalArgumentException - if catName is null
     */
    List<Owner> findOwnersByCatName(String catName);

    /**
     * Returns all cats owned by a specific owner.
     *
     * @param id the owner's id, must not be null
     * @return list of cats or empty if none found
     * @throws IllegalArgumentException - if id is null
     */
    List<Cat> getCatsByOwnerId(Long id);
}