package ru.dex_it.k3s.admin_dev.data;

import com.github.javafaker.Faker;
import ru.dex_it.k3s.admin_dev.models.Employee.AddEmployeeRequestModel;
import ru.dex_it.k3s.admin_dev.models.Employee.UpdateEmployeeRequestModel;
import ru.dex_it.k3s.admin_dev.models.Role.AddRoleRequestModel;

import javax.management.monitor.StringMonitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataGenerator {
    public static AddEmployeeRequestModel getAddEmployeeRequestModel(){
        Faker faker = new Faker();

        return AddEmployeeRequestModel.builder()
                .name(faker.name().fullName())
                .roleId("b2a142fe-9035-46a5-bd9f-06baf40be2b0")
                .email(faker.internet().emailAddress())
                .build();

    }

    public static UpdateEmployeeRequestModel getUpdateEmployeeRequestModel(String id, String roleId){

        Faker faker = new Faker();

        return UpdateEmployeeRequestModel.builder()
                .id(id)
                .name(faker.name().fullName())
                .roleId(roleId)
                .build();

    }

}
