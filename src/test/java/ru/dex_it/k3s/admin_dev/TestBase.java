package ru.dex_it.k3s.admin_dev;

import org.junit.jupiter.api.BeforeAll;
import ru.dex_it.k3s.admin_dev.helpers.Authorization;

public class TestBase {
    protected final static String URL_ADMIN = "https://api-dev.k3s.dex-it.ru/admin";
    protected static String TOKEN;

    @BeforeAll
    static void beforeAll() {
        TOKEN = Authorization.getToken();
    }
}
