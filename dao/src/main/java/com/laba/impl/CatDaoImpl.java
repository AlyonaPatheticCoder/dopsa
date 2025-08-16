package com.laba.impl;

import com.laba.DaoException;
import com.laba.dao.CatDao;
import com.laba.dao.Dao;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.specification.CatSpecifications;
import com.laba.specification.Specification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.function.Consumer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * The Implementation of CatDao.
 */
public class CatDaoImpl extends Dao<Cat> implements CatDao {

    /**
     * Constructor of CatDao, extends abstract class Dao.
     *
     * @param sessionFactory Hibernate SessionFactory, must not be null
     * @throws IllegalArgumentException if sessionFactory is null
     */
    public CatDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Checks if object is null.
     *
     * @param obj object to check
     * @param message message if obj is null
     * @throws IllegalArgumentException - if sessionFactory is null
     */
    private void checkNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void save(Cat cat) {
        checkNotNull(cat, "Cat can't be null");
        executeInsideTransaction((Consumer<Session>) session -> session.persist(cat));
    }

    @Override
    public Cat findById(Long id) {
        checkNotNull(id, "Cat id can't be null");
        return executeWithoutTransaction(session -> session.get(Cat.class, id));
    }

    @Override
    public List<Cat> findAll() {
        return executeWithoutTransaction(session -> session.createQuery("FROM Cat", Cat.class).list());
    }

    private List<Cat> findBySpecification(Specification<Cat> spec) {
        return executeWithoutTransaction(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Cat> query = cb.createQuery(Cat.class);
            Root<Cat> root = query.from(Cat.class);

            Predicate predicate = spec != null ? spec.toPredicate(root, query, cb) : null;
            if (predicate != null) {
                query.where(predicate);
            }

            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public List<Cat> findByName(String name) {
        checkNotNull(name, "Cat name can't be null");
        Specification<Cat> spec = CatSpecifications.nameLike(name);
        return findBySpecification(spec);
    }

    @Override
    public List<Cat> findByOwnerName(String ownerName) {
        checkNotNull(ownerName, "Owner name can't be null");
        Specification<Cat> spec = CatSpecifications.ownerNameLike(ownerName);
        return findBySpecification(spec);
    }

    @Override
    public List<Cat> findByOwnerId(Long ownerId) {
        checkNotNull(ownerId, "Owner id can't be null");
        Specification<Cat> spec = (root, query, cb) -> cb.equal(root.join("owner").get("id"), ownerId);
        return findBySpecification(spec);
    }

    @Override
    public List<Cat> findByNameAndOwner(String name, String ownerName) {
        checkNotNull(name, "Cat name can't be null");
        checkNotNull(ownerName, "Owner name can't be null");
        Specification<Cat> spec = CatSpecifications.nameLike(name)
                .and(CatSpecifications.ownerNameLike(ownerName));
        return findBySpecification(spec);
    }

    @Override
    public void update(Cat cat) {
        checkNotNull(cat, "Cat can't be null");
        executeInsideTransaction((Consumer<Session>) session -> session.merge(cat));
    }

    @Override
    public void delete(Cat cat) {
        checkNotNull(cat, "Cat can't be null");
        checkNotNull(cat.getId(), "Cat id can't be null");
        executeInsideTransaction(session -> {
            Cat persistentCat = session.get(Cat.class, cat.getId());
            if (persistentCat != null) {
                session.remove(persistentCat);
            } else {
                throw new DaoException("Cat to delete not found");
            }
        });
    }
}