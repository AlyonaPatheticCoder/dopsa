package com.laba.service;

import com.laba.dto.OwnerDto;
import java.util.List;

/**
 * Service interface for managing Owners and their Cats.
 */
public interface OwnerService {

    /**
     * Gets owner by id.
     *
     * @param id the id
     * @return the owner by id
     */
    OwnerDto getOwnerById(Long id);

    /**
     * Gets all owners.
     *
     * @return the all owners
     */
    List<OwnerDto> getAllOwners();

    /**
     * Find owners by name list.
     *
     * @param name the name
     * @return the list
     */
    List<OwnerDto> findOwnersByName(String name);

    /**
     * Find owners by cat name list.
     *
     * @param catName the cat name
     * @return the list
     */
    List<OwnerDto> findOwnersByCatName(String catName);

    /**
     * Find owner by cat id owner dto.
     *
     * @param catId the cat id
     * @return the owner dto
     */
    OwnerDto findOwnerByCatId(Long catId);

    /**
     * Find owners by filter list.
     *
     * @param filter the filter
     * @return the list
     */
    List<OwnerDto> findOwnersByFilter(OwnerDto filter);

    /**
     * Update owner.
     *
     * @param ownerDto the owner dto
     */
    void updateOwner(OwnerDto ownerDto);

    /**
     * Delete owner.
     *
     * @param ownerId the owner id
     */
    void deleteOwner(Long ownerId);

    /**
     * Save owner.
     *
     * @param ownerDto the owner dto
     */
    void saveOwner(OwnerDto ownerDto);

    /**
     * Add cat to owner.
     *
     * @param ownerId the owner id
     * @param catId   the cat id
     */
    void addCatToOwner(Long ownerId, Long catId);

    /**
     * Remove cat from owner.
     *
     * @param ownerId the owner id
     * @param catId   the cat id
     */
    void removeCatFromOwner(Long ownerId, Long catId);
}