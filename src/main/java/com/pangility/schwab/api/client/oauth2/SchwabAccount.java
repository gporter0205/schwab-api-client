package com.pangility.schwab.api.client.oauth2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Model class for passing tokens and expirations for handling various oauth2 requests.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SchwabAccount {
    private String userId;
    private String refreshToken;
    private String accessToken;
    private LocalDateTime refreshExpiration;
    private LocalDateTime accessExpiration;
}
