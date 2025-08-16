package com.laba;

/**
 * Custom exception in Service layer.
 */
public class ServiceException extends RuntimeException {
    /**
     * Constructor for a new ServiceException.
     *
     */
    public ServiceException() {
        super();
    }

    /**
     * Constructor for a new ServiceException with message.
     *
     * @param message the detail message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructor for a new ServiceException with message and cause.
     *
     * @param message the detail message
     * @param cause the og exception
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
