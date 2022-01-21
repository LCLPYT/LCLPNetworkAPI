/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class GsonAccess {

    private static Gson gson = createGsonBuilder().create();

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        GsonAccess.gson = gson;
    }

    public static GsonBuilder createGsonBuilder() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Date.class, new UTCDateAdapter());
    }
}
