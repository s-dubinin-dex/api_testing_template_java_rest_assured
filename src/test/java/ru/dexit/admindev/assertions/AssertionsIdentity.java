package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.dexit.admindev.models.Identity.IdentityResponseModel;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionsIdentity {

    public static void authorizationSuccessfully(Response response){

        IdentityResponseModel responseBody = response.body().as(IdentityResponseModel.class);

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        assertFalse(responseBody.access_token.isEmpty(), "Access token is empty");
        assertEquals(28800, responseBody.expires_in, "expires_in is not equal to 28800");
        assertEquals("Bearer", responseBody.token_type, "token_type is not equal to Bearer");
        assertFalse(responseBody.refresh_token.isEmpty(), "Refresh token is empty");
        assertEquals("admin-api offline_access openid policy profile", responseBody.scope, "scope is invalid");

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());
    }

}