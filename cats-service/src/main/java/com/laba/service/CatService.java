package com.laba.service;

import com.laba.dto.CatDto;
import java.util.List;

/**
 * Service interface for managing Cats and their relationships.
 */
public interface CatService {

    /**
     * Gets cat by id.
     *
     * @param id the id
     * @return the cat by id
     */
    CatDto getCatById(Long id);

    /**
     * Gets all cats.
     *
     * @return the all cats
     */
    List<CatDto> getAllCats();

    /**
     * Find cats by name list.
     *
     * @param name the name
     * @return the list
     */
    List<CatDto> findCatsByName(String name);

    /**
     * Find cats by owner name list.
     *
     * @param ownerName the owner name
     * @return the list
     */
    List<CatDto> findCatsByOwnerName(String ownerName);

    /**
     * Find cats by owner id list.
     *
     * @param ownerId the owner id
     * @return the list
     */
    List<CatDto> findCatsByOwnerId(Long ownerId);

    /**
     * Find cats by filter list.
     *
     * @param filter the filter
     * @return the list
     */
    List<CatDto> findCatsByFilter(CatDto filter);

    /**
     * Update cat.
     *
     * @param catDto the cat dto
     */
    void updateCat(CatDto catDto);

    /**
     * Delete cat.
     *
     * @param catId the cat id
     */
    void deleteCat(Long catId);

    /**
     * Save cat.
     *
     * @param catDto the cat dto
     */
    void saveCat(CatDto catDto);

    /**
     * Add friend.
     *
     * @param catId    the cat id
     * @param friendId the friend id
     */
    void addFriend(Long catId, Long friendId);

    /**
     * Remove friend.
     *
     * @param catId    the cat id
     * @param friendId the friend id
     */
    void removeFriend(Long catId, Long friendId);

}
