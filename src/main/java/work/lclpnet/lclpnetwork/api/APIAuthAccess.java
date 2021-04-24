/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api;

import java.util.function.Consumer;

public class APIAuthAccess extends APIAccess {

    protected final String accessToken;

    public APIAuthAccess(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected String getAccessToken() {
        return accessToken;
    }

    public void isAccessTokenValid(Consumer<Boolean> callback) {
        this.sendAPIRequest("api/auth", "GET", null,
                resp -> callback.accept(resp.isNoConnection() ? null : resp.getResponseCode() == 200)
        );
    }

}
