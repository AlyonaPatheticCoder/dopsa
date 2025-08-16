package com.laba.specification;

import com.laba.entity.Owner;
import com.laba.entity.Cat;
import com.laba.specification.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Specifications for filtering {@link Owner} entities.
 */
public class OwnerSpecifications {

    public static Specification<Owner> nameLike(String name) {
        return (Root<Owner> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Owner> hasCatWithId(Long catId) {
        return (root, query, cb) ->
                cb.equal(root.join("cats").get("id"), catId);
    }

    public static Specification<Owner> hasCatWithName(String catName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.join("cats").get("name")), "%" + catName.toLowerCase() + "%");
    }
}