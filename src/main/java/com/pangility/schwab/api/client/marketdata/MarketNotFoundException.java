package com.pangility.schwab.api.client.marketdata;

/**
 * Exception thrown from <em>markets</em> endpoint
 */
public class MarketNotFoundException extends Exception {
    /**
     * Constructor for passing a message about the exception
     * @param msg String
     */
    public MarketNotFoundException(String msg) {
        super(msg);
    }
}
