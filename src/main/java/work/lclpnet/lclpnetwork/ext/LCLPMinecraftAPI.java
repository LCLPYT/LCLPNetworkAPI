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

import java.util.function.Consumer;

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
     * @param callback A callback that receives the fetched User.
     */
    public void getUserByUUID(String uuid, Consumer<User> callback) {
        getMCUserByUUID(uuid,mcUser -> callback.accept(mcUser.getUser()));
    }

    /**
     * Fetches a MCUser by UUID.
     * Will result in null, if nobody linked a minecraft account with that uuid.
     *
     * @param uuid The UUID, with dashes.
     * @param callback A callback that receives the fetched MCUser.
     */
    public void getMCUserByUUID(String uuid, Consumer<MCUser> callback) {
        api.post("api/mc/user", object().set("uuid", uuid).createObject(), resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(null);
                return;
            }

            callback.accept(resp.getResponseAs(MCUser.class));
        });
    }

    /**
     * Fetches a MCUser by LCLPNetwork user id.
     * Will result in null, if nobody with that account id linked a minecraft account
     * or if there is no account with that id.
     *
     * @param userId The user id of the LCLPNetwork user account to fetch the MCUser from.
     * @param callback A callback that receives the fetched MCUser.
     */
    public void getMCUserByUserId(long userId, Consumer<MCUser> callback) {
        api.post("api/mc/user-by-user-id", object().set("userId", userId).createObject(), resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(null);
                return;
            }

            callback.accept(resp.getResponseAs(MCUser.class));
        });
    }

    /**
     * Fetches a MCPlayer by UUID.
     * Will result in null, if no minecraft account with that uuid is tracked by LCLPNetwork.'.
     *
     * @param uuid The UUID, with dashes.
     * @param callback A callback that receives the fetched MCPlayer.
     */
    public void getMCPlayerByUUID(String uuid, Consumer<MCPlayer> callback) {
        api.post("api/mc/player", object().set("uuid",uuid).createObject(), resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(null);
                return;
            }

            callback.accept(resp.getResponseAs(MCPlayer.class));
        });
    }

    /**
     * Fetches a MCPlayer by MCPlayer id.
     * Will result in null, if there is no MCPlayer with that id.
     *
     * @param playerId The id of the MCPlayer.
     * @param callback A callback that receives the fetched MCPlayer.
     */
    public void getMCPlayerById(long playerId, Consumer<MCPlayer> callback) {
        api.post("api/mc/player-by-id", object().set("playerId", playerId).createObject(), resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(null);
                return;
            }

            callback.accept(resp.getResponseAs(MCPlayer.class));
        });
    }

    /**
     * Fetches a MCPlayer by LCLPNetwork user id.
     * Will result in null, if nobody with that account id linked a minecraft account
     * or if there is no account with that id
     * or is currently not tracked as player by LCLPNetwork.
     *
     * @param userId The user id of the LCLPNetwork user account to fetch the MCPlayer from.
     * @param callback A callback that receives the fetched MCPlayer.
     */
    public void getMCPlayerByUserId(long userId, Consumer<MCPlayer> callback) {
        api.post("api/mc/player-by-user-id", object().set("userId", userId).createObject(), resp -> {
            if(resp.getResponseCode() != 200) {
                callback.accept(null);
                return;
            }

            callback.accept(resp.getResponseAs(MCPlayer.class));
        });
    }

}
