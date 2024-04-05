package ru.dexit.admindev.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.assertions.AssertionsIdentity;
import ru.dexit.admindev.assertions.AssertionsRole;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsEmployee;
import ru.dexit.admindev.apiMethods.identity.CoreApiMethodsIdentity;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsRole;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import ru.dexit.admindev.models.role.AddRoleRequestModel;
import ru.dexit.admindev.models.role.RoleCommonResponseModel;
import ru.dexit.admindev.models.role.UpdateRoleRequestModel;
import ru.dexit.admindev.spec.RequestSpecifications;
import ru.dexit.admindev.spec.ResponseSpecifications;
import ru.dexit.admindev.spec.SpecificationsServer;

import static ru.dexit.admindev.data.DataGenerator.*;

@DisplayName("Общие позитивные тесты. Smoke tests.")
public class CommonPositiveTests extends TestBase {

    @Test
    @Feature("Authorization")
    @Story("Авторизация и получение токена")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Авторизация и получение токена")
    @Description("Тест проходит авторизацию и проверяет получение токена")
    public void testAuthorizationAndGettingToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecification());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200EmptyStringBody());
        CoreApiMethodsEmployee.deleteEmployee(responseBodyCreation.id);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание роли с валидными данными")
    @Description("Тест создаёт роль с валидными данными")
    public void testAddRole(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddRoleRequestModel requestBodyAddRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseAddRole = CoreApiMethodsRole.addRole(requestBodyAddRole);
        RoleCommonResponseModel responseBodyAddRole = responseAddRole.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddRoleRequestModel requestBodyForAddingRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForAddingRole = CoreApiMethodsRole.addRole(requestBodyForAddingRole);
        RoleCommonResponseModel responseBodyForAddingRole = responseForAddingRole.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200EmptyStringBody());
        CoreApiMethodsRole.deleteRole(responseBodyForAddingRole.id);

    }
    @Test
    @Feature("Role")
    @Story("Список доступных полиси для настройки ролей")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка полиcи")
    @Description("Тест получает список доступных полиси для настройки ролей")
    public void testGetPolicies() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsRole.getODataRole();

        AssertionsRole.oDataRoleReturnsData(response);
    }
}
