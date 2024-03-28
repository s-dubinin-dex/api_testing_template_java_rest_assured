package ru.dex_it.k3s.admin_dev.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.dex_it.k3s.admin_dev.TestBase;
import ru.dex_it.k3s.admin_dev.models.Role.AddRoleRequestModel;
import ru.dex_it.k3s.admin_dev.spec.Specifications;

public class CoreApiMethodsRole extends TestBase {
    @Step("Создание роли")
    public static Response addRole(AddRoleRequestModel body){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .body(body)
                .when()
                .post("/Role/AddRole")
                .andReturn();

    }
}
