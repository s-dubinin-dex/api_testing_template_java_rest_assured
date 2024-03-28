package ru.dex_it.k3s.admin_dev.assertions;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.dex_it.k3s.admin_dev.models.Role.AddRoleRequestModel;
import ru.dex_it.k3s.admin_dev.models.Role.RoleCommonResponseModel;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsRole {
    public static void roleCreatedSuccessfully(Response response, AddRoleRequestModel requestBody){

        RoleCommonResponseModel responseBody = response.as(RoleCommonResponseModel.class);

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        assertFalse(responseBody.id.isEmpty(), "Role id is empty");
        assertEquals(requestBody.name, responseBody.name, "Role policies in response is not equal to role policies in request");
        assertEquals(requestBody.policies, responseBody.policies, "");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");

        // Check response time
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms");

    }
}
