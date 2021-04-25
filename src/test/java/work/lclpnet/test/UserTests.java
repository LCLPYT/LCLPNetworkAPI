/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.test;

import org.junit.jupiter.api.Test;
import work.lclpnet.lclpnetwork.LCLPNetworkAPI;
import work.lclpnet.lclpnetwork.api.APIAccess;
import work.lclpnet.lclpnetwork.api.APIException;
import work.lclpnet.lclpnetwork.facade.User;

import java.util.concurrent.CompletionException;

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
    void revokeTokenMissingScopes() {
        LCLPNetworkAPI auth = getDummyAuthAPI();
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
    void currentUser() {
        LCLPNetworkAPI auth = getDummyAuthAPI();
        User user = auth.getCurrentUser().join();
        assertNotNull(user);
        assertEquals(21, user.getId());
    }

    @Test
    void currentUserVerified() {
        LCLPNetworkAPI auth = getDummyAuthAPI();
        Boolean verified = auth.isCurrentUserVerified().join();
        assertNotNull(verified);
        assertFalse(verified); // the dummy user's email is not verified
    }

    /*private String getToken() throws IOException {
        File f = new File("src/test/resources/test-private.properties");
        Properties privateProps = new Properties();
        try (InputStream in = new FileInputStream(f)) {
            privateProps.load(in);
        } catch (FileNotFoundException e) {
            return null;
        }
        return privateProps.getProperty("token");
    }*/

    /**
     * This is a dummy token which has basic permission to test the API.
     */
    private static final String DUMMY_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiZjI2NDVhMTE3NzA4YTdhMGQ0ODk2YWM0YWNjY2M4MWFlOTRmZmRiMmJlMzZkNTNlNjk3Y2EyZWIwNzU5MDA5MmM1YTllNWNjMzgyM2ExMDUiLCJpYXQiOjE2MTkzNzUwNTkuNTYxNzksIm5iZiI6MTYxOTM3NTA1OS41NjE3OTQsImV4cCI6MTY1MDkxMTA1OS41NTc2NjMsInN1YiI6IjIxIiwic2NvcGVzIjpbImlkZW50aXR5IiwiaWRlbnRpdHlbZW1haWxdIl19.iQL_JExW1mBvy0oycG2V713UJBReFms6pQgL3pW6-A4hzz7aG4ePCGWsza2riCVj174KxsB9ukk5X0tzW-WRszq8VuUOfZ8uMxuLiIicpiDq8gGZAiyeaUYOz0J6nAOreIw-4ZZDUVdF2Byy-6JrbrI-Wp6rxRfBvASp2ZKErIASHyLqXO3eg7G5oGbk3KmPIepoFJQ061DFEysgkfYVewBvq73jFNPcLH1pni1aZduzJDECi4A29qbjo6F7_3VxCrhnBcNMAHN707LzISEBecil4m-bbyEEjef6zGH8vGw_-d1dLkVicDBdSZVliDXGpkCS6VLvXrZqFDMIFO2AJZEGcbOXIpKye425bpvdm_Y1iKJ2LqAo0RP6Vlqma-xL94iHUpcb9XJTGQ0lw-6yUIrWGfv0rzA2CRK5sngQEF4A_TXI46EdgiHSRJ6RtOqpet5ejxGPXrda4RYBvnr3A8x0coo_VebzCacajPeNYYSDuA51RqtD7kZ0lcSN6Woru3MOdiE1Kb_vLJkPec4zaAbEDhTH_xwSuoa1UzKhyRWPSx-dPd4r53_o35vwtbjVOy1wp7wfETCx1AFJP3vaC5w1-uBirHroU_IGl5XpVjwckn7mixJYeyHzhpNXzGN8yPru9Ulkv6-DPm5adaPj9Uov64vK67u13KCzpRfF72o";

    private LCLPNetworkAPI getDummyAuthAPI() {
        return APIAccess.withAuth(DUMMY_TOKEN).thenApply(LCLPNetworkAPI::new).join();
    }

}
