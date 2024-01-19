package ru.netology.banklogin.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static AuthInfo getAuthInfoWithTestData() {     //метод возвращает данные с аутентификации
        return new AuthInfo("vasya", "qwerty123");
    }

    private static String generateRandomLogin() {   // метод генерирует рандомный логин
        return faker.name().username();
    }

    private static String generateRandomPassword() {   // метод генерирует рандомный пароль
        return faker.internet().password();
    }

    public static AuthInfo generateRandomUser() {     // метод генерирует рандомного пользователя
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }

    public static VerificationCode generateRandomVerificationCode() {    /// метод генерирует рандомный код верификации
        return new VerificationCode(faker.numerify("#####"));
    }

    @Value
    public static class AuthInfo {  // информация о аутентификации (дата класс)
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {  //код верификации (дата класс)
        String code;
    }
}
