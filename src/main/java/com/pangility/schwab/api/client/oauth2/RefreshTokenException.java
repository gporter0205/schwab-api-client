package com.pangility.schwab.api.client.oauth2;

import lombok.Getter;

/**
 * Exception thrown by {@link SchwabOauth2Controller} when the refresh token has expired.
 * This exception should trigger the flow getting users to grant API permissions for obtaining
 * a new refresh token from the Schwab API.
 */
@Getter
public class RefreshTokenException extends RuntimeException {
    /**
     * property to keep {@link SchwabAccount}
     */
    private final SchwabAccount schwabAccount;

    /**
     * Constructor for passing a message and information about the exception
     * @param msg String
     * @param schwabAccount {@link SchwabAccount}
     */
    public RefreshTokenException(String msg, SchwabAccount schwabAccount) {
        super(msg);
        this.schwabAccount = schwabAccount;
    }
}
