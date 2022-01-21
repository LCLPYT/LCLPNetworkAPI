/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.test;

import org.junit.jupiter.api.Test;
import work.lclpnet.lclpnetwork.LCLPNetworkAPI;
import work.lclpnet.lclpnetwork.api.APIAccess;
import work.lclpnet.lclpnetwork.api.APIAuthAccess;
import work.lclpnet.lclpnetwork.api.APIException;
import work.lclpnet.lclpnetwork.model.User;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    @Test
    void dummy() {
        assertEquals(1, 1);
    }

    @Test
    void userById() {
        User user = LCLPNetworkAPI.INSTANCE.getUserById(1).join();
        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    void userByIdNotExisting() {
        User user = LCLPNetworkAPI.INSTANCE.getUserById(Integer.MAX_VALUE).join();
        assertNull(user);
    }

    @Test
    void currentUserNoAuth() {
        try {
            LCLPNetworkAPI.INSTANCE.getCurrentUser().thenAccept(System.out::println).join();
            fail("This test should not reach this statement");
        } catch (CompletionException e) {
            if(e.getCause() == null || !(e.getCause() instanceof APIException)) throw e;

            // We did not use authentication. Therefore this test tests if the exception throwing is successful.
            assertEquals(APIException.UNAUTHENTICATED, e.getCause());
        }
    }

    @Test
    void revokeTokenMissingScopes() throws IOException {
        LCLPNetworkAPI auth = getAuth();
        assertNotNull(auth);

        try {
            auth.revokeCurrentToken().thenAccept(System.out::println).join();
            fail("This test should not reach this statement");
        } catch (CompletionException e) {
            if(e.getCause() == null || !(e.getCause() instanceof APIException)) throw e;

            // The dummy token is missing the 'revoke-self' scope, therefore, an exception will be thrown.
            assertEquals(APIException.INVALID_SCOPES, e.getCause());
        }
    }

    @Test
    void currentUser() throws IOException {
        LCLPNetworkAPI auth = getAuth();
        assertNotNull(auth);

        User user = auth.getCurrentUser().join();
        assertNotNull(user);
        assertEquals(21, user.getId());
    }

    @Test
    void currentUserVerified() throws IOException {
        LCLPNetworkAPI auth = getAuth();
        assertNotNull(auth);

        Boolean verified = auth.isCurrentUserVerified().join();
        assertNotNull(verified);
        assertFalse(verified); // the dummy user's email is not verified
    }

    /* */

    @Nullable
    static LCLPNetworkAPI getAuth() throws IOException {
        return getAuth("token", null, LCLPNetworkAPI::new);
    }

    @Nullable
    static <T extends LCLPNetworkAPI> T getAuth(String tokenKey, String host, Function<APIAuthAccess, T> mapper) throws IOException {
        String token = getPrivateProperty(tokenKey);
        if (token == null) return null;

        CompletableFuture<APIAuthAccess> future;
        if (host == null) future = APIAccess.withAuth(token);
        else {
            APIAuthAccess access = new APIAuthAccess(token);
            access.setHost(host);
            future = APIAccess.withAuthCheck(access);
        }

        return future.thenApply(mapper).join();
    }

    @Nullable
    static String getPrivateProperty(String key) throws IOException {
        File f = new File("src/test/resources/test-private.properties");
        Properties privateProps = new Properties();
        try (InputStream in = new FileInputStream(f)) {
            privateProps.load(in);
        } catch (FileNotFoundException e) {
            return null;
        }
        return privateProps.getProperty(key);
    }

}
