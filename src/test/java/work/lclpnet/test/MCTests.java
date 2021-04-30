/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.test;

import org.junit.jupiter.api.Test;
import work.lclpnet.lclpnetwork.api.APIException;
import work.lclpnet.lclpnetwork.ext.LCLPMinecraftAPI;
import work.lclpnet.lclpnetwork.facade.MCStats;

import java.io.IOException;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;

public class MCTests {

    @Test
    void requestMCLinkTokenMissingScopes() throws IOException {
        LCLPMinecraftAPI api = UserTests.getAuth("tokenLocal", "http://localhost:8000", LCLPMinecraftAPI::new);
        assertNotNull(api);

        try {
            api.requestMCLinkToken().thenAccept(System.out::println).join();
            fail("This statement should not be reached.");
        } catch (CompletionException e) {
            if(e.getCause() == null || !(e.getCause() instanceof APIException)) throw e;

            assertEquals(APIException.INVALID_SCOPES, e.getCause());
        }
    }

    @Test
    void requestMCLinkToken() throws IOException {
        LCLPMinecraftAPI api = UserTests.getAuth("tokenLocalMC", "http://localhost:8000", LCLPMinecraftAPI::new);
        assertNotNull(api);
        String token = api.requestMCLinkToken().join();
        assertNotNull(token);
    }

    @Test
    void stats() {
        LCLPMinecraftAPI api = LCLPMinecraftAPI.INSTANCE;
        MCStats stats = api.getStats("7357a549-fa3e-4342-91b2-63e5e73ed39a", null).join();
        assertNotNull(stats);
    }

}
