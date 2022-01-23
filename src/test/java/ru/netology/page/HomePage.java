package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {
    private SelenideElement buyButton = $(withText("Купить")).parent().$("span");
    private SelenideElement buyCreditButton = $(withText("Купить в кредит")).parent().$("span");

    public PayPage buyButton() {
        buyButton.click();
        return new PayPage();
    }

    public PayPage buyCreditButton() {
        buyCreditButton.click();
        return new PayPage();
    }
}