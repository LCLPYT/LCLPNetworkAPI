/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.facade;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import static work.lclpnet.lclpnetwork.LCLPNetworkAPI.GSON;

public class JsonSerializable {

	protected transient Gson gson = GSON;

	@Override
	public String toString() {
		return stringify(this, this.gson);
	}
	
	public JsonElement toJson() {
		return JsonSerializable.toJson(this, this.gson);
	}

	public static JsonElement toJson(Object o) {
		return toJson(o, GSON);
	}

	public static JsonElement toJson(Object o, Gson gson) {
		return gson.toJsonTree(o);
	}

	public static String stringify(Object o) {
		return stringify(o, GSON);
	}

	public static String stringify(Object o, Gson gson) {
		return gson.toJson(o);
	}

	public static <T> T parse(String s, Class<T> clazz) {
		return parse(s, clazz, GSON);
	}

	public static <T> T parse(String s, Class<T> clazz, Gson gson) {
		return gson.fromJson(s, clazz);
	}

	public static <T> T cast(JsonElement elem, Class<T> clazz) {
		return cast(elem, clazz, GSON);
	}

	public static <T> T cast(JsonElement elem, Class<T> clazz, Gson gson) {
		return gson.fromJson(elem, clazz);
	}

}
