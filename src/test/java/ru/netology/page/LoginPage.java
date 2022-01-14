package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo getInvalidAuthInfo) {
        loginField.setValue(getInvalidAuthInfo.getLogin());
        passwordField.setValue(getInvalidAuthInfo.getPassword());
        loginButton.click();
    }

    public void warningMessageLogin() { // comment: доработать локаторы
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    public void warningMessageLoginExceedingLimit() {
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible);
        $(withText("Превышено количество попыток ввода пароля!")).shouldBe(visible);
    }

    // очистка полей ввода логина и пароля / comment: Это метод page объекта, размещайте его в классе page объекта
    public static void clearLoginFields() {
        $("[data-test-id='login'] input").click();
        $("[data-test-id='login'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='login'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='password'] input").click();
        $("[data-test-id='password'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='password'] input").sendKeys(Keys.BACK_SPACE);
    }
}