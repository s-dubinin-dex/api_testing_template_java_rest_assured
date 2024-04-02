package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.Employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.Employee.ODataEmployeeResponseModel;
import ru.dexit.admindev.models.Employee.UpdateEmployeeRequestModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsEmployee {

    public static void employeeAddedSuccessfully(Response response, AddEmployeeRequestModel requestBody){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        assertFalse(responseBody.id.isEmpty(), "Employee ID is empty!");
        assertEquals(requestBody.name, responseBody.name, "Employee name in response is equal to name in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");
        assertEquals(requestBody.roleId, responseBody.roleId, "roleId in response is not equal to roleId in request");
        assertEquals(Role.findRoleById(requestBody.roleId).roleName, responseBody.role, "role is not equal to role in request body");
        assertEquals(requestBody.email.toLowerCase(), responseBody.email, "email in response is not equal to email in request");
        assertNull(responseBody.activationDate, "activationDate is not equal to null");

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());
    }

    public static void employeeUpdatedSuccessfully(Response response, UpdateEmployeeRequestModel requestBody, EmployeeCommonResponseModel responseBodyCreation){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        assertEquals(requestBody.id, responseBody.id, "Employee ID in response is not equal to Employee ID in request");
        assertEquals(requestBody.name, responseBody.name, "Employee name in response is equal to name in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");
        assertEquals(requestBody.roleId, responseBody.roleId, "roleId in response is not equal to roleId in request");
        assertEquals(Role.findRoleById(requestBody.roleId).roleName, responseBody.role, "role is not equal to role in request body");
        assertEquals(responseBodyCreation.email, responseBody.email, "email in response is not equal to Employee email");
        assertNull(responseBody.activationDate, "activationDate is not equal to null");

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());
    }

    public static void invitationUpdatedSuccessfully(Response response, EmployeeCommonResponseModel responseBodyCreation){
        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        assertEquals(responseBodyCreation.id, responseBody.id, "Employee ID in response is not equal to Employee ID in request");
        assertEquals(responseBodyCreation.name, responseBody.name, "Employee name in response is equal to name in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");
        assertEquals(responseBodyCreation.roleId, responseBody.roleId, "roleId in response is not equal to roleId in request");
        assertEquals(Role.findRoleById(responseBodyCreation.roleId).roleName, responseBody.role, "role is not equal to Test FullWriteRole");
        assertEquals(responseBodyCreation.email, responseBody.email, "email in response is not equal to Employee email");
        assertNull(responseBody.activationDate, "activationDate is not equal to null");

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());
    }


    public static void oDataEmployeeReturnsData(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response body

        List<ODataEmployeeResponseModel> responseData = response.jsonPath().get("value");

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());
    }

    public static void employeeDeletedSuccessfully(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_OK, response.statusCode(), "Incorrect status code");

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());

    }

    public static void employeeIsNotCreatedInvalidName(Response response){

        // Check status code

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode(), "Incorrect status code");

        // TODO: Добавить проверку моделей

        // Check response time

        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 500, "Response time more than 500 ms, actual is " + response.time());


    }
}
