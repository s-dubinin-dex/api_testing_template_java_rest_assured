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
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
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

        AssertionsEmployee.employeeIsNotUpdatedDuplicateEmail(response);
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

        AssertionsEmployee.employeeIsNotUpdatedInvalidEmail(response);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c невалидным ID")
    @Description("Тест изменяет сотрудника c невалидным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeIDs")
    public void testUpdateEmployeeWithInvalidId(String id){

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(id)
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotUpdatedInvalidID(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c несуществующим ID")
    @Description("Тест изменяет сотрудника c несуществующим ID")
    @Test
    public void testUpdateEmployeeWithNotExistId(){

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotUpdatedNotExistID(response);

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
        AssertionsEmployee.employeeIsNotUpdatedAdministratorIsReadOnly(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c невалидным именем")
    @Description("Тест изменяет сотрудника c невалидным именем")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeNames")
    public void testUpdateEmployeeWithInvalidName(String name){

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(name)
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotUpdatedInvalidName(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотруднику роли на невалидную roleID")
    @Description("Изменение сотруднику роли на невалидную roleID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
    public void testUpdateEmployeeWithInvalidID(String id){

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(faker.name().firstName())
                .roleId(id)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotUpdatedInvalidRoleID(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотруднику роли на \"Полные права\"")
    @Description("Изменение сотруднику роли на \"Полные права\"")
    @Test
    public void testUpdateEmployeeWithFullRightsRoleID(){

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(faker.name().firstName())
                .roleId(Role.FULL_RIGHTS.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotUpdatedAssigningFullRights(response);

    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотруднику роли на роль c несуществующим roleID")
    @Description("Изменение сотруднику роли на роль c несуществующим roleID")
    @Test
    public void testUpdateEmployeeWithNotExistRoleID(){

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(faker.name().firstName())
                .roleId(faker.internet().uuid())
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        AssertionsEmployee.employeeIsNotUpdatedRoleNotExist(response);

    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Генерация приглашения сотруднику с невалидным ID")
    @Description("Генерация приглашения сотруднику с невалидным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeIDs")
    public void testUpdateInvitationWithInvalidID(String id){

        Response response = CoreApiMethodsEmployee.updateInvitation(id);
        AssertionsEmployee.invitationIsNotSentInvalidID(response);

    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Генерация приглашения сотруднику с несуществующим ID")
    @Description("Генерация приглашения сотруднику с несуществующим ID")
    @Test
    public void testUpdateInvitationWithNotExistID(){

        Response response = CoreApiMethodsEmployee.updateInvitation(faker.internet().uuid());
        AssertionsEmployee.invitationIsNotSentNotExistID(response);

    }

}
