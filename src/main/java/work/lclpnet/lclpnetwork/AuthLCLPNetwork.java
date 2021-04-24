/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork;

import java.util.function.Consumer;

public class AuthLCLPNetwork extends LCLPNetwork {

    protected final String accessToken;

    public AuthLCLPNetwork(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected String getAccessToken() {
        return accessToken;
    }

    public void isAccessTokenValid(Consumer<Boolean> callback) {
        this.sendAPIRequest("api/auth/user", "GET", null, resp -> {
            if(resp.isNoConnection()) {
                callback.accept(null);
                return;
            }

            callback.accept(resp.isNoConnection() ? null : resp.getResponseCode() == 200);
        });
    }

}
