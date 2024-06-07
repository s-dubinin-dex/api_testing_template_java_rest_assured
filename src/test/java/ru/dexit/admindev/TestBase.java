package ru.dexit.admindev;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.dexit.admindev.helpers.Authorization;

public class TestBase extends UrlBase {

    protected static final Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        TOKEN = Authorization.getToken();
    }

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = null;
    }
}
