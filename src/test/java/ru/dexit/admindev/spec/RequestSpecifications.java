package ru.dexit.admindev.spec;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.helpers.CustomAllureListener;

public class RequestSpecifications extends TestBase {

    public static RequestSpecification basicRequestSpecification(){

        return RestAssured.given()
                .filter(CustomAllureListener.withCustomTemplates());

    }

    public static RequestSpecification basicRequestSpecificationWithAuthorization(){

       return basicRequestSpecification()
               .contentType("application/json")
               .header("Authorization", "Bearer " + TOKEN);
    }

    public static RequestSpecification basicRequestSpecificationWithoutAuthorization(){

        return basicRequestSpecification()
                .contentType("application/json");

    }
}
