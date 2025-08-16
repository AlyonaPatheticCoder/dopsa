package com.laba.impl;
import com.laba.DaoException;
import com.laba.dao.Dao;
import com.laba.dao.OwnerDao;
import com.laba.entity.Cat;
import com.laba.entity.Owner;
import com.laba.specification.OwnerSpecifications;
import com.laba.specification.Specification;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;
import java.util.function.Consumer;

/**
 * The Implementation of OwnerDao.
 */
public class OwnerDaoImpl extends Dao<Cat> implements OwnerDao {

    /**
     * Constructor of OwnerDao.
     *
     * @param sessionFactory Hibernate SessionFactory, must not be null
     * @throws IllegalArgumentException if sessionFactory is null
     */
    public OwnerDaoImpl(SessionFactory sessionFactory) {
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
    public void save(Owner owner) {
        checkNotNull(owner, "Owner can't be null");
        executeInsideTransaction((Consumer<Session>) session -> {
            session.persist(owner);
        });
    }

    @Override
    public void update(Owner owner) {
        checkNotNull(owner, "Owner can't be null");
        executeInsideTransaction((Consumer<Session>) session -> {
            session.merge(owner);
        });
    }

    @Override
    public void delete(Owner owner) {
        checkNotNull(owner, "Owner can't be null");
        checkNotNull(owner.getId(), "Owner id can't be null");

        executeInsideTransaction((Consumer<Session>) session -> {
            Owner persistentOwner = session.get(Owner.class, owner.getId());
            if (persistentOwner != null) {
                session.remove(persistentOwner);
            } else {
                throw new DaoException("Owner to delete not found");
            }
        });
    }

    @Override
    public Owner findById(Long id) {
        checkNotNull(id, "Owner id can't be null");
        return executeWithoutTransaction(session -> session.get(Owner.class, id));
    }

    @Override
    public List<Owner> findAll() {
        return executeWithoutTransaction(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Owner> query = cb.createQuery(Owner.class);
            Root<Owner> root = query.from(Owner.class);
            root.fetch("cats", JoinType.LEFT);
            query.select(root).distinct(true);
            return session.createQuery(query).getResultList();
        });
    }

    private List<Owner> findBySpecification(Specification<Owner> spec) {
        return executeWithoutTransaction(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Owner> query = cb.createQuery(Owner.class);
            Root<Owner> root = query.from(Owner.class);

            Predicate predicate = spec != null ? spec.toPredicate(root, query, cb) : null;
            if (predicate != null) {
                query.where(predicate);
            }

            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public List<Owner> findByName(String name) {
        checkNotNull(name, "Owner name can't be null");
        Specification<Owner> spec = OwnerSpecifications.nameLike(name);
        return findBySpecification(spec);
    }

    @Override
    public Owner findByCatId(Long catId) {
        checkNotNull(catId, "Cat id can't be null");
        Specification<Owner> spec = OwnerSpecifications.hasCatWithId(catId);
        List<Owner> result = findBySpecification(spec);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Owner> findByCatName(String catName) {
        checkNotNull(catName, "Cat name can't be null");
        Specification<Owner> spec = OwnerSpecifications.hasCatWithName(catName);
        return findBySpecification(spec);
    }
}