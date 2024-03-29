package ru.dexit.admindev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.helpers.CoreApiMethodsEmployee;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;

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

}
