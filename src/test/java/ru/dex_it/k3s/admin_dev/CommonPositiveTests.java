package ru.dex_it.k3s.admin_dev;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dex_it.k3s.admin_dev.assertions.AssertionsEmployee;
import ru.dex_it.k3s.admin_dev.data.DataGenerator;
import ru.dex_it.k3s.admin_dev.helpers.CoreApiMethodsEmployee;
import ru.dex_it.k3s.admin_dev.models.Employee.AddEmployeeRequestModel;
import ru.dex_it.k3s.admin_dev.models.Employee.EmployeeCommonResponseModel;
import ru.dex_it.k3s.admin_dev.models.Employee.UpdateEmployeeRequestModel;

@DisplayName("Общие позитивные тесты. Smoke tests.")
public class CommonPositiveTests extends TestBase{
    @Test
    @Feature("Employee")
    @Story("Создание сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание сотрудника с валидными параметрами")
    @Description("Тест создаёт сотрудника с валидными параметрами")
    public void testCreateValidEmployee(){

        AddEmployeeRequestModel requestBody = DataGenerator.getAddEmployeeRequestModel();
        Response response = CoreApiMethodsEmployee.addEmployee(requestBody);
        AssertionsEmployee.employeeCreatedSuccessfully(response, requestBody);

    }
    @Feature("Employee")
    @Story("Изменение сотрудника")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение сотрудника валидными параметрами")
    @Description("Тест изменяет сотрудника валидными параметрами")
    @Test
    public void testUpdateValidEmployee(){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        UpdateEmployeeRequestModel requestBody = DataGenerator.
                getUpdateEmployeeRequestModel(
                        responseBodyCreation.id,
                        "d2ee530d-8384-439a-8486-3e960118084b"
        );
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

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        Response response = CoreApiMethodsEmployee.updateInvitation(responseBodyCreation.id);
        AssertionsEmployee.invitationCreatedSuccessfully(response, responseBodyCreation);
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
    public void testDeleteValidEmployee(){

        AddEmployeeRequestModel requestBodyCreation = DataGenerator.getAddEmployeeRequestModel();
        Response responseCreation = CoreApiMethodsEmployee.addEmployee(requestBodyCreation);
        EmployeeCommonResponseModel responseBodyCreation = responseCreation.as(EmployeeCommonResponseModel.class);

        Response response = CoreApiMethodsEmployee.deleteEmployee(responseBodyCreation.id);
        AssertionsEmployee.employeeDeletedSuccessfully(response);

    }



}
