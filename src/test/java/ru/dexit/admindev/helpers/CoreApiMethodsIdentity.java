package ru.dexit.admindev.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.spec.RequestSpecifications;
import ru.dexit.admindev.spec.SpecificationsServer;

import static io.restassured.RestAssured.given;

public class CoreApiMethodsIdentity extends TestBase {

    @Step("Авторизация и получение токена")
    public static Response connectToken(){
        SpecificationsServer.installRequestSpecification(RequestSpecifications.basicRequestSpecification());

        SpecificationsServer.setBaseUrl(URL_IDENTITY);

        return given()
                .filter(CustomAllureListener.withCustomTemplates())
                .formParam("client_id", "admin.client")
                .formParam("client_secret", "9F45EA47-9BD6-48D8-B218-273A256DB093")
                .formParam("grant_type", "password")
                .formParam("scope", "openid profile offline_access admin-api policy")
                .formParam("username", "test@gmail.com")
                .formParam("password", "005")
                .when()
                .post("/connect/token")
                .andReturn();

    }
}
