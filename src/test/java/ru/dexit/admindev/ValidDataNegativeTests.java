package ru.dexit.admindev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Employee;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.helpers.CoreApiMethodsEmployee;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.Employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.Employee.UpdateEmployeeRequestModel;

@DisplayName("Негативные тесты с валидными данными (по типу данных)")
public class ValidDataNegativeTests extends TestBase{

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с невалидными именами")
    @Description("Тест пытается создать сотрудника с невалидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeNames")
    public void testAddEmployeeWithInvalidName(String name){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);

        AssertionsEmployee.employeeIsNotCreatedInvalidName(response);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с невалидными ролями")
    @Description("Тест пытается создать сотрудника с невалидными ролями")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoles")
    public void testAddEmployeeWithInvalidRole(String roleUUID){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);

        AssertionsEmployee.employeeIsNotCreatedInvalidRole(response);
    }
    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с ролью \"Полные права\"")
    @Description("Тест пытается создать сотрудника с ролью \"Полные права\"")
    public void testAddEmployeeWithRoleFullRights(){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_RIGHTS.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);

        AssertionsEmployee.employeeIsNotCreatedWithRoleFullRights(response);
    }

    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с ID несуществующей роли")
    @Description("Тест пытается создать сотрудника с ID несуществующей роли")
    public void testAddEmployeeWithNotExistRole(){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(faker.internet().uuid())
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);

        AssertionsEmployee.employeeIsNotCreatedWithNotExistRole(response);
    }

    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с существующим email")
    @Description("Тест пытается создать сотрудника с существующим email")
    public void testAddEmployeeWithExistEmail(){

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseCreationModel = responseCreation.as(EmployeeCommonResponseModel.class);

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .email(responseCreationModel.email)
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);

        AssertionsEmployee.employeeIsNotCreatedDuplicateEmail(response);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с невалидным email")
    @Description("Тест пытается создать сотрудника с невалидным email")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmails")
    public void testAddEmployeeWithInvalidEmail(String email){

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .email(email)
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);

        AssertionsEmployee.employeeIsNotCreatedInvalidEmail(response);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c невалидным ID")
    @Description("Тест изменяет сотрудника c невалидным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidIDs")
    public void testUpdateEmployeeWithInvalidId(String id){

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(id)
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotCreatedInvalidID(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c невалидным ID")
    @Description("Тест изменяет сотрудника c невалидным ID")
    @Test
    public void testUpdateEmployeeWithNotExistId(){

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotCreatedNotExistID(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c ID администратора")
    @Description("Тест изменяет сотрудника c ID администратора")
    @Test
    public void testUpdateEmployeeWithAdministratorId(){

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(Employee.ADMINISTRATOR.id)
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotCreatedDeletingAdministratorNotAllowed(response);

    }

}
