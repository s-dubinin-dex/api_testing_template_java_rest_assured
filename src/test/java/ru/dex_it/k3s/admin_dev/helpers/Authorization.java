package ru.dex_it.k3s.admin_dev.helpers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class Authorization {


    public static String getToken(){

        RestAssured.baseURI = "https://api-dev.k3s.dex-it.ru/identity";

        Response response = given()
                .formParam("client_id", "admin.client")
                .formParam("client_secret", "9F45EA47-9BD6-48D8-B218-273A256DB093")
                .formParam("grant_type", "password")
                .formParam("scope", "openid profile offline_access admin-api policy")
                .formParam("username", "test@gmail.com")
                .formParam("password", "005")
                .when()
                .post("/connect/token")
                .andReturn();

        return response.jsonPath().get("access_token");
    }

}
