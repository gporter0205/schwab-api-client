package com.pangility.schwab.api.client.oauth2;

import org.jetbrains.annotations.NotNull;

public interface SchwabTokenHandler {
    void onAccessTokenChange(@NotNull SchwabAccount schwabAccount);
    void onRefreshTokenChange(@NotNull SchwabAccount schwabAccount);
}
