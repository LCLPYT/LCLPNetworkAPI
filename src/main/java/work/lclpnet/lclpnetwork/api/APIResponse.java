/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import work.lclpnet.lclpnetwork.facade.JsonSerializable;
import work.lclpnet.lclpnetwork.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class APIResponse {

    private final int responseCode;
    private final String rawResponse, rawError;
    private APIError validationViolations = null;
    private String jsonStatusMessage = null;

    public APIResponse(int responseCode, String rawResponse, String rawError) {
        this.responseCode = responseCode;
        this.rawResponse = rawResponse;
        this.rawError = rawError;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public String getRawError() {
        return rawError;
    }

    public static APIResponse fromRequest(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();

        String response;
        try (InputStream in = conn.getInputStream()) {
            response = Utils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException e) {
            response = null;
        }

        String error;
        try (InputStream inErr = conn.getErrorStream()) {
            error = Utils.toString(inErr, StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException e) {
            error = null;
        }

        return new APIResponse(status, response, error);
    }

    public boolean hasJsonStatusMessage() {
        return getJsonStatusMessage() != null;
    }

    public String getJsonStatusMessage() {
        if (jsonStatusMessage != null) return jsonStatusMessage;

        String s = rawError != null ? rawError : (rawResponse);
        if (s == null) return null;

        JsonObject json;
        try {
            json = JsonSerializable.parse(s, JsonObject.class);
        } catch (JsonSyntaxException e) {
            return null;
        }

        JsonElement elem = json.get("message");
        if (elem == null) return null;

        try {
            jsonStatusMessage = elem.getAsString();
            return jsonStatusMessage;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public boolean isUnauthenticated() {
        return responseCode == 401 && "{\"message\":\"Unauthenticated.\"}".equals(rawError);
    }

    public boolean hasInvalidScopes() {
        if(responseCode != 403) return false;

        JsonObject obj = this.getErrorAs(JsonObject.class);
        JsonElement elem = obj.get("message");
        return "Invalid scope(s) provided.".equals(elem.getAsString());
    }

    public boolean hasValidationViolations() {
        return getValidationViolations() != null && !validationViolations.getViolations().isEmpty();
    }

    public APIError getValidationViolations() {
        if (validationViolations != null) return validationViolations;

        if (rawError == null) return null;

        JsonObject json;
        try {
            json = JsonSerializable.parse(rawError, JsonObject.class);
        } catch (JsonSyntaxException e) {
            return null;
        }

        JsonElement elem = json.get("errors");
        if (elem == null || !elem.isJsonObject()) return null;

        List<ElementError> elemErrors = new ArrayList<>();

        JsonObject obj = elem.getAsJsonObject();
        obj.entrySet().forEach(e -> {
            List<String> errors = new ArrayList<>();
            if (e.getValue().isJsonArray()) {
                List<JsonElement> elements = new ArrayList<>();
                e.getValue().getAsJsonArray().forEach(elements::add);
                elements.forEach(eElem -> {
                    try {
                        errors.add(eElem.getAsString());
                    } catch (ClassCastException ignored) {
                    }
                });
            }
            elemErrors.add(new ElementError(e.getKey(), errors));
        });

        return (validationViolations = new APIError(elemErrors));
    }

    public <T> T getExtra(Class<T> clazz) {
        JsonObject obj = JsonSerializable.parse(rawResponse, JsonObject.class);
        JsonElement elem = obj.get("extra");
        if (elem == null || elem.isJsonNull()) return null;
        else return JsonSerializable.cast(elem.getAsJsonObject(), clazz);
    }

    public <T> T getResponseAs(Class<T> clazz) {
        return JsonSerializable.parse(rawResponse, clazz);
    }

    public <T> T getErrorAs(Class<T> clazz) {
        return JsonSerializable.parse(rawError, clazz);
    }

    @Override
    public String toString() {
        return String.format("HTTPResponse{responseCode=%d, rawResponse='%s', rawError='%s'}", responseCode, rawResponse, rawError);
    }
}
