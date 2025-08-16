package com.laba.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Criteria specification interface used to filter entities.
 *
 * @param <T> entity type
 */
@FunctionalInterface
public interface Specification<T> {

    /**
     * Converts a specification into a JPA {@link Predicate}.
     *
     * @param root  root of a query
     * @param query criteria query
     * @param cb    criteria builder
     * @return the predicate representing the specification or null if no condition
     */
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);


    default Specification<T> and(Specification<T> other) {
        return (root, query, cb) -> {
            Predicate p1 = this.toPredicate(root, query, cb);
            Predicate p2 = other.toPredicate(root, query, cb);
            if (p1 == null) return p2;
            if (p2 == null) return p1;
            return cb.and(p1, p2);
        };
    }

    default Specification<T> or(Specification<T> other) {
        return (root, query, cb) -> {
            Predicate p1 = this.toPredicate(root, query, cb);
            Predicate p2 = other.toPredicate(root, query, cb);
            if (p1 == null) return p2;
            if (p2 == null) return p1;
            return cb.or(p1, p2);
        };
    }
}