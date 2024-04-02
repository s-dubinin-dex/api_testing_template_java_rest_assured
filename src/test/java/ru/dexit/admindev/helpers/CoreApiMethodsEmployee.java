package ru.dexit.admindev.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.models.employee.AddEmployeeRequestModel;
import ru.dexit.admindev.models.employee.UpdateEmployeeRequestModel;
import ru.dexit.admindev.spec.Specifications;

public class CoreApiMethodsEmployee extends TestBase {
    @Step("Создание сотрудника")
    public static Response addEmployee(AddEmployeeRequestModel body){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .body(body)
                .when()
                .post("/Employee/AddEmployee")
                .andReturn();

    }

    @Step("Обновление данных сотрудника")
    public static Response updateEmployee(UpdateEmployeeRequestModel body){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .body(body)
                .when()
                .put("/Employee/UpdateEmployee")
                .andReturn();
    }

    @Step("Генерация нового приглашения с новым токеном активации")
    public static Response updateInvitation(String employeeId){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .body("\"" + employeeId + "\"")
                .when()
                .put("/Employee/UpdateInvitation")
                .andReturn();
    }

    @Step("Получение данных через протокол oData")
    public static Response oDataEmployee(){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .when()
                .get("/odata/Employee")
                .andReturn();
    }

    @Step("Получение данных через протокол oData c параметром IncludeDeleted")
    public static Response oDataEmployeeWithIncludeDeletedParameter(Boolean includeDeleted){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .param("includeDeleted", includeDeleted)
                .when()
                .get("/odata/Employee")
                .andReturn();
    }

    @Step("Удаление сотрудника")
    public static Response deleteEmployee(String employeeId){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .queryParam("employeeId", employeeId)
                .when()
                .delete("/Employee/DeleteEmployee")
                .andReturn();
    }
}
