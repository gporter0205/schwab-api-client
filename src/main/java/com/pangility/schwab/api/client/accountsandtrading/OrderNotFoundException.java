package com.pangility.schwab.api.client.accountsandtrading;

/**
 * Exception thrown from <em>order</em> endpoints
 */
public class OrderNotFoundException extends Exception {
    /**
     * Constructor for passing a message about the exception
     * @param msg String
     */
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
