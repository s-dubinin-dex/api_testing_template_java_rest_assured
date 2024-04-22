package ru.dexit.admindev.apiMethods.identity;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.dexit.admindev.UrlBase;
import ru.dexit.admindev.helpers.CustomAllureListener;
import ru.dexit.admindev.spec.RequestSpecifications;
import ru.dexit.admindev.spec.SpecificationsServer;

import static io.restassured.RestAssured.given;

public class CoreApiMethodsIdentity extends UrlBase {

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
