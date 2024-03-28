package ru.dex_it.k3s.admin_dev.spec;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import ru.dex_it.k3s.admin_dev.TestBase;
import ru.dex_it.k3s.admin_dev.helpers.CustomAllureListener;

import static io.restassured.RestAssured.given;

public class Specifications extends TestBase {
    public static RequestSpecification setBasicRequestSpecification(String url){

        RestAssured.baseURI = url;

        return given()
                .filter(CustomAllureListener.withCustomTemplates())
                .header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type", "application/json")
                .log().all();

    }
}
