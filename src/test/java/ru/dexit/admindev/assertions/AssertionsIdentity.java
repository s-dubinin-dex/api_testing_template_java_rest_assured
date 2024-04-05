package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import ru.dexit.admindev.models.identity.IdentityResponseModel;

import static org.assertj.core.api.Assertions.*;

public class AssertionsIdentity {

    public static void authorizationSuccessfully(Response response){

        IdentityResponseModel responseBody = response.body().as(IdentityResponseModel.class);

        assertThat(responseBody.access_token).isNotEmpty();
        assertThat(responseBody.expires_in).isEqualTo(28800);
        assertThat(responseBody.token_type).isEqualTo("Bearer");
        assertThat(responseBody.refresh_token).isNotEmpty();
        assertThat(responseBody.scope).isEqualTo("admin-api offline_access openid policy profile");

    }
}