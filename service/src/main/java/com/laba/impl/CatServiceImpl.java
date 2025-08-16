package com.laba.impl;
import com.laba.DaoException;
import com.laba.ServiceException;
import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.entity.Cat;
import com.laba.service.CatService;
import com.laba.entity.Owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Implementation of CatService.
 */
public class CatServiceImpl implements CatService {

    private final CatDao catDao;
    private final OwnerDao ownerDao;

    /**
     * Constructor of CatDao.
     *
     * @param ownerDao OwnerDao object, must not be null
     * @param catDao CatDao object, must not be null
     * @throws IllegalArgumentException - if either parameter is null
     */
    public CatServiceImpl(OwnerDao ownerDao, CatDao catDao) {
        this.ownerDao = ownerDao;
        this.catDao = catDao;
    }

    @Override
    public void saveCat(Cat cat) {
        checkCat(cat, true);
        try {
            catDao.save(cat);
        } catch (DaoException e) {
            System.err.println("DAO error in saveCat: " + e.getMessage());
            throw new ServiceException("Failed to save cat", e);
        }
    }


    @Override
    public void updateCat(Cat cat) {
        checkCat(cat, false);
        Cat existing = catDao.findById(cat.getId());
        if (existing != null && existing.isSame(cat)) {
            return;
        }
        try {
            catDao.update(cat);
        } catch (DaoException e) {
            System.err.println("DAO error in updateCat: " + e.getMessage());
            throw new ServiceException("Failed to update cat", e);
        }}

    @Override
    public void deleteCat(Cat cat) {
        if (cat == null || cat.getId() == null) {
            throw new IllegalArgumentException("Cat or its id can't be null");
        }
        try {
            catDao.delete(cat);
        } catch (DaoException e) {
            System.err.println("DAO error in deleteCat: " + e.getMessage());
            throw new ServiceException("Failed to delete cat", e);
        }
    }

    @Override
    public Cat getCatById(Long id) {
        if (id == null) throw new IllegalArgumentException("Cat id can't be null");
        try {
            return catDao.findById(id);
        } catch (DaoException e) {
            System.err.println("DAO error in getCatById: " + e.getMessage());
            throw new ServiceException("Failed to find cat by id", e);
        }
    }

    @Override
    public List<Cat> getAllCats() {
        try {
            return catDao.findAll();
        } catch (DaoException e) {
            System.err.println("DAO error in getAllCats: " + e.getMessage());
            throw new ServiceException("Failed to get all cats", e);
        }
    }

