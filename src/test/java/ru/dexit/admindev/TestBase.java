package ru.dexit.admindev;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import ru.dexit.admindev.helpers.Authorization;

public class TestBase {
    protected final static String URL_ADMIN = "https://api-dev.k3s.dex-it.ru/admin";
    protected static String TOKEN;
    protected static Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        TOKEN = Authorization.getToken();
    }
}
