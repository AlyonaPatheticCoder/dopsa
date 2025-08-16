package com.laba.controller;
import com.laba.ServiceException;
import com.laba.entity.Owner;
import com.laba.entity.Cat;
import com.laba.service.OwnerService;
import java.util.List;

/**
 * Controller class for managing Owners.
 * Handles user requests to OwnerService.
 */
public class OwnerController {
    private final OwnerService ownerService;

    /**
     * Constructor of CatController.
     *
     * @param ownerService service used for cat operations, must not be null
     * @throws IllegalArgumentException if catService is null
     */
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Saves a new owner.
     *
     * @param owner the owner to save
     */
    public void createOwner(Owner owner) {
        try {
            ownerService.saveOwner(owner);
            System.out.println("Owner saved successfully");
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to save owner: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected fail saving owner: " + e.getMessage());
        }
    }

    /**
     * Updates an existing owner.
     *
     * @param owner the owner to update
     */
    public void updateOwner(Owner owner) {
        try {
            ownerService.updateOwner(owner);
            System.out.println("Owner updated successfully.");
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to update owner: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected fail updating owner: " + e.getMessage());
        }
    }

    /**
     * Deletes an owner by id.
     *
     * @param id the id of the owner
     */
    public void deleteOwnerById(Long id) {
        try {
            Owner owner = ownerService.getOwnerById(id);
            if (owner == null) {
                System.out.println("Owner not found with id: " + id);
                return;
            }
            ownerService.deleteOwner(owner);
            System.out.println("Owner deleted successfully");
        } catch (ServiceException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Failed to delete owner: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected fail deleting owner: " + e.getMessage());
        }
    }

    /**
     * Finds an owner by id.
     *
     * @param id the id of the owner
     * @return the found owner or null if not found
     */
    public Owner findOwnerById(Long id) {
        try {
            Owner owner = ownerService.getOwnerById(id);
            if (owner == null) {
                System.out.println("Owner not found with id: " + id);
            } else {
                System.out.println("Found owner: " + owner);
            }
            return owner;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find owner: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error finding owner: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns all owners.
     *
     * @return list of all owners
     */
    public List<Owner> findAllOwners() {
        try {
            List<Owner> owners = ownerService.getAllOwners();
            return owners;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to get owners: " + e.getMessage());
            return List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Finds owners by their name.
     *
     * @param name the name to search for
     * @return list of matching owners
     */
    public List<Owner> findOwnersByName(String name) {
        try {
            List<Owner> owners = ownerService.findOwnersByName(name);
            System.out.println("Found " + owners.size() + " owners with name " + name);
            System.out.println("List of found owners: " + owners);
            return owners;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find owners by name: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Finds owners by their cat's name.
     *
     * @param catName the cat's name
     * @return list of owners who have a cat with the given name
     */
    public List<Owner> findOwnersByCatName(String catName) {
        try {
            List<Owner> owners = ownerService.findOwnersByCatName(catName);
            System.out.println("Found " + owners.size() + " owners with cat name " + catName);
            System.out.println("List of found owners: " + owners);
            return owners;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find owners by cat name: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Finds an owner by a cat's id.
     *
     * @param catId the id of the cat
     * @return the owner of the cat or null if not found
     */
    public Owner findOwnerByCatId(Long catId) {
        try {
            Owner owner = ownerService.findOwnerByCatId(catId);
            if (owner == null) {
                System.out.println("Owner not found for cat id: " + catId);
            } else {
                System.out.println("Found owner: " + owner);
            }
            return owner;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to find owner by cat id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns all cats for a given owner id.
     *
     * @param ownerId the id of the owner
     * @return list of cats belonging to the owner
     */
    public List<Cat> getCatsByOwnerId(Long ownerId) {
        try {
            List<Cat> cats = ownerService.getCatsByOwnerId(ownerId);
            System.out.println("Found " + cats.size() + " cats for owner id: " + ownerId);
            System.out.println("List of found cats: " + cats);
            return cats;
        } catch (ServiceException | IllegalArgumentException e) {
            System.err.println("Failed to get cats by owner id: " + e.getMessage());
            return List.of();
        }
    }
}
