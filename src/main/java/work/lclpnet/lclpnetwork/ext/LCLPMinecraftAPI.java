/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.ext;

import work.lclpnet.lclpnetwork.LCLPNetworkAPI;
import work.lclpnet.lclpnetwork.api.APIAccess;
import work.lclpnet.lclpnetwork.facade.MCPlayer;
import work.lclpnet.lclpnetwork.facade.MCUser;
import work.lclpnet.lclpnetwork.facade.User;

import java.util.concurrent.CompletableFuture;

import static work.lclpnet.lclpnetwork.util.JsonBuilder.object;

public class LCLPMinecraftAPI extends LCLPNetworkAPI {

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

}
