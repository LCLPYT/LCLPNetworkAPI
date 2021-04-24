/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork;

import com.google.gson.JsonElement;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import work.lclpnet.lclpnetwork.network.APIResponse;
import work.lclpnet.lclpnetwork.util.APIException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

import static work.lclpnet.lclpnetwork.util.APIException.NO_CONNECTION;

/**
 * Central API access class.
 */
public class LCLPNetwork {

    protected String host = "https://lclpnet.work";

    /**
     * Get the host to which API will be sent to.
     * It should include the protocol, e.g. <code>https://lclpnet.work</code>.
     *
     * @return The host that should be used to exchange API requests.
     */
    @NotNull
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

    /**
     * Send a HTTP GET API request.
     * @param path The request path for the request.
     * @param callback An optional callback which will receive an APIResponse object. Use <code>null</code> to ignore the response.
     */
    public void get(String path, @Nullable Consumer<APIResponse> callback) {
        sendAPIRequest(path, "GET", null, callback);
    }

    /**
     * Send a HTTP POST API request.
     *
     * @param path The request path for the request. E.g. <code>"api/auth/user"</code> for <code>https://lclpnet.work/api/auth/user</code>.
     * @param body Optional HTTP post body. Use <code>null</code> for no body.
     * @param callback An optional callback which will receive an APIResponse object. Use <code>null</code> to ignore the response.
     */
    public void post(String path, @Nullable JsonElement body, @Nullable Consumer<APIResponse> callback) {
        sendAPIRequest(path, "POST", body, callback);
    }

    /**
     * Send an API request and optionally receive the result.
     *
     * @param path The request path for the request. E.g. <code>"api/auth/user"</code> for <code>https://lclpnet.work/api/auth/user</code>.
     * @param requestMethod The HTTP request method.
     * @param body Optional HTTP post body. Use <code>null</code> for no body.
     * @param callback An optional callback which will receive an APIResponse object. Use <code>null</code> to ignore the response.
     */
    public void sendAPIRequest(String path, String requestMethod, @Nullable JsonElement body, @Nullable Consumer<APIResponse> callback) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(requestMethod);

        new Thread(() -> {
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

                if (callback != null) callback.accept(response);
            } catch (ConnectException e) {
                if (callback != null) callback.accept(APIResponse.NO_CONNECTION);
            } catch (IOException e) {
                throw new APIException(e);
            }
        }, "API Request").start();
    }

    /**
     * The main instance of the public LCLPNetwork API.
     */
    public static final LCLPNetwork INSTANCE = new LCLPNetwork();

    /**
     * Tries to login with an oauth access token.
     *
     * @param accessToken An OAuth 2.0 access token, issued by LCLPNetwork.
     * @param callback AuthLCLPNetwork object receiver. Will get null if there was an error.
     * @throws APIException If the login process could not be finished.
     */
    public static void withAuth(String accessToken, Consumer<AuthLCLPNetwork> callback) throws APIException {
        AuthLCLPNetwork api = new AuthLCLPNetwork(accessToken);
        api.isAccessTokenValid(valid -> {
            if(valid == null) {
                callback.accept(null);
                throw NO_CONNECTION;
            }
            else if(!valid) {
                callback.accept(null);
                throw new APIException("API access token is not valid!");
            }
            else callback.accept(api);
        });
    }

}
