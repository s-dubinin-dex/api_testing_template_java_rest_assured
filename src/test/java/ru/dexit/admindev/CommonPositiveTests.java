package ru.dexit.admindev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.assertions.AssertionsIdentity;
import ru.dexit.admindev.assertions.AssertionsRole;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.helpers.CoreApiMethodsEmployee;
import ru.dexit.admindev.helpers.CoreApiMethodsIdentity;
import ru.dexit.admindev.helpers.CoreApiMethodsRole;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.Employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.Employee.UpdateEmployeeRequestModel;
import ru.dexit.admindev.models.Role.AddRoleRequestModel;
import ru.dexit.admindev.models.Role.RoleCommonResponseModel;
import ru.dexit.admindev.models.Role.UpdateRoleRequestModel;

import static ru.dexit.admindev.data.DataGenerator.*;

@DisplayName("Общие позитивные тесты. Smoke tests.")
public class CommonPositiveTests extends TestBase{

    @Test
    @Feature("Authorization")
    @Story("Авторизация и получение токена")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Авторизация и получение токена")
    @Description("Тест проходит авторизаци и проверяет получение токена")
    public void testAuthorizationAndGettingToken(){

        Response response = CoreApiMethodsIdentity.connectToken();
        AssertionsIdentity.authorizationSuccessfully(response);

    }

    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание сотрудника с валидными параметрами")
    @Description("Тест создаёт сотрудника с валидными параметрами")
    public void testAddEmployee(){

        AddEmployeeRequestModel requestBody = DataGenerator.getRandomAddEmployeeRequestModel();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        AssertionsEmployee.employeeAddedSuccessfully(response, requestBody);

    }
    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение сотрудника валидными параметрами")
    @Description("Тест изменяет сотрудника валидными параметрами")
    @Test
    public void testUpdateEmployee(){

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
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Генерация нового приглашения с новым токеном активации валидными параметрами")
    @Description("Тест генерирует приглашение с новым токеном активации валидными параметрами")
    @Test
    public void testUpdateInvitation(){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        Response response = CoreApiMethodsEmployee.updateInvitation(responseBodyCreation.id);
        AssertionsEmployee.invitationUpdatedSuccessfully(response, responseBodyCreation);
    }

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData")
    @Description("Тест запрашивает данные через протокол oData")
    @Test
    public void testGetODataEmployee(){

        Response response = CoreApiMethodsEmployee.oDataEmployee();

        AssertionsEmployee.oDataEmployeeReturnsData(response);

    }

    @Test
    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление сотрудника с валидными данными")
    @Description("Тест удаляет сотрудника с валидными данными")
    public void testDeleteEmployee(){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        Response response = CoreApiMethodsEmployee.deleteEmployee(responseBodyCreation.id);
        AssertionsEmployee.employeeDeletedSuccessfully(response);

    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание роли с валидными данными")
    @Description("Тест создаёт роль с валидными данными")
    public void testAddRole(){

        AddRoleRequestModel requestBody = DataGenerator.getRandomAddRoleRequestModel();
        Response response = CoreApiMethodsRole.addRole(requestBody);

        AssertionsRole.roleAddedSuccessfully(response, requestBody);

    }

    @Test
    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение роли с валидными данными")
    @Description("Тест изменяет роль с валидными данными")
    public void testUpdateRole(){

        AddRoleRequestModel requestBodyAddRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseAddRole = CoreApiMethodsRole.addRole(requestBodyAddRole);
        RoleCommonResponseModel responseBodyAddRole = responseAddRole.as(RoleCommonResponseModel.class);

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession()+ "_" + generateRandomString(engLetters, 6))
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyAddRole.id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        AssertionsRole.roleUpdatedSuccessfully(response, requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление роли с валидными данными")
    @Description("Тест удаляет роль с валидными данными")
    public void testDeleteRole() {

        AddRoleRequestModel requestBodyForAddingRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForAddingRole = CoreApiMethodsRole.addRole(requestBodyForAddingRole);
        RoleCommonResponseModel responseBodyForAddingRole = responseForAddingRole.as(RoleCommonResponseModel.class);

        Response response = CoreApiMethodsRole.deleteRole(responseBodyForAddingRole.id);
        AssertionsRole.roleDeletedSuccessfully(response);

    }
    @Test
    @Feature("Role")
    @Story("Список доступных полиси для настройки ролей")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка полиcи")
    @Description("Тест получает список доступных полиси для настройки ролей")
    public void testGetPolicies() {

        Response response = CoreApiMethodsRole.getPolicies();
        AssertionsRole.policiesGotSuccessfully(response);

    }


    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData")
    @Description("Тест запрашивает данные через протокол oData")
    public void testGetODataRole() {

        Response response = CoreApiMethodsRole.getODataRole();
        AssertionsRole.oDataRoleReturnsData(response);

    }

}
