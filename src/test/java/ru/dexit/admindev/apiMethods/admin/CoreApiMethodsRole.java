package ru.dexit.admindev.apiMethods.admin;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.models.role.AddRoleRequestModel;
import ru.dexit.admindev.models.role.AddRoleRequestModelNotNull;
import ru.dexit.admindev.models.role.UpdateRoleRequestModel;
import ru.dexit.admindev.models.role.UpdateRoleRequestModelNotNull;
import ru.dexit.admindev.spec.SpecificationsServer;

public class CoreApiMethodsRole extends TestBase {
    // TODO: Вынести SetBaseUrl в базовый метод

    @Step("Создание роли")
    public static Response addRole(AddRoleRequestModel body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .post("/Role/AddRole")
                .andReturn();

    }

    @Step("Создание роли")
    public static Response addRole(AddRoleRequestModelNotNull body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .post("/Role/AddRole")
                .andReturn();

    }

    @Step("Изменение роли")
    public static Response updateRole(UpdateRoleRequestModel body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .put("/Role/UpdateRole")
                .andReturn();

    }

    @Step("Изменение роли")
    public static Response updateRole(UpdateRoleRequestModelNotNull body){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .body(body)
                .when()
                .put("/Role/UpdateRole")
                .andReturn();

    }

    @Step("Удаление роли")
    public static Response deleteRole(String id){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .queryParam("id", id)
                .when()
                .delete("/Role/DeleteRole")
                .andReturn();
    }

    @Step("Удаление роли")
    public static Response deleteRole(){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .when()
                .delete("/Role/DeleteRole")
                .andReturn();
    }

    @Step("Получение списка доступных полиси")
    public static Response getPolicies(){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .when()
                .get("/Role/GetPolicies")
                .andReturn();
    }

    @Step("Получение данных через протокол oData")
    public static Response getODataRole(){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .get("/odata/Role")
                .andReturn();
    }
    @Step("Получение данных через протокол oData")
    public static Response getODataRoleWithIncludeDeletedParameter(boolean includeDeleted){

        SpecificationsServer.setBaseUrl(URL_ADMIN);

        return RestAssured.given()
                .param("includeDeleted", includeDeleted)
                .get("/odata/Role")
                .andReturn();
    }
}
