package ru.dexit.admindev.spec;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.helpers.CustomAllureListener;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static io.restassured.RestAssured.responseSpecification;

public class Specifications extends TestBase {

    /*/////////////////////////////////
    ////// Request specification //////
    *//////////////////////////////////

    public static RequestSpecification setBasicRequestSpecification(String url) {

        RestAssured.baseURI = url;

        return given()
                .filter(CustomAllureListener.withCustomTemplates())
                .header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type", "application/json")
//                .log().all()
                ;
    }

    public static RequestSpecification basicRequestSpecificationWithoutAuthorization(String url){

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .addFilter(CustomAllureListener.withCustomTemplates())
                .setContentType(ContentType.JSON)
                .build();

    }

    public static RequestSpecification basicRequestSpecificationWithAuthorization(String url){

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .addFilter(CustomAllureListener.withCustomTemplates())
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .build();

    }

    /*//////////////////////////////////
    ////// Response specification //////
    *///////////////////////////////////

    public static ResponseSpecification responseSpecOK200JSONBody(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }

    public static ResponseSpecification responseSpecOK200EmptyBody(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectStatusCode(200)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }

    /*/////////////////////////////////////
    ////// RestAssured Specification //////
    *//////////////////////////////////////

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
}
