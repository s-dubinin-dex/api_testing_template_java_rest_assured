package ru.dexit.admindev.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.http.ContentType.JSON;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class ResponseSpecifications {

    // TODO: Подумать насчёт выноса повторяющихся методов в общий метод
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
    public static ResponseSpecification responseSpec401(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectBody(equalTo(""))
                .expectStatusCode(SC_UNAUTHORIZED)
                .expectResponseTime(lessThanOrEqualTo(1L), SECONDS)
                .build();
    }
    public static ResponseSpecification responseSpec403(){
        return new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectBody(equalTo(""))
                .expectStatusCode(SC_FORBIDDEN)
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
}
