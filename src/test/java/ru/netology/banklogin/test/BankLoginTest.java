package ru.netology.banklogin.test;

import org.junit.jupiter.api.*;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage; 

    @AfterEach
    void tearDown() { 
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() { 
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Должен успешно войти в панель мониторинга с существующими логином и паролем из текстовых данных sut")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData(); 
        var verificationPage = loginPage.validLogin(authInfo); 
        verificationPage.verifyVerificationPageVisibility();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Должно появиться уведомление об ошибке, если пользователь не существует в базе")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Должно появиться уведомление об ошибке, если  с существующим логин в базе и активным пользователем и случайным проверочным кодом")
    void shouldNotAuthWithInValidCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode(); 
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
