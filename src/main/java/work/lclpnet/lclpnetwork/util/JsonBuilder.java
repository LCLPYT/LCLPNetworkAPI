/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.util;

import com.google.gson.*;
import work.lclpnet.lclpnetwork.LCLPNetworkAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A builder class that can create JsonObjects or JsonArrays
 * @author LCLP
 */
public class JsonBuilder {

    protected final Gson gson;
    protected final JsonBuilder parent;
    protected String currentChildKey = null;
    protected final Map<String, JsonElement> properties;
    protected final List<JsonElement> elements;

    protected JsonBuilder(Gson gson, JsonBuilder parent, boolean array) {
        this.gson = gson;
        this.parent = parent;
        this.properties = array ? null : new HashMap<>();
        this.elements = array ? new ArrayList<>() : null;
    }

    /**
     * Begin a new JsonObject.
     * This method should be used on JsonArray builders.
     * @return A new object child builder.
     */
    public JsonBuilder beginObject() {
        ensureArray();
        return beginObject(null);
    }

    /**
     * Begin a new JsonObject.
     * This method should be used on JsonObject builders.
     * @param key The key of the finished JsonObject for this JsonObject builder.
     * @return A new object child builder.
     */
    public JsonBuilder beginObject(String key) {
        return begin(key, false);
    }

    /**
     * End the current JsonObject.
     * This should only be called on JsonObject child builders.
     * @return The parent builder.
     */
    public JsonBuilder endObject() {
        ensureObject();
        if (this.parent == null) throw new IllegalStateException("endObject() might only be called on child builders.");
        return end(this.createObject());
    }

    /**
     * Begin a new JsonArray.
     * This method should be used on JsonArray builders.
     * @return A new child array builder.
     */
    public JsonBuilder beginArray() {
        ensureArray();
        return beginArray(null);
    }

    /**
     * Begin a new JsonArray.
     * This method should be used on JsonObject builders.
     * @param key The key of the finished JsonArray for this JsonObject builder.
     * @return A new child array builder.
     */
    public JsonBuilder beginArray(String key) {
        return begin(key, true);
    }

    /**
     * End the current JsonArray.
     * This should only be called on JsonArray child builders.
     * @return The parent builder.
     */
    public JsonBuilder endArray() {
        ensureArray();
        if (this.parent == null) throw new IllegalStateException("endArray() might only be called on child builders.");
        return end(this.createArray());
    }

    protected JsonBuilder begin(String key, boolean array) {
        if(key == null && properties != null) throw new IllegalArgumentException("key must not be null on object builders.");
        else if(key != null && elements != null) throw new IllegalArgumentException("key must be null on array builders.");
        this.currentChildKey = key;
        return new JsonBuilder(gson, this,array);
    }

    protected JsonBuilder end(JsonElement json) {
        JsonBuilder builder;
        if(this.parent.currentChildKey == null) builder = this.parent.add(json);
        else builder = this.parent.set(this.parent.currentChildKey, json);
        this.parent.currentChildKey = null;
        return builder;
    }

    /**
     * Sets a property of the current JsonObject.
     * This should only be called on JsonObject builders.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @return The same builder.
     */
    public JsonBuilder set(String key, Object value) {
        ensureObject();
        if(value == null) properties.put(key, JsonNull.INSTANCE);
        else properties.put(key, gson.toJsonTree(value));
        return this;
    }

    /**
     * Creates a finished JsonObject out of this builder.
     * This should only be used on JsonObject root builders.
     *
     * @return The finished JsonObject.
     */
    public JsonObject createObject() {
        ensureObject();
        JsonObject obj = new JsonObject();
        properties.forEach(obj::add);
        return obj;
    }

    /**
     * Adds an element to the current JsonArray.
     * This should only be called on JsonArray builders.
     *
     * @param element The element to add.
     * @return The same builder.
     */
    public JsonBuilder add(Object element) {
        ensureArray();
        if(element == null) elements.add(JsonNull.INSTANCE);
        else elements.add(gson.toJsonTree(element));
        return this;
    }

    /**
     * Adds all the provided elements to the current JsonArray.
     * This should only be called on JsonArray builders.
     *
     * @param elements A list of elements to add.
     * @return The same builder.
     */
    public JsonBuilder addAll(Iterable<?> elements) {
        ensureArray();
        if(elements != null) elements.forEach(this::add);
        return this;
    }

    /**
     * Creates a finished JsonArray out of this builder.
     * This should only be used on JsonArray root builders.
     *
     * @return The finished JsonArray.
     */
    public JsonArray createArray() {
        ensureArray();
        JsonArray array = new JsonArray();
        elements.forEach(array::add);
        return array;
    }

    protected void ensureObject() {
        if(properties == null) throw new IllegalStateException("Builder's type is not object.");
    }

    protected void ensureArray() {
        if(elements == null) throw new IllegalStateException("Builder's type is not array.");
    }

    /* static methods */

    /**
     * Create a new JsonObject builder using the {@link LCLPNetworkAPI#GSON} Gson instance.
     * @return A new JsonObject builder with the default Gson instance.
     */
    public static JsonBuilder object() {
        return object(LCLPNetworkAPI.GSON);
    }

    /**
     * Create a new JsonObject builder using a custom Gson instance.
     * @param gson A Gson instance that should be used for serialization.
     * @return A new JsonObject builder.
     */
    public static JsonBuilder object(Gson gson) {
        return new JsonBuilder(gson, null,false);
    }

    /**
     * Create a new JsonArray builder using the {@link LCLPNetworkAPI#GSON} Gson instance.
     * @return A new JsonArray builder with the default Gson instance.
     */
    public static JsonBuilder array() {
        return array(LCLPNetworkAPI.GSON);
    }

    /**
     * Create a new JsonArray builder using a custom Gson instance.
     * @param gson A Gson instance that should be used for serialization.
     * @return A new JsonArray builder.
     */
    public static JsonBuilder array(Gson gson) {
        return new JsonBuilder(gson, null, true);
    }

}