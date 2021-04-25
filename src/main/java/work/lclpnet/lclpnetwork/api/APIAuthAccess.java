/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api;

import java.util.concurrent.CompletableFuture;

public class APIAuthAccess extends APIAccess {

    protected final String accessToken;

    public APIAuthAccess(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected String getAccessToken() {
        return accessToken;
    }

    public CompletableFuture<Boolean> isAccessTokenValid() {
        return this.sendAPIRequest("api/auth", "GET", null)
                .thenApply(resp -> resp.isNoConnection() ? null : resp.getResponseCode() == 200);
    }

}
