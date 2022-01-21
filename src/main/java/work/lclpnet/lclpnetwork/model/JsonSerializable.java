/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import work.lclpnet.lclpnetwork.util.GsonAccess;

/**
 * Objects extending this class will be JSON-serialized with their toString() methods.
 * Additionally, they will have all the helper methods to serialize / deserialize available.
 */
public class JsonSerializable {

	@Override
	public String toString() {
		return stringify(this, GsonAccess.getGson());
	}
	
	public JsonElement toJson() {
		return JsonSerializable.toJson(this, GsonAccess.getGson());
	}

	public static JsonElement toJson(Object o) {
		return toJson(o, GsonAccess.getGson());
	}

	public static JsonElement toJson(Object o, Gson gson) {
		return gson.toJsonTree(o);
	}

	public static String stringify(Object o) {
		return stringify(o, GsonAccess.getGson());
	}

	public static String stringify(Object o, Gson gson) {
		return gson.toJson(o);
	}

	public static <T> T parse(String s, Class<T> clazz) {
		return parse(s, clazz, GsonAccess.getGson());
	}

	public static <T> T parse(String s, Class<T> clazz, Gson gson) {
		return gson.fromJson(s, clazz);
	}

	public static <T> T cast(JsonElement elem, Class<T> clazz) {
		return cast(elem, clazz, GsonAccess.getGson());
	}

	public static <T> T cast(JsonElement elem, Class<T> clazz, Gson gson) {
		return gson.fromJson(elem, clazz);
	}

}
