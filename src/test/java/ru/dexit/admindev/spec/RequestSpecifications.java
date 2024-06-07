package ru.dexit.admindev.spec;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import ru.dexit.admindev.UrlBase;
import ru.dexit.admindev.data.DataGenerator;
import ru.dexit.admindev.helpers.CustomAllureListener;

public class RequestSpecifications extends UrlBase {

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
    public static RequestSpecification basicRequestSpecificationWithEmptyToken(){

        return basicRequestSpecification()
                .contentType("application/json")
                .header("Authorization", "");

    }
    public static RequestSpecification basicRequestSpecificationWithInvalidToken(){

        return basicRequestSpecification()
                .contentType("application/json")
                .header("Authorization", DataGenerator.generateRandomBearerToken());

    }
}
