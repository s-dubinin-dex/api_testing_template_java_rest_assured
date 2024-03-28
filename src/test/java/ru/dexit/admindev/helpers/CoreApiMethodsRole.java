package ru.dexit.admindev.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.dexit.admindev.TestBase;
import ru.dexit.admindev.models.Role.AddRoleRequestModel;
import ru.dexit.admindev.models.Role.UpdateRoleRequestModel;
import ru.dexit.admindev.spec.Specifications;

public class CoreApiMethodsRole extends TestBase {
    @Step("Создание роли")
    public static Response addRole(AddRoleRequestModel body){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .body(body)
                .when()
                .post("/Role/AddRole")
                .andReturn();

    }

    @Step("Изменение роли")
    public static Response updateRole(UpdateRoleRequestModel body){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .body(body)
                .when()
                .put("/Role/UpdateRole")
                .andReturn();

    }

    @Step("Удаление роли")
    public static Response deleteRole(String id){

        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .queryParam("id", id)
                .when()
                .delete("/Role/DeleteRole")
                .andReturn();
    }

    @Step("Получение списка доступных полиси")
    public static Response getPolicies(){
        return Specifications.setBasicRequestSpecification(URL_ADMIN)
                .when()
                .get("/Role/GetPolicies")
                .andReturn();
    }

}
