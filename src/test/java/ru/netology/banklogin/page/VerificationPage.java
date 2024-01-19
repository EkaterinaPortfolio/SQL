package ru.netology.banklogin.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public void verifyVerificationPageVisibility() {  //проверяет видимость страницы
        codeField.shouldBe(visible);
    }

    public void verifyErrorNotification(String expectedText) {   //проверяет  появление ошибки
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {  //валидная верификация и возвращает страницу  DashboardPage
        verify(verificationCode);
        return new DashboardPage();
    }

    // методы разделены, т.е. проверка видимости DashboardPage вынесена в хидор
    public void verify(String verificationCode) {   //проверяет верификацию, не возвращая таблицу дашборда
        codeField.setValue(verificationCode);
        verifyButton.click();
    }
}
