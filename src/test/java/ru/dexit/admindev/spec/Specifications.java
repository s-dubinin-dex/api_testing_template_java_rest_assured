package ru.dexit.admindev.spec;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.helpers.CustomAllureListener;

import static io.restassured.RestAssured.given;

public class Specifications extends TestBase {
    public static RequestSpecification setBasicRequestSpecification(String url){

        RestAssured.baseURI = url;

        return given()
                .filter(CustomAllureListener.withCustomTemplates())
                .header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type", "application/json")
//                .log().all()

                ;

    }
}
