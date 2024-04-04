package ru.dexit.admindev.spec;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.dexit.admindev.TestBase;

public class SpecificationsServer extends TestBase {

    public static void installSpecification(RequestSpecification requestSpecification, ResponseSpecification responseSpecification){
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

    public static void installRequestSpecification(RequestSpecification requestSpecification){
        RestAssured.requestSpecification = requestSpecification;
    }

    public static void installResponseSpecification(ResponseSpecification responseSpecification){
        RestAssured.responseSpecification = responseSpecification;
    }

    public static void setBaseUrl(String url) {
        RestAssured.baseURI = url;
        RestAssured.requestSpecification.baseUri(url);
    }
}
