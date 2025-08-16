package com.laba.controller;

import com.laba.ServiceException;
import com.laba.entity.Cat;
import com.laba.service.CatService;

import java.util.List;

/**
 * Controller class for managing Cats.
 * Handles user requests to CatService.
 */
public class CatController {
    private final CatService catService;

    /**
     * Constructor of CatController.
     *
     * @param catService service used for cat operations, must not be null
     * @throws IllegalArgumentException if catService is null
     */
    public CatController(CatService catService) {
        this.catService = catService;
    }

    /**
     * Saves a new cat.
     *
     * @param cat the cat to save
     */
    public void createCat(Cat cat) {
        try {
            catService.saveCat(cat);
            System.out.println("Cat saved successfully");
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to save cat: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected save fail: " + e.getMessage());
        }
    }

    /**
     * Updates an existing cat.
     *
     * @param cat the cat to update
     */
    public void updateCat(Cat cat) {
        try {
            catService.updateCat(cat);
            System.out.println("Cat updated successfully");
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to update cat: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected update fail: " + e.getMessage());
        }
    }

    /**
     * Deletes a cat by its id.
     *
     * @param id the id of the cat
     */
    public void deleteCatById(Long id) {
        try {
            Cat cat = catService.getCatById(id);
            if (cat == null) {
                System.out.println("Cat not found with id: " + id);
                return;
            }
            catService.deleteCat(cat);
            System.out.println("Cat deleted successfully");
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to delete cat: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected delete fail: " + e.getMessage());
        }
    }

    /**
     * Finds a cat by its id.
     *
     * @param id the id of the cat
     * @return the found cat or null if not found
     */
    public Cat findCatById(Long id) {
        try {
            Cat cat = catService.getCatById(id);
            if (cat == null) {
                System.out.println("Cat not found with id: " + id);
            } else {
                System.out.println("Found cat: " + cat);
            }
            return cat;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find cat: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected fail finding cat: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns all cats.
     *
     * @return list of all cats
     */
    public List<Cat> findAllCats() {
        try {
            List<Cat> cats = catService.getAllCats();
            System.out.println("Found " + cats.size() + " cats");
            System.out.println("List of found cats: " + cats);
            return cats;
        } catch (ServiceException e) {
            System.err.println("Failed to get cats: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Finds cats by their name.
     *
     * @param name the name of the cats
     * @return list of matching cats
     */
    public List<Cat> findCatsByName(String name) {
        try {
            List<Cat> cats = catService.findCatsByName(name);
            System.out.println("Found " + cats.size() + " cats with name " + name);
            System.out.println("List of found cats: " + cats);
            return cats;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find cats by name: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Finds cats by their owner's name.
     *
     * @param ownerName the owner's name
     * @return list of matching cats
     */
    public List<Cat> findCatsByOwnerName(String ownerName) {
        try {
            List<Cat> cats = catService.findCatsByOwnerName(ownerName);
            System.out.println("Found " + cats.size() + " cats with owner " + ownerName);
            System.out.println("List of found cats: " + cats);
            return cats;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find cats by owner name: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Adds a friend to a cat.
     *
     * @param catId the cat's id
     * @param friendId the friend's id
     */
    public void addFriend(Long catId, Long friendId) {
        try {
            catService.addFriend(catId, friendId);
            System.out.println("Friend added successfully");
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to add friend: " + e.getMessage());
        }
    }
}