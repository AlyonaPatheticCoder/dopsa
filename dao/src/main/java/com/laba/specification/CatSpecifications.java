package com.laba.specification;

import com.laba.entity.Cat;
import com.laba.entity.Color;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Specifications for filtering {@link Cat} entities.
 */
public class CatSpecifications {

    public static Specification<Cat> nameLike(String name) {
        return (Root<Cat> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Cat> ownerNameLike(String ownerName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.join("owner").get("name")), "%" + ownerName.toLowerCase() + "%");
    }

    public static Specification<Cat> breedEquals(String breed) {
        return (root, query, cb) -> cb.equal(root.get("breed"), breed);
    }

    public static Specification<Cat> colorEquals(Color color) {
        return (root, query, cb) -> cb.equal(root.get("color"), color);
    }
}