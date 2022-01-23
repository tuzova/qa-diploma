package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class PayPage {

    private SelenideElement cardField = $(withText("Номер карты")).parent().$("input");
    private SelenideElement monthField = $(withText("Месяц")).parent().$("input");
    private SelenideElement yearField = $(withText("Год")).parent().$("input");
    private SelenideElement cardOwnerField = $(withText("Владелец")).parent().$("input");
    private SelenideElement сvcField = $(withText("CVC")).parent().$("input");
    private SelenideElement continueButton = $(withText("Продолжить")).parent().$("span");

    public void payment(String cardNumber, String month, String year, String cardOwner, String cvc) { // шаблон заполнения формы платежа
        cardField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        cardOwnerField.setValue(cardOwner);
        сvcField.setValue(cvc);
        continueButton.click();
    }

    public void messageApprovedPay() { // сообщение об успешной оплате
        $(withText("Успешно")).parent().$("div").shouldBe(visible, Duration.ofSeconds(20));
        $(".notification").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("Операция одобрена банком"));
    }

    public void messageDeniedPay() { // сообщение об отклонении платежа
        $(withText("Ошибка")).parent().$("div").shouldBe(visible, Duration.ofSeconds(20));
        $(".notification").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Банк отказал в проведении операции"));
    }

    public void messageWrongDataCardNumber() {
        $(withText("Неверный формат")).parent().$(withText("Номер карты")).shouldBe(visible);
    }

    public void messageWrongDataMonth() {
        $(withText("Неверный формат")).parent().$(withText("Месяц")).shouldBe(visible);
    }

    public void messageWrongDataMonth13() {
        $(withText("Неверно указан срок действия карты")).parent().$(withText("Месяц")).shouldBe(visible);
    }

    public void messageWrongDataYear() {
        $(withText("Неверный формат")).parent().$(withText("Год")).shouldBe(visible);
    }

    public void messageWrongOverYear() {
        $(withText("Неверно указан срок действия карты")).parent().$(withText("Год")).shouldBe(visible);
    }

    public void messageWrongLastYear() {
        $(withText("Истёк срок действия карты")).parent().$(withText("Год")).shouldBe(visible);
    }

    public void messageWrongDataCardOwner() {
        $(withText("Поле обязательно для заполнения")).parent().$(withText("Владелец")).shouldBe(visible);
    }

    public void messageWrongDataCvc() {
        $(withText("Неверный формат")).parent().$(withText("CVC")).shouldBe(visible);
    }
}