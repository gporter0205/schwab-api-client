package com.pangility.schwab.api.client.accountsandtrading;

/**
 * Exception thrown from <em>transaction</em> endpoints
 */
public class TransactionNotFoundException extends Exception {
    /**
     * Constructor for passing a message about the exception
     * @param msg String
     */
    public TransactionNotFoundException(String msg) {
        super(msg);
    }
}
