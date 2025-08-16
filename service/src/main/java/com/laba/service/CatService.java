package com.laba.service;

import com.laba.entity.Cat;
import java.util.List;

/**
 * Service interface for managing Cats and their relationships.
 */
public interface CatService {

    /**
     * Saves a new cat.
     *
     * @param cat the cat to save, must not be null
     * @throws IllegalArgumentException - if cat is null
     */
    void saveCat(Cat cat);

    /**
     * Updates an existing cat.
     *
     * @param cat the cat to update, must not be null
     * @throws IllegalArgumentException - if cat is null
     */
    void updateCat(Cat cat);

    /**
     * Deletes a cat.
     *
     * @param cat the cat to delete, must not be null
     * @throws IllegalArgumentException - if cat is null
     */
    void deleteCat(Cat cat);

    /**
     * Adds a friend to a cat.
     *
     * @param catId the id of the cat, must not be null
     * @param friendId the id of the friend cat, must not be null
     * @throws IllegalArgumentException - if catId or friendId is null
     */
    void addFriend(Long catId, Long friendId);

    /**
     * Removes a friend from a cat.
     *
     * @param catId the id of the cat, must not be null
     * @param friendId the id of the friend cat, must not be null
     * @throws IllegalArgumentException - if catId or friendId is null
     */
    void removeFriend(Long catId, Long friendId);

    /**
     * Gets all friends of a cat.
     *
     * @param catId the id of the cat, must not be null
     * @return list of friends or empty if none found
     * @throws IllegalArgumentException - if catId is null
     */
    List<Cat> getFriends(Long catId);

    /**
     * Finds a cat by its id.
     *
     * @param id the id of the cat, must not be null
     * @return the cat or null if not found
     * @throws IllegalArgumentException - if id is null
     */
    Cat getCatById(Long id);

    /**
     * Returns all cats.
     *
     * @return list of all cats or empty if none found
     */
    List<Cat> getAllCats();

    /**
     * Finds cats by name (case-insensitive).
     *
     * @param name the cat name, must not be null
     * @return list of matching cats or empty if none found
     * @throws IllegalArgumentException - if name is null
     */
    List<Cat> findCatsByName(String name);

    /**
     * Finds cats by owner's name (case-insensitive).
     *
     * @param ownerName the owner's name, must not be null
     * @return list of matching cats or empty if none found
     * @throws IllegalArgumentException - if ownerName is null
     */
    List<Cat> findCatsByOwnerName(String ownerName);

    /**
     * Finds cats by owner's id.
     *
     * @param ownerId the owner's id, must not be null
     * @return list of matching cats or empty if none found
     * @throws IllegalArgumentException - if ownerId is null
     */
    List<Cat> findCatsByOwnerId(Long ownerId);

    /**
     * Finds cats by name and owner's name (case-insensitive).
     *
     * @param catName the cat name, must not be null
     * @param ownerName the owner's name, must not be null
     * @return list of matching cats or empty if none found
     * @throws IllegalArgumentException - if catName or ownerName is null
     */
    List<Cat> findCatsByNameAndOwner(String catName, String ownerName);
}