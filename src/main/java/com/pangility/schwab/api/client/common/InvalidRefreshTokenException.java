package com.pangility.schwab.api.client.common;

import com.pangility.schwab.api.client.oauth2.SchwabAccount;
import lombok.Getter;

@Getter
public class InvalidRefreshTokenException extends RuntimeException {
    private final SchwabAccount schwabAccount;
    public InvalidRefreshTokenException(String msg, SchwabAccount schwabAccount) {
        super(msg);
        this.schwabAccount = schwabAccount;
    }
}
