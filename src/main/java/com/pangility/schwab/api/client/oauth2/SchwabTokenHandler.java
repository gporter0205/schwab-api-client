package com.pangility.schwab.api.client.oauth2;

import org.jetbrains.annotations.NotNull;

/**
 * Interface implemented by the client when needing a callback from
 * {@link SchwabOauth2Controller} when either the refresh or access
 * tokens are changed by the controller.
 */
public interface SchwabTokenHandler {
    /**
     * method triggered when the <em>access</em> token changes.
     * @param schwabAccount {@link SchwabAccount}
     */
    void onAccessTokenChange(@NotNull SchwabAccount schwabAccount);
    /**
     * method triggered when the <em>refresh</em> token changes.
     * @param schwabAccount {@link SchwabAccount}
     */
    void onRefreshTokenChange(@NotNull SchwabAccount schwabAccount);
}
