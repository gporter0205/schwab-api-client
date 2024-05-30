package com.pangility.schwab.api.client.accountsandtrading;

/**
 * Exception thrown from <em>account</em> endpoint
 */
public class AccountNotFoundException extends Exception {
    /**
     * Constructor for passing a message about the exception
     * @param msg String
     */
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
