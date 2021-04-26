/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.ext;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import work.lclpnet.lclpnetwork.LCLPNetworkAPI;
import work.lclpnet.lclpnetwork.api.APIAccess;
import work.lclpnet.lclpnetwork.api.annotation.AuthRequired;
import work.lclpnet.lclpnetwork.api.annotation.Scopes;
import work.lclpnet.lclpnetwork.facade.MCPlayer;
import work.lclpnet.lclpnetwork.facade.MCStats;
import work.lclpnet.lclpnetwork.facade.MCUser;
import work.lclpnet.lclpnetwork.facade.User;
import work.lclpnet.lclpnetwork.util.JsonBuilder;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static work.lclpnet.lclpnetwork.util.JsonBuilder.object;

/**
 * A library of LCLPNetwork Minecraft API requests.
 * @author LCLP
 */
public class LCLPMinecraftAPI extends LCLPNetworkAPI {

    /**
     * The main instance for the public LCLPNetwork Minecraft API.
     */
    public static final LCLPMinecraftAPI INSTANCE = new LCLPMinecraftAPI(APIAccess.PUBLIC);

    /**
     * Construct a new LCLPNetworkAPI object.
     *
     * @param access The API accessor to use.
     */
    public LCLPMinecraftAPI(APIAccess access) {
        super(access);
    }

    /**
     * Fetches a User by MCUser uuid.
     * Will result in null, if nobody linked a minecraft account with that uuid.
     *
     * @param uuid The UUID, with dashes.
     * @return A completable future that will contain the User.
     */
    public CompletableFuture<User> getUserByUUID(String uuid) {
        return getMCUserByUUID(uuid).thenApply(MCUser::getUser);
    }

    /**
     * Fetches a MCUser by UUID.
     * Will result in null, if nobody linked a minecraft account with that uuid.
     *
     * @param uuid The UUID, with dashes.
     * @return A completable future that will contain the MCUser.
     */
    public CompletableFuture<MCUser> getMCUserByUUID(String uuid) {
        return api.post("api/mc/user", object().set("uuid", uuid).createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(MCUser.class);
        });
    }

    /**
     * Fetches a MCUser by LCLPNetwork user id.
     * Will result in null, if nobody with that account id linked a minecraft account
     * or if there is no account with that id.
     *
     * @param userId The user id of the LCLPNetwork user account to fetch the MCUser from.
     * @return A completable future that will contain the MCUser.
     */
    public CompletableFuture<MCUser> getMCUserByUserId(long userId) {
        return api.post("api/mc/user-by-user-id", object().set("userId", userId).createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(MCUser.class);
        });
    }

    /**
     * Fetches a MCPlayer by UUID.
     * Will result in null, if no minecraft account with that uuid is tracked by LCLPNetwork.'.
     *
     * @param uuid The UUID, with dashes.
     * @return A completable future that will contain the MCPlayer.
     */
    public CompletableFuture<MCPlayer> getMCPlayerByUUID(String uuid) {
        return api.post("api/mc/player", object().set("uuid",uuid).createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(MCPlayer.class);
        });
    }

    /**
     * Fetches a MCPlayer by MCPlayer id.
     * Will result in null, if there is no MCPlayer with that id.
     *
     * @param playerId The id of the MCPlayer.
     * @return A completable future that will contain the MCPlayer.
     */
    public CompletableFuture<MCPlayer> getMCPlayerById(long playerId) {
        return api.post("api/mc/player-by-id", object().set("playerId", playerId).createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(MCPlayer.class);
        });
    }

    /**
     * Fetches a MCPlayer by LCLPNetwork user id.
     * Will result in null, if nobody with that account id linked a minecraft account
     * or if there is no account with that id
     * or is currently not tracked as player by LCLPNetwork.
     *
     * @param userId The user id of the LCLPNetwork user account to fetch the MCPlayer from.
     * @return A completable future that will contain the MCPlayer.
     */
    public CompletableFuture<MCPlayer> getMCPlayerByUserId(long userId) {
        return api.post("api/mc/player-by-user-id", object().set("userId", userId).createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(MCPlayer.class);
        });
    }

    /**
     * Fetches MCStats of a tracked MCPlayer (UUID).
     * Will result in null, if no MCPlayer with that UUID is tracked by LCLPNetwork
     * or if no Minecraft account with that UUID exists.
     *
     * @param uuid The UUID of the MCPlayer.
     * @param modules An optional list of stats modules to fetch. Pass null to receive every module.
     * @return A completable future that will contain the MCStats.
     */
    public CompletableFuture<MCStats> getStats(String uuid, @Nullable Iterable<String> modules) {
        JsonBuilder builder = object().set("uuid", uuid);
        if(modules != null) builder = builder.beginArray("modules").addAll(modules).endArray();

        return api.post("api/mc/stats", builder.createObject()).thenApply(resp -> {
            if(resp.getResponseCode() != 200) return null;
            else return resp.getResponseAs(MCStats.class);
        });
    }

    @AuthRequired
    @Scopes("minecraft")
    public CompletableFuture<String> requestMCLinkToken() {
        return api.post("api/mc/request-mclink-token", null).thenApply(resp -> {
            if(resp.getResponseCode() != 201) return null;

            JsonObject obj = resp.getResponseAs(JsonObject.class);
            JsonElement elem = obj.get("token");
            if(elem == null) return null;

            return elem.getAsString();
        });
    }

}
