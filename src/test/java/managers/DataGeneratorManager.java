package managers;

import com.github.javafaker.Faker;

import java.util.Random;
import java.util.regex.Pattern;

public class DataGeneratorManager {
    private static final Faker faker = new Faker();
    public static String getRandomEmail() {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String email;
        do {
            email = faker.internet().emailAddress();
        } while (!Pattern.matches(emailRegex, email) || email.length() > 20);
        return email;
    }
    public static String getRandomFirstName(){
        String firstName;
        String nameRegex = "^[A-Za-z]+$";
        do {
            firstName = faker.name().firstName();
        } while (!Pattern.matches(nameRegex, firstName) || firstName.length() > 20);
        return firstName;
    }
    public static String getRandomLastName(){
        String lastName;
        String nameRegex = "^[A-Za-z]+$";
        do {
            lastName = faker.name().lastName();
        } while (!Pattern.matches(nameRegex, lastName) || lastName.length() > 20);
        return lastName;
    }
    public static String getRandomPassword(){
        String password;
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        do {
            password = faker.internet().password(8, 16, true, true, true);
        } while (!Pattern.matches(passwordRegex, password) || password.length() > 20);
        return password;
    }
}