package com.laba.dao;

import com.laba.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Cat DAO extends JpaRepository
 */
public interface OwnerDao extends JpaRepository<Owner, Long>, JpaSpecificationExecutor<Owner> {

    /**
     * Find by name ignore case list.
     *
     * @param name the name
     * @return the list
     */
    List<Owner> findByNameIgnoreCase(String name);

    /**
     * Find by cats name ignore case list.
     *
     * @param catName the cat name
     * @return the list
     */
    List<Owner> findByCats_NameIgnoreCase(String catName);

    /**
     * Find by cats id optional.
     *
     * @param catId the cat id
     * @return the optional
     */
    Optional<Owner> findByCats_Id(Long catId);

    @Query("SELECT o FROM Owner o LEFT JOIN FETCH o.cats")
    List<Owner> findAllWithCats();

    @Query("SELECT o FROM Owner o LEFT JOIN FETCH o.cats WHERE o.id = :id")
    Optional<Owner> findByIdWithCats(@Param("id") Long id);

}