package com.laba;

/**
 * Custom exception in DAO layer.
 */
public class DaoException extends RuntimeException {

    /**
     * Constructor for a new DaoException with message.
     *
     * @param message the detail message
     */

    public DaoException(String message) {
        super(message);
    }

    /**
     * Constructor for a new DaoException.
     *
     */
    public DaoException() {
        super();
    }

    /**
     * Constructor for a new DaoException with message and cause.
     *
     * @param message the detail message
     * @param cause the og exception
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}