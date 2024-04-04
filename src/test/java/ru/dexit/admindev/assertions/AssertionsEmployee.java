package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.employee.ODataEmployeeResponseModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsEmployee {

    public static void employeeAddedSuccessfully(Response response, AddEmployeeRequestModel requestBody){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        // Check response body
        // TODO: Переделать на assertThat, класс с ролями и идентити тоже
        assertFalse(responseBody.id.isEmpty(), "Employee ID is empty!");
        assertEquals(requestBody.name, responseBody.name, "Employee name in response is equal to name in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");
        assertEquals(requestBody.roleId, responseBody.roleId, "roleId in response is not equal to roleId in request");
        assertEquals(Role.findRoleById(requestBody.roleId).roleName, responseBody.role, "role is not equal to role in request body");
        assertEquals(requestBody.email.toLowerCase(), responseBody.email, "email in response is not equal to email in request");
        assertNull(responseBody.activationDate, "activationDate is not equal to null");

    }

    public static void employeeUpdatedSuccessfully(Response response, UpdateEmployeeRequestModel requestBody, EmployeeCommonResponseModel responseBodyCreation){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        // Check response body

        assertEquals(requestBody.id, responseBody.id, "Employee ID in response is not equal to Employee ID in request");
        assertEquals(requestBody.name, responseBody.name, "Employee name in response is equal to name in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");
        assertEquals(requestBody.roleId, responseBody.roleId, "roleId in response is not equal to roleId in request");
        assertEquals(Role.findRoleById(requestBody.roleId).roleName, responseBody.role, "role is not equal to role in request body");
        assertEquals(responseBodyCreation.email, responseBody.email, "email in response is not equal to Employee email");
        assertNull(responseBody.activationDate, "activationDate is not equal to null");

    }

    public static void invitationUpdatedSuccessfully(Response response, EmployeeCommonResponseModel responseBodyCreation){
        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        // Check response body

        assertEquals(responseBodyCreation.id, responseBody.id, "Employee ID in response is not equal to Employee ID in request");
        assertEquals(responseBodyCreation.name, responseBody.name, "Employee name in response is equal to name in request");
        assertFalse(responseBody.createdUtc.isEmpty(), "createdUtc is empty");
        assertNull(responseBody.deletedUtc, "deletedUtc is not null");
        assertEquals(responseBodyCreation.roleId, responseBody.roleId, "roleId in response is not equal to roleId in request");
        assertEquals(Role.findRoleById(responseBodyCreation.roleId).roleName, responseBody.role, "role is not equal to Test FullWriteRole");
        assertEquals(responseBodyCreation.email, responseBody.email, "email in response is not equal to Employee email");
        assertNull(responseBody.activationDate, "activationDate is not equal to null");

    }

    public static void oDataEmployeeReturnsData(Response response){

        // Check response body

        List<ODataEmployeeResponseModel> responseData = response.jsonPath().get("value");

    }
}
