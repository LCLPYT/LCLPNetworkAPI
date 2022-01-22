/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.util;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class JsonDateAdapter extends TypeAdapter<Date> {

    private final SimpleDateFormat format;

    public JsonDateAdapter(SimpleDateFormat format) {
        this.format = Objects.requireNonNull(format);
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if(value == null) out.nullValue();
        else out.value(format.format(value));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL) return null;
        try {
            return format.parse(in.nextString());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * A date adapter that will serialize / deserialize with the "year-month-day hour:minute:second" (yyyy-MM-dd HH:mm:ss) format.
     */
    public static class YMDHMS extends JsonDateAdapter {

        public YMDHMS() {
            super(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
    }

}
