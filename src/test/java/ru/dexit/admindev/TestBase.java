package ru.dexit.admindev;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.dexit.admindev.helpers.Authorization;

public class TestBase {
    protected final static String BASE_URL = "https://api-dev.k3s.dex-it.ru";
    protected final static String URL_IDENTITY = BASE_URL + "/identity";
    protected final static String URL_ADMIN = BASE_URL + "/admin";
    protected static String TOKEN;
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
