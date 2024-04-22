package ru.dexit.admindev.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.assertions.AssertionsEmployee;
import ru.dexit.admindev.assertions.AssertionsRole;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsEmployee;
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

import java.util.List;

import static ru.dexit.admindev.data.DataGenerator.engLetters;
import static ru.dexit.admindev.data.DataGenerator.generateRandomString;

@DisplayName("Расширенные позитивные тесты")
public class ExtendedPositiveTests extends TestBase {

    @Epic("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание сотрудника c валидным именем:")
    @Description("Тест создаёт сотрудника с валидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidEmployeeNames")
    public void testAddEmployeeWithValidNames(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.getRoleUUID())
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        String name = faker.name().fullName();

        AddEmployeeRequestModel requestBodyFirstEmployee = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .email(faker.internet().emailAddress())
                .build();
        CoreApiMethodsEmployee.addEmployee(requestBodyFirstEmployee);

        AddEmployeeRequestModel requestBodySecondEmployee = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.getRoleUUID())
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().fullName())
                .roleId(Role.FULL_WRITE.getRoleUUID())
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyCreation.id)
                .name(faker.name().fullName())
                .roleId(Role.FULL_READ.getRoleUUID())
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyCreation.id)
                .name(name)
                .roleId(Role.FULL_READ.getRoleUUID())
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyCreation.id)
                .name(faker.name().fullName())
                .roleId(role)
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);

        AssertionsEmployee.employeeUpdatedSuccessfully(response, requestBody, responseBodyCreation);
    }
    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Генерация нового приглашения с новым токеном активации с валидным ID")
    @Description("Тест генерирует приглашение с новым токеном активации с валидным ID")
    @Test
    public void testUpdateInvitationWithValidRole(){
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
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос данных через протокол OData без передачи параметра IncludeDeleted")
    @Description("Тест запрашивает данные через протокол oData без передачи параметра IncludeDeleted")
    @Test
    public void testGetODataEmployeeWithoutIncludeDeleteParam(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsEmployee.oDataEmployee();

        AssertionsEmployee.oDataEmployeeReturnsData(response);
    }

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос данных через протокол OData c передачей параметра IncludeDeleted = True")
    @Description("Тест запрашивает данные через протокол oData c передачей параметра IncludeDeleted = True")
    @Test
    public void testGetODataEmployeeWithIncludeDeleteParamEqualTrue(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsEmployee.oDataEmployeeWithIncludeDeletedParameter(true);

        AssertionsEmployee.oDataEmployeeReturnsData(response);
    }

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос данных через протокол OData c передачей параметра IncludeDeleted = False")
    @Description("Тест запрашивает данные через протокол oData c передачей параметра IncludeDeleted = False")
    @Test
    public void testGetODataEmployeeWithIncludeDeleteParamEqualFalse(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsEmployee.oDataEmployeeWithIncludeDeletedParameter(false);

        AssertionsEmployee.oDataEmployeeReturnsData(response);
    }

    @Test
    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Удаление сотрудника с валидным ID")
    @Description("Тест удаляет сотрудника с валидным ID")
    public void testDeleteEmployeeWithValidId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200EmptyStringBody());
        CoreApiMethodsEmployee.deleteEmployee(responseBodyCreation.id);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание роли с валидными наименованиями")
    @Description("Тест создаёт роль с валидными наименованиями")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidRoleNames")
    public void testAddRoleWithValidNames(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(name)
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);

        AssertionsRole.roleAddedSuccessfully(response, requestBody);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание роли с валидной политикой из группы read")
    @Description("Тест создаёт роль с валидной политикой из группы read")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getReadPoliciesStream")
    public void testAddRoleWithValidReadPolicies(String policy){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(faker.company().profession() + "_" + DataGenerator.getSalt())
                .policies(List.of(policy))
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);

        AssertionsRole.roleAddedSuccessfully(response, requestBody);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание роли с валидной политикой из группы write")
    @Description("Тест создаёт роль с валидной политикой из группы write")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getWritePoliciesStream")
    public void testAddRoleWithValidWritePolicies(String policy){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(faker.company().profession() + "_" + DataGenerator.getSalt())
                .policies(List.of(policy))
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);

        AssertionsRole.roleAddedSuccessfully(response, requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание роли со всеми политиками")
    @Description("Тест создаёт роль со всеми политиками")
    public void testAddRoleWithAllPolicies(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(faker.company().profession() + "_" + DataGenerator.getSalt())
                .policies(DataGenerator.getAllPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);

        AssertionsRole.roleAddedSuccessfully(response, requestBody);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение роли с валидными наименованиями")
    @Description("Тест изменяет роль с валидными наименованиями")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getValidRoleNames")
    public void testUpdateRoleWithValidNames(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddRoleRequestModel requestBodyAddRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseAddRole = CoreApiMethodsRole.addRole(requestBodyAddRole);
        RoleCommonResponseModel responseBodyAddRole = responseAddRole.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(name)
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyAddRole.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);

        AssertionsRole.roleUpdatedSuccessfully(response, requestBody);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение роли с валидной политикой из группы read")
    @Description("Тест изменяет роль с валидной политикой из группы read")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getReadPoliciesStream")
    public void testUpdateRoleWithValidReadPolicies(String policy){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddRoleRequestModel requestBodyAddRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseAddRole = CoreApiMethodsRole.addRole(requestBodyAddRole);
        RoleCommonResponseModel responseBodyAddRole = responseAddRole.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession()+ "_" + generateRandomString(engLetters, 6))
                .policies(List.of(policy))
                .id(responseBodyAddRole.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);

        AssertionsRole.roleUpdatedSuccessfully(response, requestBody);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение роли с валидной политикой из группы write")
    @Description("Тест изменяет роль с валидной политикой из группы write")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getWritePoliciesStream")
    public void testUpdateRoleWithValidWritePolicies(String policy){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddRoleRequestModel requestBodyAddRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseAddRole = CoreApiMethodsRole.addRole(requestBodyAddRole);
        RoleCommonResponseModel responseBodyAddRole = responseAddRole.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession()+ "_" + generateRandomString(engLetters, 6))
                .policies(List.of(policy))
                .id(responseBodyAddRole.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);

        AssertionsRole.roleUpdatedSuccessfully(response, requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение роли со всеми политиками")
    @Description("Тест изменяет роль со всеми политиками")
    public void testUpdateRoleWithAllPolicies(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());

        AddRoleRequestModel requestBodyAddRole = DataGenerator.getRandomAddRoleRequestModel();
        Response responseAddRole = CoreApiMethodsRole.addRole(requestBodyAddRole);
        RoleCommonResponseModel responseBodyAddRole = responseAddRole.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession()+ "_" + generateRandomString(engLetters, 6))
                .policies(DataGenerator.getAllPolicies())
                .id(responseBodyAddRole.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);

        AssertionsRole.roleUpdatedSuccessfully(response, requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение роли с передачей существующего id")
    @Description("Тест изменяет роль с передачей существующего id")
    public void testUpdateRoleWithExistingID(){
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
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Удаление роли с валидным id")
    @Description("Тест удаляет роль с валидными данными")
    public void testDeleteRoleWithValidData() {
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
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение списка полиcи")
    @Description("Тест получает список доступных полиси для настройки ролей")
    public void testGetPoliciesWithValidData() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsRole.getPolicies();

        AssertionsRole.policiesGotSuccessfully(response);
    }

    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос данных через протокол OData без передачи параметра IncludeDeleted")
    @Description("Тест запрашивает данные через протокол oData без передачи параметра IncludeDeleted")
    public void testGetODataRoleWithoutIncludeDeletedParam() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsRole.getODataRole();

        AssertionsRole.oDataRoleReturnsData(response);
    }

    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос данных через протокол OData c передачей параметра IncludeDeleted = True")
    @Description("Тест запрашивает данные через протокол oData c передачей параметра IncludeDeleted = True")
    public void testGetODataRoleWithIncludeDeletedParamEqualTrue() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsRole.getODataRoleWithIncludeDeletedParameter(true);

        AssertionsRole.oDataRoleReturnsData(response);
    }

    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Запрос данных через протокол OData c передачей параметра IncludeDeleted = False")
    @Description("Тест запрашивает данные через протокол oData c передачей параметра IncludeDeleted = False")
    public void testGetODataRoleWithIncludeDeletedParamEqualFalse() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        Response response = CoreApiMethodsRole.getODataRoleWithIncludeDeletedParameter(false);

        AssertionsRole.oDataRoleReturnsData(response);
    }
}
