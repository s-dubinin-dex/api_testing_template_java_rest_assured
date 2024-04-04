package ru.dexit.admindev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Employee;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsEmployee;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsRole;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.EmployeeCommonResponseModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import ru.dexit.admindev.models.errors.ErrorModel;
import ru.dexit.admindev.models.role.AddRoleRequestModel;
import ru.dexit.admindev.models.role.RoleCommonResponseModel;
import ru.dexit.admindev.models.role.UpdateRoleRequestModel;
import ru.dexit.admindev.spec.RequestSpecifications;
import ru.dexit.admindev.spec.ResponseSpecifications;
import ru.dexit.admindev.spec.SpecificationsServer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Негативные тесты с валидными данными (по типу данных)")
public class ValidDataNegativeTests extends TestBase{

    private final String duplicateKeyEmployeeEmail = "duplicate key value violates unique constraint \"IX_Employee_Email\"";
    private final String duplicateKeyRoleName = "duplicate key value violates unique constraint \"IX_Role_Name\"";
    private final String exceptionOfTypeEntityInUseExceptionRoleInfo = "Exception of type 'Shared.Domain.Exceptions.EntityInUseException`1[Admin.Application.Abstraction.Models.Queries.Role.RoleInfo]' was thrown.";
    private final String exceptionOfTypeEntityNotFoundExceptionEmployeeInfo = "Exception of type 'Shared.Domain.Exceptions.EntityNotFoundException`1[Admin.Application.Abstraction.Models.Queries.Employee.EmployeeInfo]' was thrown.";
    private final String exceptionOfTypeEntityNotFoundExceptionRoleInfo = "Exception of type 'Shared.Domain.Exceptions.EntityNotFoundException`1[Admin.Application.Abstraction.Models.Queries.Role.RoleInfo]' was thrown.";
    private final String noAccessRoleIsReadOnly = "NoAccess role is read-only";
    private final String validationsEditRoleFieldIsRequired = "validations.The editRole field is required.";
    private final String validationsInvalidName = "validations.InvalidName";
    private final String validationsInvalidEmail = "validations.InvalidEmail";
    private final String validationsMaxLengthExceeded = "validations.MaxLengthExceeded";
    private final String validationsNewEmployeeFieldIsRequired = "validations.The newEmployee field is required.";
    private final String validationsRequired = "validations.Required";
    private final String validationsTryingToApplyNonExistentPolicy = "validations.TryingToApplyNonExistentPolicy";
    private final String validationsUpdateEmployeeFieldIsRequired = "validations.The updateEmployee field is required.";
    private final String requestValidationError = "Request Validation Error";
    private final String userWithSuperAdminRoleIsReadOnly = "User with SuperAdmin role is read-only";

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с пустым именем")
    @Description("Тест пытается создать сотрудника с пустым именем")
    @Test
    public void testAddEmployeeWithEmptyName(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name("")
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel errorModel = response.as(ErrorModel.class);

        assertThat(errorModel.getTitle()).isEqualTo(requestValidationError);
        assertThat(errorModel.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(errorModel.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);

    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с невалидными именами")
    @Description("Тест пытается создать сотрудника с невалидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeNames")
    public void testAddEmployeeWithInvalidName(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(name)
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel errorModel = response.as(ErrorModel.class);

        assertThat(errorModel.getTitle()).isEqualTo(requestValidationError);
        assertThat(errorModel.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsInvalidName);

    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с невалидными ролями")
    @Description("Тест пытается создать сотрудника с невалидными ролями")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
    public void testAddEmployeeWithInvalidRole(String roleUUID){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getNewEmployee().get(0).getErrorCode()).isEqualTo(validationsNewEmployeeFieldIsRequired);
    }
    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с ролью \"Полные права\"")
    @Description("Тест пытается создать сотрудника с ролью \"Полные права\"")
    public void testAddEmployeeWithRoleFullRights(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_RIGHTS.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(userWithSuperAdminRoleIsReadOnly);
    }

    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с ID несуществующей роли")
    @Description("Тест пытается создать сотрудника с ID несуществующей роли")
    public void testAddEmployeeWithNotExistRole(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(faker.internet().uuid())
                .email(faker.internet().emailAddress())
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с существующим email")
    @Description("Тест пытается создать сотрудника с существующим email")
    public void testAddEmployeeWithExistEmail(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseCreationModel = responseCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec409());
        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .email(responseCreationModel.email)
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(duplicateKeyEmployeeEmail);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с невалидным email")
    @Description("Тест пытается создать сотрудника с невалидным email")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmails")
    public void testAddEmployeeWithInvalidEmail(String email){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .email(email)
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmail().get(0).getErrorCode()).isEqualTo(validationsInvalidEmail);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание сотрудника с email, превышающим допустимую длину")
    @Description("Тест пытается создать сотрудника с email, превышающим допустимую длину")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmailsExceedLength")
    public void testAddEmployeeWithInvalidEmailExceedLength(String email){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModel requestBody = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .email(email)
                .build();

        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmail().get(0).getErrorCode()).isEqualTo(validationsMaxLengthExceeded);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c невалидным ID")
    @Description("Тест изменяет сотрудника c невалидным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeIDs")
    public void testUpdateEmployeeWithInvalidId(String id){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(id)
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getUpdateEmployee().get(0).getErrorCode()).isEqualTo(validationsUpdateEmployeeFieldIsRequired);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c несуществующим ID")
    @Description("Тест изменяет сотрудника c несуществующим ID")
    @Test
    public void testUpdateEmployeeWithNotExistId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);
        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionEmployeeInfo);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c ID администратора")
    @Description("Тест изменяет сотрудника c ID администратора")
    @Test
    public void testUpdateEmployeeWithAdministratorId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(Employee.ADMINISTRATOR.id)
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(userWithSuperAdminRoleIsReadOnly);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c пустым именем")
    @Description("Тест изменяет сотрудника c пустым именем")
    @Test
    public void testUpdateEmployeeWithEmptyName(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name("")
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотрудника c невалидным именем")
    @Description("Тест изменяет сотрудника c невалидным именем")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeNames")
    public void testUpdateEmployeeWithInvalidName(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(name)
                .roleId(Role.FULL_READ.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотруднику роли на невалидную roleID")
    @Description("Изменение сотруднику роли на невалидную roleID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
    public void testUpdateEmployeeWithInvalidID(String id){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(responseBodyForCreation.name)
                .roleId(id)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getUpdateEmployee().get(0).getErrorCode()).isEqualTo(validationsUpdateEmployeeFieldIsRequired);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотруднику роли на \"Полные права\"")
    @Description("Изменение сотруднику роли на \"Полные права\"")
    @Test
    public void testUpdateEmployeeWithFullRightsRoleID(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(responseBodyForCreation.name)
                .roleId(Role.FULL_RIGHTS.roleUUID)
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(userWithSuperAdminRoleIsReadOnly);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение сотруднику роли на роль c несуществующим roleID")
    @Description("Изменение сотруднику роли на роль c несуществующим roleID")
    @Test
    public void testUpdateEmployeeWithNotExistRoleID(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddEmployeeRequestModel requestBodyForCreation = DataGenerator.getRandomAddEmployeeRequestModel();
        Response responseForCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForCreation);
        EmployeeCommonResponseModel responseBodyForCreation = responseForCreation.as(EmployeeCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());
        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(responseBodyForCreation.id)
                .name(responseBodyForCreation.name)
                .roleId(faker.internet().uuid())
                .build();

        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Генерация приглашения сотруднику с невалидным ID")
    @Description("Генерация приглашения сотруднику с невалидным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidEmployeeIDs")
    public void testUpdateInvitationWithInvalidID(String id){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        Response response = CoreApiMethodsEmployee.updateInvitation(id);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Генерация приглашения сотруднику с несуществующим ID")
    @Description("Генерация приглашения сотруднику с несуществующим ID")
    @Test
    public void testUpdateInvitationWithNotExistID(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        Response response = CoreApiMethodsEmployee.updateInvitation(faker.internet().uuid());
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionEmployeeInfo);
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
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        Response response = CoreApiMethodsEmployee.deleteEmployee(id);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmployeeId().get(0).getErrorCode()).startsWith("validations.The value");
    }

    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление сотрудника с несуществующим ID")
    @Description("Удаление сотрудника с несуществующим ID")
    @Test
    public void testDeleteEmployeeWithNotExistID(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        Response response = CoreApiMethodsEmployee.deleteEmployee(faker.internet().uuid());

        ErrorModel responseBody = response.as(ErrorModel.class);
        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionEmployeeInfo);
    }

    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление сотрудника с полными правами")
    @Description("Попытка удаления сотрудника с полными правами")
    @Test
    public void testDeleteEmployeeWithFullRightsIsForbidden(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        Response response = CoreApiMethodsEmployee.deleteEmployee(Employee.ADMINISTRATOR.id);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(userWithSuperAdminRoleIsReadOnly);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с пустым именем")
    @Description("Тест пытается создать роль с пустым именем")
    @Test
    public void testAddRoleWithEmptyName(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name("")
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с невалидными именами")
    @Description("Тест пытается создать роль с невалидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleNames")
    public void testAddRoleWithInvalidName(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(name)
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с существующим именем")
    @Description("Тест пытается создать роль с существующим именем")
    @Test
    public void testAddRoleWithExistingName(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec409());
        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(duplicateKeyRoleName);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли без политик")
    @Description("Тест пытается создать роль без политик")
    @Test
    public void testAddRoleWithoutPolicies(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(new ArrayList<>())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание роли с некорректными политиками")
    @Description("Тест пытается создать роль с некорректными политиками")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidPoliciesStream")
    public void testAddRoleWithInvalidPolicies(List<String> policies){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModel requestBody = AddRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(policies)
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsTryingToApplyNonExistentPolicy);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с пустым именем")
    @Description("Тест пытается измененить роль с пустым именем")
    @Test
    public void testUpdateRoleWithEmptyName(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name("")
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyForCreation.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с невалидными именами")
    @Description("Тест пытается измененить роль с невалидными именами")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleNames")
    public void testUpdateRoleWithInvalidName(String name){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(name)
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyForCreation.id)
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли существующим именем")
    @Description("Тест пытается изменить роль существующим именем")
    @Test
    public void testUpdateRoleWithExistingName(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        AddRoleRequestModel requestBodyForUpdate = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForUpdate = CoreApiMethodsRole.addRole(requestBodyForUpdate);
        RoleCommonResponseModel responseBodyForUpdate = responseForUpdate.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec409());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(DataGenerator.getDefaultPolicies())
                .id(responseBodyForUpdate.id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(duplicateKeyRoleName);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли без политик")
    @Description("Тест пытается измененить роль, записывая пустое тело с политиками")
    @Test
    public void testUpdateRoleWithoutPolicies(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(new ArrayList<>())
                .id(responseBodyForCreation.id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с некорректными политиками")
    @Description("Тест пытается измененить роль с некорректными политиками")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidPoliciesStream")
    public void testUpdateRoleWithInvalidPolicies(List<String> policies){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(policies)
                .id(responseBodyForCreation.id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsTryingToApplyNonExistentPolicy);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с некорректными RoleId")
    @Description("Тест пытается измененить роль с некорректными RoleId")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
    public void testUpdateRoleWithInvalidRoleId(String id){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(responseBodyForCreation.policies)
                .id(id)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEditRole().get(0).getErrorCode()).isEqualTo(validationsEditRoleFieldIsRequired);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли с несуществующим RoleId")
    @Description("Тест пытается измененить роль с несуществующим RoleId")
    @Test
    public void testUpdateRoleWithNotExistRoleId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForCreation = CoreApiMethodsRole.addRole(requestBodyForCreation);
        RoleCommonResponseModel responseBodyForCreation = responseForCreation.as(RoleCommonResponseModel.class);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());
        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(responseBodyForCreation.name)
                .policies(responseBodyForCreation.policies)
                .id(faker.internet().uuid())
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли Полные права")
    @Description("Тест пытается изменить роль Полные права")
    @Test
    public void testUpdateFullRightsRole(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(Role.FULL_RIGHTS.roleUUID)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(userWithSuperAdminRoleIsReadOnly);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Изменение роли Нет прав")
    @Description("Тест пытается изменить роль Нет прав")
    @Test
    public void testUpdateNoRightsRole(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(Role.NO_RIGHTS.roleUUID)
                .build();

        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(noAccessRoleIsReadOnly);
    }

    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление роли, назначенной на сотрудника")
    @Description("Тест пытается удалить роль, назначенную на сотрудника")
    @Test
    public void testDeleteRoleAssignedToEmployee(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpecOK200JSONBody());

        AddRoleRequestModel requestBodyForRoleCreation = DataGenerator.getRandomAddRoleRequestModel();
        Response responseForRoleCreation = CoreApiMethodsRole.addRole(requestBodyForRoleCreation);
        RoleCommonResponseModel responseBodyForRoleCreation = responseForRoleCreation.as(RoleCommonResponseModel.class);

        AddEmployeeRequestModel requestBodyForEmployeeCreation = AddEmployeeRequestModel.builder()
                .name(faker.name().firstName())
                .roleId(responseBodyForRoleCreation.id)
                .email(faker.internet().emailAddress())
                .build();

        Response responseForEmployeeCreation = CoreApiMethodsEmployee.addEmployee(requestBodyForEmployeeCreation);

        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec409());
        Response responseForRoleDeleting = CoreApiMethodsRole.deleteRole(responseBodyForRoleCreation.id);
        ErrorModel responseBody = responseForRoleDeleting.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityInUseExceptionRoleInfo);
    }

    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление роли c некорректным ID")
    @Description("Тест пытается удалить роль c некорректным ID")
    @ParameterizedTest
    @MethodSource("ru.dexit.admindev.data.DataGenerator#getInvalidRoleIDs")
    public void testDeleteRoleWithInvalidID(String id){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        Response responseForRoleDeleting = CoreApiMethodsRole.deleteRole(id);
        ErrorModel responseBody = responseForRoleDeleting.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getId().get(0).getErrorCode()).startsWith("validations.The value");
    }

    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление роли c несуществующим ID")
    @Description("Тест пытается удалить роль c несуществующим ID")
    @Test
    public void testDeleteRoleWithNotExistID(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        Response responseForRoleDeleting = CoreApiMethodsRole.deleteRole(faker.internet().uuid());
        ErrorModel responseBody = responseForRoleDeleting.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление роли Полные права")
    @Description("Тест пытается удалить роль Полные права")
    @Test
    public void testDeleteFullRightsRoleIsForbidden(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        Response responseForRoleDeleting = CoreApiMethodsRole.deleteRole(Role.FULL_RIGHTS.roleUUID);
        ErrorModel responseBody = responseForRoleDeleting.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(userWithSuperAdminRoleIsReadOnly);
    }

    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Удаление роли Нет прав")
    @Description("Тест пытается удалить роль Нет прав")
    @Test
    public void testDeleteNoRightsRoleIsForbidden(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec412());

        Response responseForRoleDeleting = CoreApiMethodsRole.deleteRole(Role.NO_RIGHTS.roleUUID);
        ErrorModel responseBody = responseForRoleDeleting.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(noAccessRoleIsReadOnly);
    }
}
