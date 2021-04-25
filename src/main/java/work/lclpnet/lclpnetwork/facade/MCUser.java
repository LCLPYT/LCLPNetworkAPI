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

    public void fetchUser(APIAccess access, Consumer<User> callback) {
        LCLPMinecraftAPI instance = APIAccess.PUBLIC.equals(access) ? LCLPMinecraftAPI.INSTANCE : new LCLPMinecraftAPI(access);
        instance.getUserByUUID(this.uuid,  user -> {
            this.user = user;
            callback.accept(this.user);
        });
    }

    public void fetchPlayer(APIAccess access, Consumer<MCPlayer> callback) {
        LCLPMinecraftAPI instance = APIAccess.PUBLIC.equals(access) ? LCLPMinecraftAPI.INSTANCE : new LCLPMinecraftAPI(access);
        instance.getMCPlayerByUUID(this.uuid,  mcPlayer -> {
            this.player = mcPlayer;
            callback.accept(this.player);
        });
    }

}