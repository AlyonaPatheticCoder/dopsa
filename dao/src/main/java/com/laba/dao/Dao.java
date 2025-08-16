package com.laba.dao;
import com.laba.DaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Dao class providing Hibernate session operations.
 *
 * @param <T> entity type
 */
public abstract class Dao<T> {

    protected final SessionFactory sessionFactory;

    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory
     */
    public Dao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected <R> R executeInsideTransaction(Function<Session, R> function) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Transaction failed", e);
        }
    }

    protected void executeInsideTransaction(Consumer<Session> consumer) {
        executeInsideTransaction(session -> {
            consumer.accept(session);
            return null;
        });
    }

    protected  <R> R executeWithoutTransaction(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            return function.apply(session);
        } catch (HibernateException e) {
            throw new DaoException("Session operation failed", e);
        }
    }
}