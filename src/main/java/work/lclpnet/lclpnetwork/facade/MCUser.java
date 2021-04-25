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
 * Represents a Minecraft LCLPNetwork user, who linked his minecraft account with his LCLPNetwork account.
 */
public class MCUser extends JsonSerializable {

    @Expose
    @SerializedName("user_id")
    private long userId;
    @Expose
    private String uuid;
    @Expose
    @SerializedName("created_at")
    private Date createdAt;
    @Expose
    @SerializedName("updated_at")
    private Date updatedAt;
    @Expose
    private User user;
    @Expose
    private MCPlayer player;

    public long getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public MCPlayer getPlayer() {
        return player;
    }

    public CompletableFuture<User> fetchUser(APIAccess access) {
        LCLPMinecraftAPI instance = APIAccess.PUBLIC.equals(access) ? LCLPMinecraftAPI.INSTANCE : new LCLPMinecraftAPI(access);
        return instance.getUserByUUID(this.uuid).thenApply(user -> this.user = user);
    }

    public CompletableFuture<MCPlayer> fetchPlayer(APIAccess access) {
        LCLPMinecraftAPI instance = APIAccess.PUBLIC.equals(access) ? LCLPMinecraftAPI.INSTANCE : new LCLPMinecraftAPI(access);
        return instance.getMCPlayerByUUID(this.uuid).thenApply(mcPlayer -> this.player = mcPlayer);
    }

}
