package ru.dexit.admindev.data;

import com.github.javafaker.Faker;
import org.junit.jupiter.params.provider.Arguments;
import ru.dexit.admindev.models.Employee.AddEmployeeRequestModel;

import ru.dexit.admindev.models.Role.AddRoleRequestModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DataGenerator {
    static Faker faker = new Faker();
    public static final String engLettersLowerCase = "abcdefghijklmnopqrstuvwxyz";
    public static final String engLettersUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String engLetters = engLettersLowerCase + engLettersUpperCase;
    public static final String rusLettersLowerCase = "абвгдежзийклмнопрстуфхцчшщыьэюя";
    public static final String rusLettersUpperCase = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЫЬЭЮЯ";
    public static final String rusLetters = rusLettersLowerCase + rusLettersUpperCase;
    public static final String digits = "0123456789";

    public static AddEmployeeRequestModel getRandomAddEmployeeRequestModel(){

        return AddEmployeeRequestModel.builder()
                .name(faker.name().fullName())
                .roleId(Role.FULL_WRITE.roleUUID)
                .email(faker.internet().emailAddress())
                .build();

    }

    public static AddRoleRequestModel getRandomAddRoleRequestModel(){

        return AddRoleRequestModel.builder()
                .name(faker.company().profession() + "_" + getSalt())
                .policies(getDefaultPolicies())
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
                arguments(Role.FULL_WRITE.roleUUID),
                arguments(Role.FULL_READ.roleUUID),
                arguments(Role.NO_RIGHTS.roleUUID)
        );
    }

    public static Stream<Arguments> getValidRoleNames(){
        return getValidEmployeeNames();
    }

    public static List<String> getDefaultPolicies(){
        return Arrays.asList(
                "notification.write",
                "employee.write",
                "role.write",
                "reminder.write",
                "log.read",
                "user.read"
        );
    }

    public static List<String> getReadPolicies() {
        return Arrays.asList(
                "user.read",
                "notification.read",
                "employee.read",
                "role.read",
                "reminder.read",
                "log.read",
                "marketing.read");
    }

    public static List<String> getWritePolicies() {
        return Arrays.asList(
                "notification.write",
                "employee.write",
                "role.write",
                "reminder.write",
                "marketing.write");
    }

    public static List<String> getAllPolicies(){
        List<String> result = new ArrayList<>();

        result.addAll(getReadPolicies());
        result.addAll(getWritePolicies());

        return result;
    }

    public static Stream<Arguments> getReadPoliciesStream(){
        return getReadPolicies().stream().map(Arguments::of);
    }

    public static Stream<Arguments> getWritePoliciesStream(){
        return getWritePolicies().stream().map(Arguments::of);
    }

    public static Stream<Arguments> getAllPoliciesStream(){
        return getAllPolicies().stream().map(Arguments::of);
    }

    public static Stream<Arguments> getValidEmails(){
        String validSpecialCharactersForEmail = "!#$%&'()*+,-./:;<>?[]^_`{|}~ " + "1";

        List<String> result = new ArrayList<>();

        result.add(generateRandomString(rusLetters, 1) + "@" + generateRandomString(rusLetters, 1) + "." + generateRandomString(rusLetters, 1));
        result.add(generateRandomString(engLetters, 1) + "@" + generateRandomString(engLetters, 1) + "." + generateRandomString(engLetters, 1));
        result.add(generateRandomString(rusLetters, 25) + "@" + generateRandomString(rusLetters, 25) + "." + generateRandomString(rusLetters, 25));
        result.add(generateRandomString(engLetters, 25) + "@" + generateRandomString(engLetters, 25) + "." + generateRandomString(engLetters, 25));
        result.add(generateRandomString(rusLetters, 250) + "@" + generateRandomString(rusLetters, 1) + "." + generateRandomString(rusLetters, 1));
        result.add(generateRandomString(rusLetters, 1) + "@" + generateRandomString(rusLetters, 250) + "." + generateRandomString(rusLetters, 1));
        result.add(generateRandomString(rusLetters, 1) + "@" + generateRandomString(rusLetters, 1) + "." + generateRandomString(rusLetters, 250));
        result.add(generateRandomString(engLetters, 250) + "@" + generateRandomString(engLetters, 1) + "." + generateRandomString(engLetters, 1));
        result.add(generateRandomString(engLetters, 1) + "@" + generateRandomString(engLetters, 250) + "." + generateRandomString(engLetters, 1));
        result.add(generateRandomString(engLetters, 1) + "@" + generateRandomString(engLetters, 1) + "." + generateRandomString(engLetters, 250));
        result.add(generateRandomString(rusLetters, 1) + "@" + generateRandomString(engLetters, 1) + "." + generateRandomString(engLetters, 1));
        result.add(generateRandomString(engLetters, 1) + "@" + generateRandomString(rusLetters, 1) + "." + generateRandomString(rusLetters, 1));
        result.add(generateRandomString(digits, 1) + "@" + generateRandomString(engLetters, 1) + "." + generateRandomString(engLetters, 1));

        for (Character specialCharacter: validSpecialCharactersForEmail.toCharArray()){
            result.add(generateEmailWithCharacterInUsername(specialCharacter));
            result.add(generateEmailWithCharacterInServerName(specialCharacter));
            result.add(generateEmailWithCharacterInDomain(specialCharacter));
        }

        return result.stream().map(Arguments::of);

    }

    public static String generateEmailWithCharacterInUsername(Character character){
        return character + "@" + generateRandomString(engLetters, 1) + "." + generateRandomString(engLetters, 1);
    }
    public static String generateEmailWithCharacterInServerName(Character character){
        return generateRandomString(engLetters, 1) + "@" + character + "." + generateRandomString(engLetters, 1);
    }
    public static String generateEmailWithCharacterInDomain(Character character){
        return generateRandomString(engLetters, 1) + "@" + generateRandomString(engLetters, 1) + "." + character;
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

    public static String getSalt(){
        return generateRandomString(engLetters, 6);
    }
}
