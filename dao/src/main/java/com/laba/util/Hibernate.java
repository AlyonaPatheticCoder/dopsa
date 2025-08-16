package com.laba.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for managing Hibernate SessionFactory.
 * Provides methods: set, get, and shutdown.
 */
public class Hibernate {

    private static SessionFactory sessionFactory;

    /**
     * Sets the SessionFactory.
     *
     * @param sf a SessionFactory instance - must not be null
     * @throws IllegalArgumentException - if SessionFactory is null
     */
    public static void setSessionFactory(SessionFactory sf) {
        if (sf == null) {
            throw new IllegalArgumentException("SessionFactory must not be null");
        }
        sessionFactory = sf;
    }

    /**
     * Returns current SessionFactory.
     * If not initialized, a new SessionFactory will be built.
     *
     * @return SessionFactory instance
     * @throws ExceptionInInitializerError - if SessionFactory creation fails
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Hibernate session building failed: " + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    /**
     * Shuts down SessionFactory.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}