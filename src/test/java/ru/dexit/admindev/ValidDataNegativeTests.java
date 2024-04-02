package ru.dexit.admindev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.assertions.AssertionsRole;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Employee;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.helpers.CoreApiMethodsEmployee;
import ru.dexit.admindev.helpers.CoreApiMethodsRole;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import ru.dexit.admindev.models.role.AddRoleRequestModel;
import ru.dexit.admindev.models.role.RoleCommonResponseModel;
import ru.dexit.admindev.models.role.UpdateRoleRequestModel;

import java.util.List;

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
                .name(responseBodyForCreation.name)
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
                .name(responseBodyForCreation.name)
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
                .name(responseBodyForCreation.name)
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

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Запрос данных через протокол OData")
    @Description("Тест не реализован")
    @Test
    public void testGetODataEmployeeDraft(){

    }

    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление сотрудника с невалидным ID")
    @Description("Удаление сотрудника с невалидным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeIDs")
    public void testDeleteEmployeeWithInvalidID(String id){

        Response response = CoreApiMethodsEmployee.deleteEmployee(id);
        AssertionsEmployee.employeeIsNotDeletedInvalidID(response);

    }

    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление сотрудника с несуществующим ID")
    @Description("Удаление сотрудника с несуществующим ID")
    @Test
    public void testDeleteEmployeeWithNotExistID(){

        Response response = CoreApiMethodsEmployee.deleteEmployee(faker.internet().uuid());
        AssertionsEmployee.employeeIsNotDeletedNotExistID(response);

    }

    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление сотрудника с полными правами")
    @Description("Попытка удаления сотрудника с полными правами")
    @Test
    public void testDeleteEmployeeWithFullRightsIsForbidden(){

        Response response = CoreApiMethodsEmployee.deleteEmployee(Employee.ADMINISTRATOR.id);
        AssertionsEmployee.deleteEmployeeWithFullRightsIsForbidden(response);

    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с невалидными именами")
    @Description("Тест пытается создать роль с невалидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleNames")
    public void testAddRoleWithInvalidName(String name){

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(name)
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);

        AssertionsRole.roleIsNotCreatedInvalidName(response);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с существующим именем")
    @Description("Тест пытается создать роль с существующим именем")
    @Test
    public void testAddRoleWithExistingName(){

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(DataGenerator.getDefaultPolicies())
                .build();

        Response response = CoreApiMethodsRole.addRole(requestBody);
        AssertionsRole.roleIsNotCreatedWithExistName(response);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с некорректными политиками")
    @Description("Тест пытается создать роль с некорректными политиками")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidPoliciesStream")
    public void testAddRoleWithInvalidPolicies(List<String> policies){

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(policies)
                .build();

        Response response = CoreApiMethodsRole.addRole(requestBody);
        AssertionsRole.roleIsNotCreatedWithInvalidPolicies(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с невалидными именами")
    @Description("Тест пытается измененить роль с невалидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleNames")
    public void testUpdateRoleWithInvalidName(String name){

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(name)
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyForCreation.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);

        AssertionsRole.roleIsNotUpdatedInvalidName(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли существующим именем")
    @Description("Тест пытается изменить роль существующим именем")
    @Test
    public void testUpdateRoleWithExistingName(){

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        AddRoleRequestModel requestBodyForUpdate = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForUpdate = CoreApiMethodsRole.addRole(requestBodyForUpdate);
        RoleCommonResponseModel responseBodyForUpdate = responseForUpdate.as(RoleCommonResponseModel.class);

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyForUpdate.id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleIsNotUpdatedWithExistName(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с некорректными политиками")
    @Description("Тест пытается измененить роль с некорректными политиками")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidPoliciesStream")
    public void testUpdateRoleWithInvalidPolicies(List<String> policies){

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(policies)
                .id(responseBodyForCreation.id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleIsNotUpdatedWithInvalidPolicies(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с некорректными RoleId")
    @Description("Тест пытается измененить роль с некорректными RoleId")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
    public void testUpdateRoleWithInvalidRoleId(String id){

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(responseBodyForCreation.policies)
                .id(id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleIsNotUpdatedWithInvalidRoleId(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с несуществующим RoleId")
    @Description("Тест пытается измененить роль с несуществующим RoleId")
    @Test
    public void testUpdateRoleWithNotExistRoleId(){

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(responseBodyForCreation.policies)
                .id(faker.internet().uuid())
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleIsNotUpdatedWithNotExistRoleId(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли Полные права")
    @Description("Тест пытается изменить роль Полные права")
    @Test
    public void testUpdateFullRightsRole(){

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(Role.FULL_RIGHTS.roleUUID)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleFullRightsIsNotUpdated(response);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли Нет прав")
    @Description("Тест пытается изменить роль Нет прав")
    @Test
    public void testUpdateNoRightsRole(){

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(Role.NO_RIGHTS.roleUUID)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleNoRightsIsNotUpdated(response);
    }

}
