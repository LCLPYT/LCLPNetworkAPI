/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import work.lclpnet.lclpnetwork.api.APIAccess;
import work.lclpnet.lclpnetwork.api.annotation.AuthRequired;
import work.lclpnet.lclpnetwork.api.annotation.Scopes;
import work.lclpnet.lclpnetwork.facade.User;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static work.lclpnet.lclpnetwork.util.JsonBuilder.object;

/**
 * A library of LCLPNetwork API requests.
 * @author LCLP
 */
public class LCLPNetworkAPI {

    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            .create();

    /**
     * The main instance for the public LCLPNetwork API.
     */
    public static final LCLPNetworkAPI INSTANCE = new LCLPNetworkAPI(APIAccess.PUBLIC);

    /* */

    protected final APIAccess api;

    /**
     * Construct a new LCLPNetworkAPI object.
     * @param access The API accessor to use.
     */
    public LCLPNetworkAPI(APIAccess access) {
        this.api = Objects.requireNonNull(access);
    }

    /**
     * Gets a user by id.
     *
     * @param id The id of the user.
     * @return A completable future that will contain the User.
     */
    public CompletableFuture<User> getUserById(int id) {
        return api.post("api/auth/user-by-id", object().set("userId", id).createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getExtra(User.class);
        });
    }

    /**
     * Revokes the current access token.
     * Similar to a logout.
     *
     * @return A completable future that will contain the success result.
     */
    @AuthRequired
    @Scopes("revoke-self")
    public CompletableFuture<Boolean> revokeCurrentToken() {
        return api.get("api/auth/revoke-token").thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return "{\"message\":\"Successfully logged out\"}".equals(resp.getRawResponse());
        });
    }

    /**
     * Get the current user.
     * If the access token has the scope "identity[email]",
     * the user object will contain the email and verification date.
     *
     * @return A completable future that will contain the User.
     */
    @AuthRequired
    @Scopes("identity")
    public CompletableFuture<User> getCurrentUser() {
        return api.get("api/auth/user").thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(User.class);
        });
    }

    /**
     * Check, whether the current user has verified his email.
     *
     * @return A completable future that will contain, whether the current user is logged in.
     */
    @AuthRequired
    @Scopes("identity")
    public CompletableFuture<Boolean> isCurrentUserVerified() {
        return api.get("api/auth/verified").thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;

            JsonObject obj = resp.getResponseAs(JsonObject.class);
            JsonElement elem = obj.get("email_verified");
            if(elem == null) return null;
            else return elem.getAsBoolean();
        });
    }

}