    @Override
    public List<Cat> findCatsByName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Cat name can't be null or empty string");
        try {
            return catDao.findByName(name.trim());
        } catch (DaoException e) {
            System.err.println("DAO error in findCatsByName: " + e.getMessage());
            throw new ServiceException("Failed to find cats by name", e);
        }
    }

    @Override
    public List<Cat> findCatsByOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty())
            throw new IllegalArgumentException("Owner name can't be null or empty string");
        try {
            return catDao.findByOwnerName(ownerName.trim());
        } catch (DaoException e) {
            System.err.println("DAO error in findCatsByOwnerName: " + e.getMessage());
            throw new ServiceException("Failed to find cats by owner name", e);
        }
    }

    @Override
    public List<Cat> findCatsByOwnerId(Long ownerId) {
        if (ownerId == null)
            throw new IllegalArgumentException("Owner id can't be null");
        try {
            return catDao.findByOwnerId(ownerId);
        } catch (DaoException e) {
            System.err.println("DAO error in findCatsByOwnerId: " + e.getMessage());
            throw new ServiceException("Failed to find cats by owner id", e);
        }
    }

    @Override
    public List<Cat> findCatsByNameAndOwner(String catName, String ownerName) {
        if (catName == null || catName.trim().isEmpty())
            throw new IllegalArgumentException("Cat name can't be null or empty string");
        if (ownerName == null || ownerName.trim().isEmpty())
            throw new IllegalArgumentException("Owner name can't be null or empty string");
        try {
            return catDao.findByNameAndOwner(catName.trim(), ownerName.trim());
        } catch (DaoException e) {
            System.err.println("DAO error in findCatsByNameAndOwner: " + e.getMessage());
            throw new ServiceException("Failed to find cats by name and owner", e);
        }
    }

    @Override
    public void addFriend(Long catId, Long friendId) {
        if (catId == null || friendId == null)
            throw new IllegalArgumentException("Cat ids can't be null");

        if (catId.equals(friendId))
            throw new IllegalArgumentException("Cats can't be the same");

        Cat cat;
        Cat friend;
        try {
            cat = catDao.findById(catId);
            friend = catDao.findById(friendId);
        } catch (DaoException e) {
            System.err.println("DAO error in addFriend (find cats): " + e.getMessage());
            throw new ServiceException("Failed to find cats for friendship", e);
        }

        if (cat == null || friend == null)
            throw new IllegalArgumentException("Cats must exist to add friendship");

        if (cat.getFriends() == null) cat.setFriends(new ArrayList<>());
        if (friend.getFriends() == null) friend.setFriends(new ArrayList<>());

        if (!cat.getFriends().contains(friend)) {
            cat.getFriends().add(friend);
            try {
                catDao.update(cat);
            } catch (DaoException e) {
                System.err.println("DAO error updating cat friends (cat): " + e.getMessage());
                throw new ServiceException("Failed to update cat with new friend", e);
            }
        }

        if (!friend.getFriends().contains(cat)) {
            friend.getFriends().add(cat);
            try {
                catDao.update(friend);
            } catch (DaoException e) {
                System.err.println("DAO error updating cat friends (friend): " + e.getMessage());
                throw new ServiceException("Failed to update friend cat with new friend", e);
            }
        }
    }

    @Override
    public void removeFriend(Long catId, Long friendId) {
        if (catId == null || friendId == null)
            throw new IllegalArgumentException("Cat ids can't be null");

        if (catId.equals(friendId))
            throw new IllegalArgumentException("Cats can't be the same");

        Cat cat;
        Cat friend;
        try {
            cat = catDao.findById(catId);
            friend = catDao.findById(friendId);
        } catch (DaoException e) {
            System.err.println("DAO error in removeFriend (find cats): " + e.getMessage());
            throw new ServiceException("Failed to find cats for removing friendship", e);
        }

        if (cat == null || friend == null)
            throw new IllegalArgumentException("Cats must exist to remove friendship");

        if (cat.getFriends() != null && cat.getFriends().contains(friend)) {
            cat.getFriends().remove(friend);
            try {
                catDao.update(cat);
            } catch (DaoException e) {
                System.err.println("DAO error updating cat friends (cat): " + e.getMessage());
                throw new ServiceException("Failed to update cat after removing friend", e);
            }
        }

        if (friend.getFriends() != null && friend.getFriends().contains(cat)) {
            friend.getFriends().remove(cat);
            try {
                catDao.update(friend);
            } catch (DaoException e) {
                System.err.println("DAO error updating cat friends (friend): " + e.getMessage());
                throw new ServiceException("Failed to update friend cat after removing friend", e);
            }
        }
    }

    @Override
    public List<Cat> getFriends(Long catId) {
        if (catId == null)
            throw new IllegalArgumentException("Cat id can't be null");

        Cat cat;
        try {
            cat = catDao.findById(catId);
        } catch (DaoException e) {
            System.err.println("DAO error in getFriends: " + e.getMessage());
            throw new ServiceException("Failed to find cat for getting friends", e);
        }

        if (cat == null)
            throw new IllegalArgumentException("Cat must exist to get friends");

        if (cat.getFriends() == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(cat.getFriends());
        }
    }

    /**
     * Validates the cat.
     *
     * @param cat cat object to validate
     * @param isNew true if the cat is new
     */
    private void checkCat(Cat cat, boolean isNew) {
        if (cat == null)
            throw new IllegalArgumentException("Cat can't be null");

        if (!isNew && (cat.getId() == null))
            throw new IllegalArgumentException("Cat id can't be null");

        if (cat.getName() == null || cat.getName().trim().isEmpty())
            throw new IllegalArgumentException("Cat name can't be null or empty string");

        if (cat.getName().length() > 30)
            throw new IllegalArgumentException("Cat name must be under 30 characters");

        if (cat.getBirthday() == null)
            throw new IllegalArgumentException("Cat birthday can't be null");

        if (cat.getBirthday().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Incorrect birthday");

        if (cat.getColor() == null)
            throw new IllegalArgumentException("Cat color can't be null");

        if (cat.getOwner() == null)
            throw new IllegalArgumentException("Cat owner can't be null");

        if (cat.getOwner().getId() == null)
            throw new IllegalArgumentException("Owner id can't be null");

        if (cat.getBreed() == null || cat.getBreed().trim().isEmpty())
            throw new IllegalArgumentException("Cat breed can't be null or empty string");

        Owner owner;
        try {
            owner = ownerDao.findById(cat.getOwner().getId());
        } catch (DaoException e) {
            System.err.println("DAO error checking cat owner existence: " + e.getMessage());
            throw new ServiceException("Failed to check cat owner existence", e);
        }

        if (owner == null)
            throw new IllegalArgumentException("Cat owner doesn't exist");
    }
}