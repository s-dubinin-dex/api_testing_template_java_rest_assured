package ru.dexit.admindev.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsEmployee;
import ru.dexit.admindev.apiMethods.admin.CoreApiMethodsRole;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.data.Role;
import ru.dexit.admindev.models.employee.*;
import ru.dexit.admindev.models.errors.ErrorModel;
import ru.dexit.admindev.models.role.*;
import ru.dexit.admindev.spec.RequestSpecifications;
import ru.dexit.admindev.spec.ResponseSpecifications;
import ru.dexit.admindev.spec.SpecificationsServer;

import static org.assertj.core.api.Assertions.*;
import static ru.dexit.admindev.helpers.ErrorDescription.*;

@DisplayName("Негативные тесты с невалидными данными (по типу данных)")
public class InvalidDataNegativeTests extends TestBase {

    // TODO: Переписать DisplayName и Description на те, которые подробно описывают финальный результат

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание сотрудника без авторизации")
    @Description("Тест пытается создать сотрудника без авторизации")
    @Test
    public void testAddEmployeeWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        AddEmployeeRequestModel requestBody = DataGenerator.getRandomAddEmployeeRequestModel();
        CoreApiMethodsEmployee.addEmployee(requestBody);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание сотрудника с пустым токеном")
    @Description("Тест пытается создать сотрудника с переданным заголовком авторизации, но пустым токеном")
    @Test
    public void testAddEmployeeWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        AddEmployeeRequestModel requestBody = DataGenerator.getRandomAddEmployeeRequestModel();
        CoreApiMethodsEmployee.addEmployee(requestBody);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание сотрудника с невалидным токеном")
    @Description("Тест пытается создать сотрудника с невалидным токеном")
    @Test
    public void testAddEmployeeWithInvalidToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        AddEmployeeRequestModel requestBody = DataGenerator.getRandomAddEmployeeRequestModel();
        CoreApiMethodsEmployee.addEmployee(requestBody);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание сотрудника с неполной моделью данных - не передан name")
    @Description("Тест пытается создать сотрудника с неполной моделью данных - не передан name")
    @Test
    public void testAddEmployeeWithoutNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModelNotNull requestBody = AddEmployeeRequestModelNotNull.builder()
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .email(faker.internet().emailAddress())
                .build();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание сотрудника с неполной моделью данных - передан только name")
    @Description("Тест пытается создать сотрудника с неполной моделью данных - передан только name")
    @Test
    public void testAddEmployeeWithOnlyNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModelNotNull requestBody = AddEmployeeRequestModelNotNull.builder()
                .name(faker.name().fullName())
                .build();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmail().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание сотрудника с неполной моделью данных - не передан roleId")
    @Description("Тест пытается создать сотрудника с неполной моделью данных - не передан roleId")
    @Test
    public void testAddEmployeeWithoutRoleIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        AddEmployeeRequestModelNotNull requestBody = AddEmployeeRequestModelNotNull.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .build();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание сотрудника с неполной моделью данных - передан только RoleId")
    @Description("Тест пытается создать сотрудника с неполной моделью данных - передан только RoleId")
    @Test
    public void testAddEmployeeWithOnlyRoleIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModelNotNull requestBody = AddEmployeeRequestModelNotNull.builder()
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .build();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmail().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание сотрудника с неполной моделью данных - не передан email")
    @Description("Тест пытается создать сотрудника с неполной моделью данных - не передан email")
    @Test
    public void testAddEmployeeWithoutEmailIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModelNotNull requestBody = AddEmployeeRequestModelNotNull.builder()
                .name(faker.name().fullName())
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .build();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmail().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание сотрудника с неполной моделью данных - передан только email")
    @Description("Тест пытается создать сотрудника с неполной моделью данных - передан только email")
    @Test
    public void testAddEmployeeWithOnlyEmailIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddEmployeeRequestModelNotNull requestBody = AddEmployeeRequestModelNotNull.builder()
                .email(faker.internet().emailAddress())
                .build();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение сотрудника без авторизации")
    @Description("Тест пытается изменить сотрудника без авторизации")
    @Test
    public void testUpdateEmployeeWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.getRoleUUID())
                .build();

        CoreApiMethodsEmployee.updateEmployee(requestBody);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение сотрудника c пустым токеном")
    @Description("Тест пытается изменить сотрудника с переданным заголовком авторизации, но пустым токеном")
    @Test
    public void testUpdateEmployeeWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.getRoleUUID())
                .build();

        CoreApiMethodsEmployee.updateEmployee(requestBody);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение сотрудника c невалидным токеном")
    @Description("Тест пытается изменить сотрудника с невалидным токеном")
    @Test
    public void testUpdateEmployeeWithInvalidToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        UpdateEmployeeRequestModel requestBody = UpdateEmployeeRequestModel.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .roleId(Role.FULL_READ.getRoleUUID())
                .build();

        CoreApiMethodsEmployee.updateEmployee(requestBody);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение сотрудника с неполной моделью данных - не передан id")
    @Description("Тест пытается изменить сотрудника с неполной моделью данных - не передан id")
    @Test
    public void testUpdateEmployeeWithoutIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        UpdateEmployeeRequestModelNotNull requestBody = UpdateEmployeeRequestModelNotNull.builder()
                .name(faker.name().fullName())
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionEmployeeInfo);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение сотрудника с неполной моделью данных - передан только id")
    @Description("Тест пытается измененить сотрудника с неполной моделью данных - передан только id")
    @Test
    public void testUpdateEmployeeWithOnlyIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateEmployeeRequestModelNotNull requestBody = UpdateEmployeeRequestModelNotNull.builder()
                .id(faker.internet().uuid())
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsNameFieldIsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение сотрудника с неполной моделью данных - не передан name")
    @Description("Тест пытается изменить сотрудника с неполной моделью данных - не передан name")
    @Test
    public void testUpdateEmployeeWithoutNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateEmployeeRequestModelNotNull requestBody = UpdateEmployeeRequestModelNotNull.builder()
                .id(faker.internet().uuid())
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsNameFieldIsRequired);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение сотрудника с неполной моделью данных - передан только name")
    @Description("Тест пытается изменить сотрудника с неполной моделью данных - передан только name")
    @Test
    public void testUpdateEmployeeWithOnlyNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        UpdateEmployeeRequestModelNotNull requestBody = UpdateEmployeeRequestModelNotNull.builder()
                .name(faker.name().fullName())
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение сотрудника с неполной моделью данных - не передан roleId")
    @Description("Тест пытается изменить сотрудника с неполной моделью данных - не передан roleId")
    @Test
    public void testUpdateEmployeeWithoutRoleIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        UpdateEmployeeRequestModelNotNull requestBody = UpdateEmployeeRequestModelNotNull.builder()
                .id(faker.internet().uuid())
                .name(faker.name().fullName())
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);
    }

    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение сотрудника с неполной моделью данных - передан только roleId")
    @Description("Тест пытается изменить сотрудника с неполной моделью данных - передан только roleId")
    @Test
    public void testUpdateEmployeeWithOnlyRoleIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateEmployeeRequestModelNotNull requestBody = UpdateEmployeeRequestModelNotNull.builder()
                .roleId(Role.FULL_WRITE.getRoleUUID())
                .build();
        Response response = CoreApiMethodsEmployee.updateEmployee(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsNameFieldIsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Генерация нового приглашения с новым токеном активации без авторизации")
    @Description("Тест генерирует приглашение с новым токеном активации без авторизации")
    @Test
    public void testUpdateInvitationWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.updateInvitation(faker.internet().uuid());
    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Генерация нового приглашения с новым токеном активации c пустым токеном")
    @Description("Тест генерирует приглашение с новым токеном активации с переданным заголовком авторизации, но пустым токеном")
    @Test
    public void testUpdateInvitationWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.updateInvitation(faker.internet().uuid());
    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Генерация нового приглашения с новым токеном активации с невалидным токеном")
    @Description("Тест генерирует приглашение с новым токеном активации с невалидным токеном")
    @Test
    public void testUpdateInvitationWithInvalidToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.updateInvitation(faker.internet().uuid());
    }

    @Feature("Employee")
    @Story("Генерация приглашения")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Генерация нового приглашения с новым токеном активации с пустым id")
    @Description("Тест генерирует приглашение с новым токеном активации с пустым id")
    @Test
    public void testUpdateInvitationWithEmptyId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        Response response = CoreApiMethodsEmployee.updateInvitation();
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
    }

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData без авторизации")
    @Description("Тест запрашивает данные через протокол oData без авторизации")
    @Test
    public void testGetODataEmployeeWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.oDataEmployee();
    }

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData с пустым токеном")
    @Description("Тест запрашивает данные через протокол oData с пустым токеном")
    @Test
    public void testGetODataEmployeeWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.oDataEmployee();
    }

    @Feature("Employee")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData с невалидным токеном")
    @Description("Тест запрашивает данные через протокол с невалидным токеном")
    @Test
    public void testGetODataEmployeeWithoutToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.oDataEmployee();
    }

    @Test
    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление сотрудника без авторизации")
    @Description("Тест удаляет сотрудника без авторизации")
    public void testDeleteEmployeeWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.deleteEmployee(faker.internet().uuid());
    }

    @Test
    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление сотрудника с пустым токеном")
    @Description("Тест удаляет сотрудника с пустым токеном")
    public void testDeleteEmployeeWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.deleteEmployee(faker.internet().uuid());
    }
    @Test
    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление сотрудника с невалидным токеном")
    @Description("Тест удаляет сотрудника с невалидным токеном")
    public void testDeleteEmployeeWithInvalidToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsEmployee.deleteEmployee(faker.internet().uuid());
    }

    @Test
    @Feature("Employee")
    @Story("Удаление сотрудника")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Удаление сотрудника с пустым id")
    @Description("Тест удаляет сотрудника с пустым id")
    public void testDeleteEmployeeWithEmptyId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        Response response = CoreApiMethodsEmployee.deleteEmployee();
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getEmployeeId().get(0).getErrorCode()).isEqualTo(validationsEmployeeIdFieldIsRequired);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание роли без авторизации")
    @Description("Тест создаёт роль без авторизации")
    public void testAddRoleWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        AddRoleRequestModel requestBody = DataGenerator.getRandomAddRoleRequestModel();
        CoreApiMethodsRole.addRole(requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание роли с пустым токеном")
    @Description("Тест создаёт роль с пустым токеном")
    public void testAddRoleWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        AddRoleRequestModel requestBody = DataGenerator.getRandomAddRoleRequestModel();
        CoreApiMethodsRole.addRole(requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание роли c невалидным токеном")
    @Description("Тест создаёт роль c невалидным токеном")
    public void testAddRoleWithInvalidToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        AddRoleRequestModel requestBody = DataGenerator.getRandomAddRoleRequestModel();
        CoreApiMethodsRole.addRole(requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание роли с неполной моделью данных - не передан name")
    @Description("Тест создаёт роль с неполной моделью данных - не передан name")
    public void testAddRoleWithoutNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModelNotNull requestBody = AddRoleRequestModelNotNull.builder()
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание роли с неполной моделью данных - передан только name")
    @Description("Тест создаёт роль с неполной моделью данных - передан только name")
    public void testAddRoleWithOnlyNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModelNotNull requestBody = AddRoleRequestModelNotNull.builder()
                .name(faker.company().profession())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание роли с неполной моделью данных - не передан policies")
    @Description("Тест создаёт роль с неполной моделью данных - не передан policies")
    public void testAddRoleWithoutPoliciesInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModelNotNull requestBody = AddRoleRequestModelNotNull.builder()
                .name(faker.company().profession())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Test
    @Feature("Role")
    @Story("Создание роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создание роли с неполной моделью данных - передан только policies")
    @Description("Тест создаёт роль с неполной моделью данных - передан только policies")
    public void testAddRoleWithOnlyPoliciesInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        AddRoleRequestModelNotNull requestBody = AddRoleRequestModelNotNull.builder()
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.addRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Test
    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение роли без авторизации")
    @Description("Тест изменяет роль без авторизации")
    public void testUpdateRoleWithoutAuthorization(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(faker.internet().uuid())
                .build();
        CoreApiMethodsRole.updateRole(requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение роли с пустым токеном")
    @Description("Тест изменяет роль с пустым токеном")
    public void testUpdateRoleWithEmptyToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(faker.internet().uuid())
                .build();
        CoreApiMethodsRole.updateRole(requestBody);
    }

    @Test
    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение роли c невалидным токеном")
    @Description("Тест измененяет роль c невалидным токеном")
    public void testUpdateRoleWithInvalidToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        UpdateRoleRequestModel requestBody = UpdateRoleRequestModel.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .id(faker.internet().uuid())
                .build();
        CoreApiMethodsRole.updateRole(requestBody);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение роли с неполной моделью данных - не передан name")
    @Description("Тест пытается изменить роль с неполной моделью данных - не передан name")
    @Test
    public void testUpdateRoleWithoutNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateRoleRequestModelNotNull requestBody = UpdateRoleRequestModelNotNull.builder()
                .policies(DataGenerator.getDefaultPolicies())
                .id(faker.internet().uuid())
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение роли с неполной моделью данных - передан только name")
    @Description("Тест пытается изменить роль с неполной моделью данных - передан только name")
    @Test
    public void testUpdateRoleWithOnlyNameInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateRoleRequestModelNotNull requestBody = UpdateRoleRequestModelNotNull.builder()
                .name(faker.name().fullName())
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение роли с неполной моделью данных - не передан policies")
    @Description("Тест пытается изменить роль с неполной моделью данных - не передан policies")
    @Test
    public void testUpdateRoleWithoutPoliciesInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateRoleRequestModelNotNull requestBody = UpdateRoleRequestModelNotNull.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .id(faker.internet().uuid())
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getPolicies().get(0).getErrorCode()).isEqualTo(validationsRequired);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение роли с неполной моделью данных - передан только policies")
    @Description("Тест пытается изменить роль с неполной моделью данных - передан только policies")
    @Test
    public void testUpdateRoleWithOnlyPoliciesInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateRoleRequestModelNotNull requestBody = UpdateRoleRequestModelNotNull.builder()
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение роли с неполной моделью данных - не передан id")
    @Description("Тест пытается изменить роль с неполной моделью данных - не передан id")
    @Test
    public void testUpdateRoleWithoutIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec404());

        UpdateRoleRequestModelNotNull requestBody = UpdateRoleRequestModelNotNull.builder()
                .name(faker.company().profession() + DataGenerator.getSalt())
                .policies(DataGenerator.getDefaultPolicies())
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(exceptionOfTypeEntityNotFoundExceptionRoleInfo);

    }

    @Feature("Role")
    @Story("Изменение роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Изменение роли с неполной моделью данных - передан только id")
    @Description("Тест пытается изменить роль с неполной моделью данных - передан только id")
    @Test
    public void testUpdateRoleWithOnlyIdInModel(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        UpdateRoleRequestModelNotNull requestBody = UpdateRoleRequestModelNotNull.builder()
                .id(faker.internet().uuid())
                .build();
        Response response = CoreApiMethodsRole.updateRole(requestBody);
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getTitle()).isEqualTo(requestValidationError);
        assertThat(responseBody.getErrors().getName().get(0).getErrorCode()).isEqualTo(validationsRequired);
        assertThat(responseBody.getErrors().getName().get(1).getErrorCode()).isEqualTo(validationsInvalidName);
    }

    @Test
    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление роли без авторизации")
    @Description("Тест удаляет без авторизации")
    public void testDeleteRoleWithoutAuthorization() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsRole.deleteRole(faker.internet().uuid());
    }

    @Test
    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление роли с пустым токеном")
    @Description("Тест удаляет с пустым токеном")
    public void testDeleteRoleWithWithEmptyToken() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsRole.deleteRole(faker.internet().uuid());
    }

    @Test
    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление роли с невалидным токеном")
    @Description("Тест удаляет с невалидным токеном")
    public void testDeleteRoleWithWithInvalidToken() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec401());

        CoreApiMethodsRole.deleteRole(faker.internet().uuid());
    }

    @Test
    @Feature("Role")
    @Story("Удаление роли")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Удаление роли с пустым id")
    @Description("Тест роли сотрудника с пустым id")
    public void testDeleteRoleWithEmptyId(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec400());

        Response response = CoreApiMethodsRole.deleteRole();
        ErrorModel responseBody = response.as(ErrorModel.class);

        assertThat(responseBody.getMessage()).isEqualTo(idIsEmpty);
    }

    @Test
    @Feature("Role")
    @Story("Список доступных полиси для настройки ролей")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка полиcи без авторизации")
    @Description("Тест получает список доступных полиси без авторизации")
    public void testGetPoliciesWithoutAuthorization() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec403());

        CoreApiMethodsRole.getPolicies();
    }

    @Test
    @Feature("Role")
    @Story("Список доступных полиси для настройки ролей")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка полиcи с пустым токеном")
    @Description("Тест получает список доступных полиси с пустым токеном")
    public void testGetPoliciesWithEmptyToken() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec403());

        CoreApiMethodsRole.getPolicies();
    }

    @Test
    @Feature("Role")
    @Story("Список доступных полиси для настройки ролей")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка полиcи с невалидным токеном")
    @Description("Тест получает список доступных полиси с невалидным токеном")
    public void testGetPoliciesWithInvalidToken() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec403());

        CoreApiMethodsRole.getPolicies();
    }

    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData без авторизации")
    @Description("Тест запрашивает данные через протокол oData без авторизации")
    public void testGetODataRoleWithoutAuthorization() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithoutAuthorization());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec403());

        CoreApiMethodsRole.getODataRole();
    }

    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData с пустым токеном")
    @Description("Тест запрашивает данные через протокол с пустым токеном")
    public void testGetODataRoleWithEmptyToken() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithEmptyToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec403());

        CoreApiMethodsRole.getODataRole();
    }

    @Test
    @Feature("Role")
    @Story("Интерфейс запроса данных через протокол OData")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Запрос данных через протокол OData с невалидным токеном")
    @Description("Тест запрашивает данные через протокол с невалидным токеном")
    public void testGetODataRoleWithInvalidToken() {
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecificationWithInvalidToken());
        SpecificationsServer.installResponseSpecification(ResponseSpecifications.responseSpec403());

        CoreApiMethodsRole.getODataRole();
    }
}
