package com.laba.impl;

import com.laba.DaoException;
import com.laba.ServiceException;
import com.laba.dao.OwnerDao;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import com.laba.service.OwnerService;
import java.time.LocalDate;
import java.util.List;

/**
 * The Implementation of OwnerService.
 */
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDao ownerDao;

    /**
     * Constructor of CatDao.
     *
     * @param ownerDao OwnerDao object, must not be null
     * @throws IllegalArgumentException - if either parameter is null
     */
    public OwnerServiceImpl(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @Override
    public void saveOwner(Owner owner) {
        checkOwner(owner, true);
        try {
            ownerDao.save(owner);
        } catch (DaoException e) {
            System.err.println("DAO error in saveOwner: " + e.getMessage());
            throw new ServiceException("Failed to save owner", e);
        }
    }

    @Override
    public void updateOwner(Owner owner) {
        checkOwner(owner, false);
        Owner existing = ownerDao.findById(owner.getId());
        if (existing != null && existing.isSame(owner)) {
            return;
        }
        try {
            ownerDao.update(owner);
        } catch (DaoException e) {
            System.err.println("DAO error in updateOwner: " + e.getMessage());
            throw new ServiceException("Failed to update owner", e);
        }
    }

    @Override
    public void deleteOwner(Owner owner) {
        if (owner == null || owner.getId() == null) {
            throw new IllegalArgumentException("Owner or its id can't be null");
        }

        Owner persistentOwner;
        try {
            persistentOwner = ownerDao.findById(owner.getId());
        } catch (DaoException e) {
            System.err.println("DAO error in findById (deleteOwner): " + e.getMessage());
            throw new ServiceException("Failed to find owner by id", e);
        }

        if (persistentOwner == null) {
            throw new IllegalArgumentException("Owner does not exist");
        }

        if (persistentOwner.getCats() != null && !persistentOwner.getCats().isEmpty()) {
            throw new IllegalStateException("Can't delete owner with existing cats");
        }

        try {
            ownerDao.delete(owner);
        } catch (DaoException e) {
            System.err.println("DAO error in deleteOwner: " + e.getMessage());
            throw new ServiceException("Failed to delete owner", e);
        }
    }

    @Override
    public Owner getOwnerById(Long id) {
        if (id == null) throw new IllegalArgumentException("Owner id can't be null");
        try {
            return ownerDao.findById(id);
        } catch (DaoException e) {
            System.err.println("DAO error in getOwnerById: " + e.getMessage());
            throw new ServiceException("Failed to find owner by id", e);
        }
    }

    @Override
    public List<Owner> getAllOwners() {
        try {
            return ownerDao.findAll();
        } catch (DaoException e) {
            System.err.println("DAO error in getAllOwners: " + e.getMessage());
            throw new ServiceException("Failed to get all owners", e);
        }
    }

    @Override
    public List<Owner> findOwnersByName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name can't be null or empty");
        try {
            return ownerDao.findByName(name);
        } catch (DaoException e) {
            System.err.println("DAO error in findOwnersByName: " + e.getMessage());
            throw new ServiceException("Failed to find owners by name", e);
        }
    }

    @Override
    public List<Owner> findOwnersByCatName(String catName) {
        if (catName == null || catName.isBlank()) throw new IllegalArgumentException("Cat name can't be null or empty");
        try {
            return ownerDao.findByCatName(catName);
        } catch (DaoException e) {
            System.err.println("DAO error in findOwnersByCatName: " + e.getMessage());
            throw new ServiceException("Failed to find owners by cat name", e);
        }
    }

    @Override
    public Owner findOwnerByCatId(Long catId) {
        if (catId == null) throw new IllegalArgumentException("Cat id can't be null");
        try {
            return ownerDao.findByCatId(catId);
        } catch (DaoException e) {
            System.err.println("DAO error in findOwnerByCatId: " + e.getMessage());
            throw new ServiceException("Failed to find owner by cat id", e);
        }
    }

    @Override
    public List<Cat> getCatsByOwnerId(Long ownerId) {
        if (ownerId == null) throw new IllegalArgumentException("Owner id can't be null");
        Owner owner;
        try {
            owner = ownerDao.findById(ownerId);
        } catch (DaoException e) {
            System.err.println("DAO error in getCatsByOwnerId: " + e.getMessage());
            throw new ServiceException("Failed to find owner by id", e);
        }

        if (owner == null) {
            throw new IllegalArgumentException("Owner not found");
        }
        return owner.getCats();
    }

    /**
     * Validates the owner.
     *
     * @param owner owner object to validate
     * @param isNew true if the owner is new
     */
    private void checkOwner(Owner owner, boolean isNew) {
        if (owner == null)
            throw new IllegalArgumentException("Owner can't be null");

        if (!isNew && (owner.getId() == null))
            throw new IllegalArgumentException("Owner id can't be null");

        if (owner.getName() == null || owner.getName().trim().isEmpty())
            throw new IllegalArgumentException("Owner name can't be null or empty string");

        if (owner.getBirthday() == null)
            throw new IllegalArgumentException("Owner birthday can't be null");

        if (owner.getName().length() > 30)
            throw new IllegalArgumentException("Owner name must be under 30 characters");

        if (owner.getBirthday().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Incorrect birthday");
    }
}