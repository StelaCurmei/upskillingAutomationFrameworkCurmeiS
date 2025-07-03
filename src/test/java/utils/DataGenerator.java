package utils;

import net.datafaker.Faker;

import java.util.regex.Pattern;

public class DataGenerator {
    private static final Faker faker = new Faker();

    public static String getRandomEmail() {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String email;
        do {
            email = faker.internet().emailAddress();
        } while (!Pattern.matches(emailRegex, email) || email.length() > 20);
        return email;
    }

    public static String getRandomFirstName() {
        String nameRegex = "^[A-Za-z]+$";
        String firstName;
        do {
            firstName = faker.name().firstName();
        } while (!Pattern.matches(nameRegex, firstName) || firstName.length() > 20);
        return firstName;
    }

    public static String getRandomLastName() {
        String nameRegex = "^[A-Za-z]+$";
        String lastName;
        do {
            lastName = faker.name().lastName();
        } while (!Pattern.matches(nameRegex, lastName) || lastName.length() > 20);
        return lastName;
    }

    public static String getRandomPassword() {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        String password;
        do {
            // minLength, maxLength, includeUpper, includeDigits, includeSpecial
            password = faker.internet().password(8, 16, true, true, true);
        } while (!Pattern.matches(passwordRegex, password) || password.length() > 20);
        return password;
    }
}
