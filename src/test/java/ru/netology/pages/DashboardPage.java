package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;


public class DashboardPage {
    private final SelenideElement heading = $x("//h2[contains(text(), 'Путешествие дня')]");
    private final SelenideElement payCardButton = $x("//button[.//span[text()='Купить']]");
    private final SelenideElement payCreditCardButton = $x("//button[.//span[text()='Купить в кредит']]");

    public DashboardPage() {
        heading.shouldHave(text("Путешествие дня")).shouldBe(visible);
        payCardButton.shouldHave(text("Купить")).shouldBe(visible);
        payCreditCardButton.shouldHave(text("Купить в кредит")).shouldBe(visible);
    }

    public void clickPayCardButton() {
        payCardButton.click();
    }

    public void clickCreditPayCardButton() {
        payCreditCardButton.click();
    }
}