/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api;

import java.util.concurrent.CompletableFuture;

/**
 * Central Auth API access class.
 */
public class APIAuthAccess extends APIAccess {

    protected final String accessToken;

    public APIAuthAccess(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected String getAccessToken() {
        return accessToken;
    }

    /**
     * Check whether the current access token is valid.
     *
     * @return A completable future that will contain the validity boolean. The boolean may be null if there was a connection error.
     */
    public CompletableFuture<Boolean> isAccessTokenValid() {
        return this.sendAPIRequest("api/auth", "GET", null)
                .thenApply(resp -> resp.getResponseCode() == 200);
    }

}
