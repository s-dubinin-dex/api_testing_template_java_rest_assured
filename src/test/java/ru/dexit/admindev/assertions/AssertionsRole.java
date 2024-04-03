package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.dexit.admindev.models.role.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsRole {
    public static void roleAddedSuccessfully(Response response, AddRoleRequestModel requestBody){

        RoleCommonResponseModel responseBody = response.as(RoleCommonResponseModel.class);

        // Check response body

        assertFalse(responseBody.id.isEmpty(), "Role id is empty");
        assertEquals(requestBody.name, responseBody.name, "Role name in response is equal to role name in request");
        assertEquals(requestBody.policies, responseBody.policies, "Role policies in response is not equal to role policies in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");

    }

    public static void roleUpdatedSuccessfully(Response response, UpdateRoleRequestModel requestBody){

        RoleCommonResponseModel responseBody = response.as(RoleCommonResponseModel.class);

        List<String> requestBodyPolicies = new ArrayList<>(requestBody.policies);
        List<String> responseBodyPolicies = new ArrayList<>(responseBody.policies);

        Collections.sort(requestBodyPolicies);
        Collections.sort(responseBodyPolicies);

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        assertEquals(requestBody.id, responseBody.id, "Role ID in response is not equal to Role ID in request");
        assertEquals(requestBody.name, responseBody.name, "Role name in response is not equal to role name in request");
        assertEquals(requestBodyPolicies, responseBodyPolicies, "Role policies in response is not equal to role policies in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");

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

    public static void roleIsNotCreatedInvalidName(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void roleIsNotCreatedWithExistName(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_CONFLICT, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleIsNotCreatedWithInvalidPolicies(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleIsNotUpdatedInvalidName(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleIsNotUpdatedWithExistName(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_CONFLICT, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleIsNotUpdatedWithInvalidPolicies(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleIsNotUpdatedWithInvalidRoleId(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleIsNotUpdatedWithNotExistRoleId(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleFullRightsIsNotUpdated(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_PRECONDITION_FAILED, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
    public static void roleNoRightsIsNotUpdated(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_PRECONDITION_FAILED, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void roleAssignedToEmployeeIsNotDeleted(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_CONFLICT, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void roleIsNotDeletedInvalidRoleId(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void roleIsNotDeletedNotExistRoleId(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void roleFullRightsIsNotDeleted(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_PRECONDITION_FAILED, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void roleNoRightsIsNotDeleted(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_PRECONDITION_FAILED, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }
}
