package ru.dexit.admindev.data;

import com.github.javafaker.Faker;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;

import ru.dexit.admindev.models.Role.AddRoleRequestModel;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    static Faker faker = new Faker();

    public static AddEmployeeRequestModel getRandomAddEmployeeRequestModel(){

        return AddEmployeeRequestModel.builder()
                .name(faker.name().fullName())
                .roleId("b2a142fe-9035-46a5-bd9f-06baf40be2b0")
                .email(faker.internet().emailAddress())
                .build();

    }

    public static AddRoleRequestModel getRandomAddRoleRequestModel(){

        List<String> policies = new ArrayList<>();
        policies.add("notification.read");
        policies.add("employee.read");
        policies.add("role.read");
        policies.add("reminder.read");
        policies.add("log.read");
        policies.add("user.read");

        return AddRoleRequestModel.builder()
                .name(faker.company().profession())
                .policies(policies)
                .build();
    }
}
