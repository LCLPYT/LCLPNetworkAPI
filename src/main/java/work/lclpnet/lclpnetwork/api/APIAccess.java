/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Central API access class.
 */
public class APIAccess {

    protected String host = "https://lclpnet.work";
    protected boolean throwConnectionError = true, throwAuthError = true;
    protected Executor customExecutor = null;

    /**
     * Get the host to which API will be sent to.
     * It should include the protocol, e.g. <code>https://lclpnet.work</code>.
     *
     * @return The host that should be used to exchange API requests.
     */
    @Nonnull
    public String getHost() {
        return this.host;
    }

    @Nullable
    protected String getAccessToken() {
        return null;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean doesThrowConnectionError() {
        return throwConnectionError;
    }

    public void setThrowConnectionError(boolean throwConnectionError) {
        this.throwConnectionError = throwConnectionError;
    }

    public boolean doesThrowAuthError() {
        return throwAuthError;
    }

    public void setThrowAuthError(boolean throwAuthError) {
        this.throwAuthError = throwAuthError;
    }

    public Executor getCustomExecutor() {
        return customExecutor;
    }

    public void setCustomExecutor(Executor customExecutor) {
        this.customExecutor = customExecutor;
    }

    /**
     * Send a HTTP GET API request.
     * @param path The request path for the request.
     * @return A completable future that will contain the APIResponse.
     */
    public CompletableFuture<APIResponse> get(String path) {
        return sendAPIRequest(path, "GET", null);
    }

    /**
     * Send a HTTP POST API request.
     *
     * @param path The request path for the request. E.g. <code>"api/auth/user"</code> for <code>https://lclpnet.work/api/auth/user</code>.
     * @param body Optional HTTP post body. Use <code>null</code> for no body.
     * @return A completable future that will contain the APIResponse.
     */
    public CompletableFuture<APIResponse> post(String path, @Nullable JsonElement body) {
        return sendAPIRequest(path, "POST", body);
    }

    /**
     * Send an API request and optionally receive the result.
     *
     * @param path The request path for the request. E.g. <code>"api/auth/user"</code> for <code>https://lclpnet.work/api/auth/user</code>.
     * @param requestMethod The HTTP request method.
     * @param body Optional HTTP post body. Use <code>null</code> for no body.
     * @return A completable future that will contain the APIResponse.
     */
    public CompletableFuture<APIResponse> sendAPIRequest(String path, String requestMethod, @Nullable JsonElement body) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(requestMethod);

        Supplier<APIResponse> supplier = () -> sendAPIRequestSync(path, requestMethod, body);

        if (this.customExecutor == null) return CompletableFuture.supplyAsync(supplier);
        else return CompletableFuture.supplyAsync(supplier, this.customExecutor);
    }

    public APIResponse sendAPIRequestSync(String path, String requestMethod, @Nullable JsonElement body) throws APIException {
        try {
            URL url = new URL(String.format("%s/%s", this.getHost(), path));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            String accessToken;
            if ((accessToken = this.getAccessToken()) != null)
                conn.setRequestProperty("Authorization", String.format("Bearer %s", accessToken));

            if (body != null) {
                conn.setDoOutput(true);
                try (OutputStream out = conn.getOutputStream()) {
                    out.write(body.toString().getBytes(StandardCharsets.UTF_8));
                    out.flush();
                }
            }

            APIResponse response = APIResponse.fromRequest(conn);

            conn.disconnect();

            if (this.throwAuthError) {
                if(response.isUnauthenticated()) throw APIException.UNAUTHENTICATED;
                else if(response.hasInvalidScopes()) throw APIException.INVALID_SCOPES;
            }

            return response;
        } catch (ConnectException e) {
            if (this.throwConnectionError) throw APIException.NO_CONNECTION;
            return APIResponse.NO_CONNECTION;
        } catch (IOException e) {
            throw new APIException(e);
        }
    }

    /**
     * The main instance of the public LCLPNetwork API.
     */
    public static final APIAccess PUBLIC = new APIAccess();

    /**
     * Tries to login with an oauth access token.
     *
     * @param accessToken An OAuth 2.0 access token, issued by LCLPNetwork.
     * @throws APIException If the login process could not be finished.
     * @return A completable future which will contain the APIAuthAccess instance.
     */
    public static CompletableFuture<APIAuthAccess> withAuth(String accessToken) throws APIException {
        return withAuthCheck(new APIAuthAccess(accessToken));
    }

    /**
     * Tries to login with an oauth access token.
     *
     * @param access An AuthAPIAccess instance.
     * @throws APIException If the login process could not be finished.
     * @return A completable future which will contain the APIAuthAccess instance.
     */
    public static CompletableFuture<APIAuthAccess> withAuthCheck(final APIAuthAccess access) throws APIException {
        return access.isAccessTokenValid().thenApply(valid -> {
            if(valid == null) throw APIException.NO_CONNECTION;
            else if(!valid) throw new APIException("API access token is not valid!");
            else return access;
        });
    }

}
