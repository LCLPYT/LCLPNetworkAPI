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
import java.util.concurrent.CompletableFuture;

/**
 * Represents a minecraft player who has played once on a LCLPNetwork minecraft server.
 */
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

    public CompletableFuture<MCUser> fetchMCUser(APIAccess access) {
        LCLPMinecraftAPI instance = APIAccess.PUBLIC.equals(access) ? LCLPMinecraftAPI.INSTANCE : new LCLPMinecraftAPI(access);
        return instance.getMCUserByUUID(this.uuid).thenApply(mcUser -> this.mcUser = mcUser);
    }

}
