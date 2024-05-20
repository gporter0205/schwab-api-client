package com.pangility.schwab.api.client.marketdata;

/**
 * Exception thrown from various endpoints
 */
public class IndexNotFoundException extends Exception {

    /**
     * Constructor for passing a message about the exception
     * @param msg String
     */
    public IndexNotFoundException(String msg) {
        super(msg);
    }
}
