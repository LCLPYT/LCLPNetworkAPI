/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.facade;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import work.lclpnet.lclpnetwork.LCLPNetworkAPI;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents the LCLPNetwork minecraft statistics of a {@link MCPlayer}.
 */
public class MCStats extends JsonSerializable {

    @Expose
    @SerializedName("schema_version")
    private int schemaVersion;
    @Expose
    private List<Entry> stats;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public List<Entry> getStats() {
        return stats;
    }

    @Nullable
    public Entry getModule(String name) {
        return stats.stream().filter(e -> name.equals(e.name)).findFirst().orElse(null);
    }

    /**
     * Represents a {@link MCStats} statistics entry.
     */
    public static class Entry extends JsonSerializable {

        @Expose
        private EntryType type;
        @Expose
        private String name;
        @Expose
        private String title;
        @Expose
        private Icon icon;
        @Expose
        private Map<String, Value> properties;
        @Expose
        private Map<String, JsonElement> extra;
        @Expose
        private List<Entry> children;

        public EntryType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public Icon getIcon() {
            return icon;
        }

        public Map<String, Value> getProperties() {
            return properties;
        }

        public Map<String, JsonElement> getExtra() {
            return extra;
        }

        public List<Entry> getChildren() {
            return children;
        }
    }

    /**
     * Specifies the type of an {@link Entry}
     */
    public enum EntryType {

        @SerializedName("general")
        GENERAL,
        @SerializedName("game")
        GAME,
        @SerializedName("group")
        GROUP;

    }

    /**
     * Represents the value of an {@link Entry}.
     */
    public static class Value extends JsonSerializable {

        @Expose
        private ValueType type;
        @Expose
        private JsonElement value;

        public ValueType getType() {
            return type;
        }

        public JsonElement getValue() {
            return value;
        }

        public boolean isNull() {
            return value == null || value.isJsonNull();
        }

        public int getAsInt() {
            return isNull() ? 0 : value.getAsInt();
        }

        public Date getAsDate() {
            return isNull() ? null : LCLPNetworkAPI.GSON.fromJson(value, Date.class);
        }

        public String getValueAsFormattedString() {
            if(type == ValueType.INTEGER) return String.valueOf(getAsInt());
            else if(type == ValueType.DATE) {
                Date d = getAsDate();
                return d == null ? "Never" : d.toString();
            }
            else throw new IllegalStateException(String.format("Unimplemented type '%s'.", type));
        }

    }

    /**
     * Specifies the value type of an {@link Entry}.
     */
    public enum ValueType {

        @SerializedName("int")
        INTEGER,
        @SerializedName("date")
        DATE;

    }

    /**
     * Used to determine the icon of an {@link Entry}.
     */
    public static class Icon {

        @Expose
        private String minecraft;

        public String getMinecraft() {
            return minecraft;
        }
    }

}
