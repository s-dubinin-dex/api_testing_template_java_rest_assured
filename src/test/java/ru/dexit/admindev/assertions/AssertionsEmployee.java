package ru.dexit.admindev.assertions;

import io.restassured.response.Response;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.employee.ODataEmployeeResponseModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AssertionsEmployee {

    public static void employeeAddedSuccessfully(Response response, AddEmployeeRequestModel requestBody){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        assertAll(
                () -> assertThat(responseBody.id).isNotEmpty(),
                () -> assertThat(responseBody.name).isEqualTo(requestBody.name),
                () -> assertThat(responseBody.createdUtc).isNotEmpty(),
                () -> assertThat(responseBody.deletedUtc).isNull(),
                () -> assertThat(responseBody.roleId).isEqualTo(requestBody.roleId),
                () -> assertThat(responseBody.role).isEqualTo(Role.findRoleById(requestBody.roleId).getRoleName()),
                () -> assertThat(responseBody.email).isEqualTo(requestBody.getEmail().toLowerCase()),
                () -> assertThat(responseBody.activationDate).isNull()
        );

    }

    public static void employeeUpdatedSuccessfully(Response response, UpdateEmployeeRequestModel requestBody, EmployeeCommonResponseModel responseBodyCreation){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        assertAll(
                () -> assertThat(responseBody.id).isEqualTo(requestBody.id),
                () -> assertThat(responseBody.name).isEqualTo(requestBody.name),
                () -> assertThat(responseBody.createdUtc).isNotEmpty(),
                () -> assertThat(responseBody.deletedUtc).isNull(),
                () -> assertThat(responseBody.roleId).isEqualTo(requestBody.roleId),
                () -> assertThat(responseBody.role).isEqualTo(Role.findRoleById(requestBody.roleId).getRoleName()),
                () -> assertThat(responseBody.email).isEqualTo(responseBodyCreation.email),
                () -> assertThat(responseBody.activationDate).isNull()
        );

    }

    public static void invitationUpdatedSuccessfully(Response response, EmployeeCommonResponseModel responseBodyCreation){

        EmployeeCommonResponseModel responseBody = response.body().as(EmployeeCommonResponseModel.class);

        assertAll(
                () -> assertThat(responseBody.id).isEqualTo(responseBodyCreation.id),
                () -> assertThat(responseBody.name).isEqualTo(responseBodyCreation.name),
                () -> assertThat(responseBody.createdUtc).isNotEmpty(),
                () -> assertThat(responseBody.deletedUtc).isNull(),
                () -> assertThat(responseBody.roleId).isEqualTo(responseBodyCreation.roleId),
                () -> assertThat(responseBody.role).isEqualTo(Role.findRoleById(responseBodyCreation.roleId).getRoleName()),
                () -> assertThat(responseBody.email).isEqualTo(responseBodyCreation.email),
                () -> assertThat(responseBody.activationDate).isNull()
        );

    }

    public static void oDataEmployeeReturnsData(Response response){

        List<ODataEmployeeResponseModel> responseData = response.jsonPath().get("value");

    }
}
