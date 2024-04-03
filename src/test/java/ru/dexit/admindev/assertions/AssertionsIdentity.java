package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import ru.dexit.admindev.models.identity.IdentityResponseModel;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsIdentity {

    public static void authorizationSuccessfully(Response response){

        IdentityResponseModel responseBody = response.body().as(IdentityResponseModel.class);

        assertFalse(responseBody.access_token.isEmpty(), "Access token is empty");
        assertEquals(28800, responseBody.expires_in, "expires_in is not equal to 28800");
        assertEquals("Bearer", responseBody.token_type, "token_type is not equal to Bearer");
        assertFalse(responseBody.refresh_token.isEmpty(), "Refresh token is empty");
        assertEquals("admin-api offline_access openid policy profile", responseBody.scope, "scope is invalid");

    }

}