package com.pangility.schwab.api.client.marketdata;

/**
 * Exception thrown from various endpoints
 */
public class SymbolNotFoundException extends Exception {
    /**
     * Constructor for passing a message about the exception
     * @param msg String
     */
    public SymbolNotFoundException(String msg) {
        super(msg);
    }
}
