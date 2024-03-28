package ru.dexit.admindev.data;

import com.github.javafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;

import ru.dexit.admindev.models.Role.AddRoleRequestModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DataGenerator {
    static Faker faker = new Faker();
    static final String engLettersLowerCase = "abcdefghijklmnopqrstuvwxyz";
    static final String engLettersUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String engLetters = engLettersLowerCase + engLettersUpperCase;
    static final String rusLettersLowerCase = "абвгдежзийклмнопрстуфхцчшщыьэюя";
    static final String rusLettersUpperCase = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЫЬЭЮЯ";
    static final String rusLetters = rusLettersLowerCase + rusLettersUpperCase;

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
                .name(faker.company().profession() + "_" + generateRandomString(engLetters, 6))
                .policies(policies)
                .build();
    }

    public static Stream<Arguments> getValidEmployeeNames(){

        return Stream.of(
                arguments(generateRandomString(rusLettersLowerCase, 3)),
                arguments(generateRandomString(rusLettersLowerCase, 25)),
                arguments(generateRandomString(rusLettersLowerCase, 50)),
                arguments(generateRandomString(rusLettersUpperCase, 3)),
                arguments(generateRandomString(rusLettersUpperCase, 3) + "."),
                arguments(generateRandomString(rusLettersUpperCase, 25)),
                arguments(generateRandomString(rusLettersUpperCase, 50)),
                arguments(generateRandomString(rusLetters, 24) + "." + generateRandomString(rusLetters, 24)),
                arguments(generateRandomString(engLettersLowerCase, 3)),
                arguments(generateRandomString(engLettersLowerCase, 25)),
                arguments(generateRandomString(engLettersLowerCase, 50)),
                arguments(generateRandomString(engLettersUpperCase, 3)),
                arguments(generateRandomString(engLettersUpperCase, 3) + "."),
                arguments(generateRandomString(engLettersUpperCase, 25)),
                arguments(generateRandomString(rusLetters, 12) + generateRandomString(engLetters, 12)),
                arguments(generateRandomString(rusLetters, 12) + " " + generateRandomString(engLetters, 12)),
                arguments(generateRandomString(rusLetters, 5) + " " + generateRandomString(rusLetters, 12) + " " + generateRandomString(rusLetters, 12)),
                arguments(generateRandomString(rusLetters, 5) + "'" + " " + generateRandomString(rusLetters, 5)),
                arguments(generateRandomString(rusLetters, 5) + "[" + " " + generateRandomString(rusLetters, 5)),
                arguments(generateRandomString(rusLetters, 5) + "]" + " " + generateRandomString(rusLetters, 5)),
                arguments(generateRandomString(rusLetters, 5) + "^" + " " + generateRandomString(rusLetters, 5)),
                arguments(generateRandomString(rusLetters, 5) + "_" + " " + generateRandomString(rusLetters, 5)),
                arguments(generateRandomString(rusLetters, 5) + "`" + " " + generateRandomString(rusLetters, 5)),
                arguments(generateRandomString(rusLetters, 5) + " " + " " + generateRandomString(rusLetters, 5))
        );
    }

    public static Stream<Arguments> getValidRoles(){
        return Stream.of(
                arguments(UserRole.FULL_WRITE.role.uid),
                arguments(UserRole.FULL_READ.role.uid),
                arguments(UserRole.NO_RIGHTS.role.uid)
        );
    }


    public static Integer generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000);
    }

    public static String generateRandomString(String characters, int length) {
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
}
