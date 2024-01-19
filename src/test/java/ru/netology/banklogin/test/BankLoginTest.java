package ru.netology.banklogin.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage; //объявляем, как переменную экземпляра класса

    @AfterEach
    void tearDown() {  //чистит коды верификации
        cleanAuthCodes();
    }

    @AfterEach
    static void tearDownAll() { // после всех тестов чистится база данных, после каждого теста нужно перезапускать джарник иначе не будет валидного логина не получится
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class); //перед каждым тестом открывается страница и ей присваивается значение loginPage
    }

    @Test
    @DisplayName("Должен успешно войти в панель мониторинга с существующими логином и паролем из текстовых данных sut")
    void shouldSuccessfulLogin(){
        var authInfo = DataHelper.getAuthInfoWithTestData(); //получили валидные данные для аутентификации
        var verificationPage = loginPage.validLogin(authInfo); //на странице loginPage ввели валидные данные, нажали кнопку
        verificationPage.verifyVerificationPageVisibility(); //возвратили новую страницу верификации и положили ее в переменную, на странице верификации проверили что видин элемент поле ввода кода, что принимаем как доказ. перехода на страницу верификации
        var verificationCode = SQLHelper.getVerificationCode();  //получили код верификации
        verificationPage.validVerify(verificationCode.getCode()); //ввел код, и верную новый экхемпляр DashboardPage
    }

    @Test
    @DisplayName("Должно появиться уведомление об ошибке, если пользователь не существует в базе")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();  //вводим данные случайного пользователя
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Должно появиться уведомление об ошибке, если  с существующим логин в базе и активным пользователем и случайным проверочным кодом")
    void shouldNotAuthWithInValidCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();  //вводим случайный код верификации
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("Cледует заблокировать пользователя, если он трижды ввел Неверный код")
    void shouldBlockUserIfInputThreeTimesInvalidCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Неверно указан код! Попробуйте ещё раз.");
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Неверно указан код! Попробуйте ещё раз.");
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Система заблокирована!");
    }

}
