package com.schwabapi.oauth2;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SchwabAccount {
    private String userId;
    private String refreshToken;
    private String accessToken;
    private LocalDateTime refreshExpiration;
    private LocalDateTime accessExpiration;
}
