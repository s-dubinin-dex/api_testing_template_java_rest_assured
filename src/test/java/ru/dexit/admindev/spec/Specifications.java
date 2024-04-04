package ru.dexit.admindev.spec;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.helpers.CustomAllureListener;

import static io.restassured.http.ContentType.*;
import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class Specifications extends TestBase {

    /*/////////////////////////////////
    ////// Request specification //////
    *//////////////////////////////////

    public static RequestSpecification setBasicRequestSpecification(String url) {

        RestAssured.baseURI = url;

        return given()
                .filter(CustomAllureListener.withCustomTemplates())
//                .log().all()
                .header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type", "application/json")
                ;
    }

    public static RequestSpecification basicRequestSpecificationWithoutAuthorization(String url){

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .addFilter(CustomAllureListener.withCustomTemplates())
                .setContentType(JSON)
                .build();

    }

    public static RequestSpecification basicRequestSpecificationWithAuthorization(String url){

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .addFilter(CustomAllureListener.withCustomTemplates())
                .setContentType(JSON)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .build();

    }

    /*//////////////////////////////////
    ////// Response specification //////
    *///////////////////////////////////

    public static ResponseSpecification responseSpecOK200JSONBody(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(SC_OK)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }

    public static ResponseSpecification responseSpecOK200EmptyStringBody(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectBody(equalTo(""))
                .expectStatusCode(SC_OK)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }

    public static ResponseSpecification responseSpec400(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(SC_BAD_REQUEST)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }
    public static ResponseSpecification responseSpec404(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(SC_NOT_FOUND)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }
    public static ResponseSpecification responseSpec409(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(SC_CONFLICT)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }
    public static ResponseSpecification responseSpec412() {
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectContentType(JSON)
                .expectStatusCode(SC_PRECONDITION_FAILED)
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
