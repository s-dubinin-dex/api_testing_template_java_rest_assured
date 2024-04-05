package ru.dexit.admindev.apiMethods.admin;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModelNotNull;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModelNotNull;
import ru.dexit.admindev.spec.SpecificationsServer;

public class CoreApiMethodsEmployee extends TestBase {

    // TODO: Вынести SetBaseUrl в базовый метод

    @Step("Создание сотрудника")
    public static Response addEmployee(AddEmployeeRequestModel body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .post("/Employee/AddEmployee")
                .andReturn();

    }

    @Step("Создание сотрудника c неполной моделью данных")
    public static Response addEmployee(AddEmployeeRequestModelNotNull body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .post("/Employee/AddEmployee")
                .andReturn();

    }

    @Step("Обновление данных сотрудника")
    public static Response updateEmployee(UpdateEmployeeRequestModel body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .put("/Employee/UpdateEmployee")
                .andReturn();
    }

    @Step("Обновление данных сотрудника c неполной моделью данных")
    public static Response updateEmployee(UpdateEmployeeRequestModelNotNull body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .put("/Employee/UpdateEmployee")
                .andReturn();
    }

    @Step("Генерация нового приглашения с новым токеном активации")
    public static Response updateInvitation(String employeeId){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body("\"" + employeeId + "\"")
                .when()
                .put("/Employee/UpdateInvitation")
                .andReturn();
    }

    @Step("Генерация нового приглашения с новым токеном активации без тела запроса")
    public static Response updateInvitation(){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .when()
                .put("/Employee/UpdateInvitation")
                .andReturn();
    }

    @Step("Получение данных через протокол oData")
    public static Response oDataEmployee(){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .when()
                .get("/odata/Employee")
                .andReturn();
    }

    @Step("Получение данных через протокол oData c параметром IncludeDeleted")
    public static Response oDataEmployeeWithIncludeDeletedParameter(Boolean includeDeleted){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .param("includeDeleted", includeDeleted)
                .when()
                .get("/odata/Employee")
                .andReturn();
    }

    @Step("Удаление сотрудника")
    public static Response deleteEmployee(String employeeId){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .queryParam("employeeId", employeeId)
                .when()
                .delete("/Employee/DeleteEmployee")
                .andReturn();
    }

    @Step("Удаление сотрудника без тела запроса")
    public static Response deleteEmployee(){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .when()
                .delete("/Employee/DeleteEmployee")
                .andReturn();
    }
}
