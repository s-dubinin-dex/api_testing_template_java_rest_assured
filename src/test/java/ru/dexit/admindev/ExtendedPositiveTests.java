package ru.dexit.admindev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.helpers.CoreApiMethodsEmployee;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.Employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.Employee.UpdateEmployeeRequestModel;

@DisplayName("Расширенные позитивные тесты")
public class ExtendedPositiveTests extends TestBase{

    @Epic("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание сотрудника c валидным именем:")
    @Description("Тест создаёт сотрудника с валидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidEmployeeNames")
    public void testAddEmployeeWithValidNames(String name){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        AssertionsEmployee.employeeAddedSuccessfully(response, requestBody);

    }

    @Epic("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание сотрудника c существуюшим именем")
    @Description("Тест создаёт сотрудника с существуюшим именем")
    @Test
    public void testAddEmployeeWithExistName(){

        String name = faker.name().fullName();

        AddEmployeeRequestModel requestBodyFirstEmployee = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response responseFirstEmployee = CoreApiMethodsEmployee.addEmployee(requestBodyFirstEmployee);

        AddEmployeeRequestModel requestBodySecondEmployee = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response responseSecondEmployee = CoreApiMethodsEmployee.addEmployee(requestBodySecondEmployee);

        AssertionsEmployee.employeeAddedSuccessfully(responseSecondEmployee, requestBodySecondEmployee);

    }

    @Epic("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание сотрудника c валидной ролью")
    @Description("Тест создаёт сотрудника с валидными ролями")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidRoles")
    public void testAddEmployeeWithValidRoles(String role){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().fullName())
                .roleId(role)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        AssertionsEmployee.employeeAddedSuccessfully(response, requestBody);

    }

    @Epic("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание сотрудника c валидным email")
    @Description("Тест создаёт сотрудника с валидными email")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidEmails")
    public void testAddEmployeeWithDifferentEmails(String email){
            AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                    .name(faker.name().fullName())
                    .roleId(Role.FULL_WRITE.roleUUID)
                    .email(email)
                    .build();

            Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
            AssertionsEmployee.employeeAddedSuccessfully(response, requestBody);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение сотрудника c валидным ID")
    @Description("Тест изменяет сотрудника c валидным ID")
    @Test
    public void testUpdateEmployeeWithValidId(){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyCreation.id)
                .name(faker.name().fullName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeUpdatedSuccessfully(response, requestBody, responseBodyCreation);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение сотрудника c валидным именем")
    @Description("Тест изменяет сотрудника c валидным именем")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidEmployeeNames")
    public void testUpdateEmployeeWithValidName(String name){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyCreation.id)
                .name(name)
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeUpdatedSuccessfully(response, requestBody, responseBodyCreation);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение сотрудника валидными ролями")
    @Description("Тест изменяет сотрудника валидными ролями")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidRoles")
    public void testUpdateEmployeeWithValidRole(String role){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyCreation.id)
                .name(faker.name().fullName())
                .roleId(role)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeUpdatedSuccessfully(response, requestBody, responseBodyCreation);

    }

}
