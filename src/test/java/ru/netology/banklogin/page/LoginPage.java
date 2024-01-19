package ru.netology.banklogin.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.banklogin.data.DataHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input"); //описание элементов
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login] input");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public void verifyErrorNotification(String expectedText) { //проверка появления сообщения об ошибке, принимает ожидаемый текст
        errorNotification.shouldHave(exactText(expectedText)).shouldHave(visible); //проверка что у элемента есть этот текст и он виден
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) { //метод выполняет валидный логин, принимает данные аутентификации
        loginField.setValue(info.getLogin()); // заполнение полей
        passwordField.setValue(info.getPassword());
        loginButton.click();  //нажал
        return new VerificationPage();  //вернул новую страницу VerificationPage
    }
}
