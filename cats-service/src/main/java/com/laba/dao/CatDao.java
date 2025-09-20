package com.laba.dao;

import com.laba.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Cat DAO extends JpaRepository
 */
public interface CatDao extends JpaRepository<Cat, Long>, JpaSpecificationExecutor<Cat> {

    /**
     * Find by name ignore case list.
     *
     * @param name the name
     * @return the list
     */
    List<Cat> findByNameIgnoreCase(String name);

    /**
     * Find by owner name ignore case list.
     *
     * @param ownerName the owner name
     * @return the list
     */
    List<Cat> findByOwner_NameIgnoreCase(String ownerName);

    /**
     * Find by owner id list.
     *
     * @param ownerId the owner id
     * @return the list
     */
    List<Cat> findByOwner_Id(Long ownerId);

}