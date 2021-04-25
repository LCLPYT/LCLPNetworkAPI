/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.facade;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import work.lclpnet.lclpnetwork.api.APIAccess;
import work.lclpnet.lclpnetwork.ext.LCLPMinecraftAPI;

import java.util.Date;
import java.util.function.Consumer;

public class MCPlayer extends JsonSerializable {

    @Expose
    private long id;
    @Expose
    private String uuid;
    @Expose
    private int points;
    @Expose
    private int coins;
    @Expose
    private String language;
    @Expose
    @SerializedName("last_seen")
    private Date lastSeen;
    @Expose
    @SerializedName("mc_user")
    private MCUser mcUser;

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public int getPoints() {
        return points;
    }

    public int getCoins() {
        return coins;
    }

    public String getLanguage() {
        return language;
    }

    public void fetchMCUser(APIAccess access, Consumer<MCUser> callback) {
        LCLPMinecraftAPI instance = APIAccess.PUBLIC.equals(access) ? LCLPMinecraftAPI.INSTANCE : new LCLPMinecraftAPI(access);
        instance.getMCUserByUUID(this.uuid,  mcUser -> {
            this.mcUser = mcUser;
            callback.accept(this.mcUser);
        });
    }

}