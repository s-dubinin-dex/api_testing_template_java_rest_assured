package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import ru.dexit.admindev.models.role.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AssertionsRole {
    public static void roleAddedSuccessfully(Response response, AddRoleRequestModel requestBody){

        RoleCommonResponseModel responseBody = response.as(RoleCommonResponseModel.class);

        assertAll(
                () -> assertThat(responseBody.id).isNotEmpty(),
                () -> assertThat(responseBody.name).isEqualTo(requestBody.name),
                () -> assertThat(responseBody.policies).isEqualTo(requestBody.policies),
                () -> assertThat(responseBody.createdUtc).isNotEmpty(),
                () -> assertThat(responseBody.deletedUtc).isNull()
        );

    }

    public static void roleUpdatedSuccessfully(Response response, UpdateRoleRequestModel requestBody){

        RoleCommonResponseModel responseBody = response.as(RoleCommonResponseModel.class);

        List<String> requestBodyPolicies = new ArrayList<>(requestBody.policies);
        List<String> responseBodyPolicies = new ArrayList<>(responseBody.policies);

        Collections.sort(requestBodyPolicies);
        Collections.sort(responseBodyPolicies);

        assertAll(
                () -> assertThat(responseBody.id).isEqualTo(requestBody.id),
                () -> assertThat(responseBody.name).isEqualTo(requestBody.name),
                () -> assertThat(responseBodyPolicies).isEqualTo(requestBodyPolicies),
                () -> assertThat(responseBody.createdUtc).isNotEmpty(),
                () -> assertThat(responseBody.deletedUtc).isNull()
        );



    }

    public static void policiesGotSuccessfully(Response response) {

        // Check response body

        List<GetPoliciesResponseModel> responseBody = response.jsonPath().get();

        // TODO: придумать, как верифицировать тело ответа

    }

    public static void oDataRoleReturnsData(Response response){

        // Check response body

        List<ODataRoleResponseModel> responseBody = response.jsonPath().get("value");

    }

}
