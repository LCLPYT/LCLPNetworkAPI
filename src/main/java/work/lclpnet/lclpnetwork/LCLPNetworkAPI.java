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

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

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
     * @param callback A callback that receives the fetched user.
     */
    public void getUserById(int id, Consumer<User> callback) {
        api.post("api/auth/user-by-id", object().set("userId", id).createObject(), resp -> {
            if(resp.getResponseCode() != 200) callback.accept(null);
            else callback.accept(resp.getExtra(User.class));
        });
    }

    /**
     * Revokes the current access token.
     * Similar to a logout.
     *
     * @param callback An optional callback which receives if the revoke was successful.
     */
    @AuthRequired
    @Scopes("revoke-self")
    public void revokeCurrentToken(@Nullable Consumer<Boolean> callback) {
        api.get("api/auth/revoke-token", callback == null ? null : resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(false);
                return;
            }

            callback.accept("{\"message\":\"Successfully logged out\"}".equals(resp.getRawResponse()));
        });
    }

    /**
     * Get the current user.
     * If the access token has the scope "identity[email]",
     * the user object will contain the email and verification date.
     *
     * @param callback A callback that receives the fetched user.
     */
    @AuthRequired
    @Scopes("identity")
    public void getCurrentUser(Consumer<User> callback) {
        api.get("api/auth/user", resp -> {
            if(resp.getResponseCode() != 200) callback.accept(null);
            else callback.accept(resp.getResponseAs(User.class));
        });
    }

    /**
     * Check, whether the current user has verified his email.
     *
     * @param callback A callback that will receive the result.
     */
    @AuthRequired
    @Scopes("identity")
    public void isCurrentUserVerified(Consumer<Boolean> callback) {
        api.get("api/auth/verified", resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(null);
                return;
            }

            JsonObject obj = resp.getResponseAs(JsonObject.class);
            JsonElement elem = obj.get("email_verified");
            if(elem == null) callback.accept(null);
            else callback.accept(elem.getAsBoolean());
        });
    }

}
