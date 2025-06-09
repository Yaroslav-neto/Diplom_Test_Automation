package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

public class PayCardPage {
    protected final SelenideElement fieldNumberCard = $x("//span[contains(@class, 'input__top') and contains(text(), 'Номер карты')]/following::input[1]");
    protected final SelenideElement fieldMonth = $x("//span[contains(@class, 'input__top') and contains(text(), 'Месяц')]/following::input[1]");
    protected final SelenideElement fieldYear = $x("//span[contains(@class, 'input__top') and contains(text(), 'Год')]/following::input[1]");
    protected final SelenideElement fieldOwner = $x("//span[contains(@class, 'input__top') and contains(text(), 'Владелец')]/following::input[1]");
    protected final SelenideElement fieldCVC = $x("//span[contains(@class, 'input__top') and contains(text(), 'CVC/CVV')]/following::input[1]");
    protected final SelenideElement buttonContinue = $x("//button[.//span[text()='Продолжить']]");
    protected final SelenideElement successfullyNotification = $(".notification.notification_status_ok");
    protected final SelenideElement closeSuccessfullyNotification = $(".notification.notification_status_ok button");
    protected final SelenideElement errorNotification = $(".notification.notification_status_error");
    protected final SelenideElement closeErrorNotification = $(".notification.notification_status_error button");
    protected final SelenideElement substringMonth = $x("//span[contains(@class, 'input__sub') and contains(text(), 'Неверно указан срок действия карты')]");
    protected final SelenideElement substringMonthInvalid = $x("(//span[contains(@class, 'input__sub') and contains(text(), 'Неверный формат')])[1]");
    protected final SelenideElement substringYear = $x("//span[contains(@class, 'input__sub') and contains(text(), 'Истёк срок действия карты')]");
    protected final SelenideElement substringDateIsIncorrect = $x("//span[contains(@class, 'input__sub') and contains(text(), 'Неверно указан срок действия карты')]");
    protected final SelenideElement substringYearInvalid = $x("(//span[contains(@class, 'input__sub') and contains(text(), 'Неверный формат')])[2]");
    protected final SelenideElement substringCVC = $x("//span[contains(@class, 'input__sub') and contains(text(), 'Неверный формат')]");
    protected final SelenideElement substringCVCInvalid = $x("(//span[contains(@class, 'input__sub') and contains(text(), 'Неверный формат')])[3]");
    protected final SelenideElement substringNumber = $$(".input__sub").findBy(text("Неверный формат"));
    protected final SelenideElement substringOwner = $x("//span[contains(@class, 'input__sub') and contains(text(), 'Поле обязательно для заполнения')]");


    public void fillCardNumber(String cardNumber) {
        fieldNumberCard.setValue(cardNumber);
    }

    public void fillMonth(String month) {
        fieldMonth.setValue(month);
    }

    public void fillYear(String year) {
        fieldYear.setValue(year);
    }

    public void fillOwner(String owner) {
        fieldOwner.setValue(owner);
    }

    public void fillCVC(String cvc) {
        fieldCVC.setValue(cvc);
    }

    public void clickContinue() {
        buttonContinue.click();
    }

    public void visibleSuccessfullyMessage(String expectedText) {
        successfullyNotification.shouldHave(exactText(expectedText), Duration.ofSeconds(13)).shouldBe(visible);
    }

    public void closeSuccessfullyMessage() {
        closeSuccessfullyNotification.click();
    }

    public void notVisibleSuccessfullyMessage() {
        successfullyNotification.shouldNotBe(visible, Duration.ofSeconds(5));
    }

    public void visibleErrorMessage(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText), Duration.ofSeconds(13)).shouldBe(visible);
    }

    public void closeErrorMessage() {
        closeErrorNotification.click();
    }

    public void notVisibleErrorMessage() {
        errorNotification.shouldNotBe(visible, Duration.ofSeconds(5));
    }

    public void visibleSubstringMonth(String expectedText) {
        substringMonth.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringYear(String expectedText) {
        substringYear.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringCVC(String expectedText) {
        substringCVC.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringNumber(String expectedText) {
        substringNumber.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringOwner(String expectedText) {
        substringOwner.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringMonthInvalid(String expectedText) {
        substringMonthInvalid.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringYearInvalid(String expectedText) {
        substringYearInvalid.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringCVCInvalid(String expectedText) {
        substringCVCInvalid.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void visibleSubstringDateIsIncorrect(String expectedText) {
        substringDateIsIncorrect.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void fillCardData(DataHelper.FullCardData cardData) {
        fillCardNumber(cardData.getNumber());
        fillMonth(cardData.getMonth());
        fillYear(cardData.getYear());
        fillOwner(cardData.getOwner());
        fillCVC(cardData.getCvc());
    }

    public static class PayFromCardPage extends PayCardPage {
        private final SelenideElement headerPayCard = $x("//h3[contains(text(), 'Оплата по карте')]");

        public PayFromCardPage() {
            headerPayCard.shouldHave(text("Оплата по карте")).shouldBe(visible);
        }
    }

    public static class CreditPayCardPage extends PayCardPage {
        private final SelenideElement creditPayCardHeader = $x("//h3[contains(text(), 'Кредит по данным карты')]");

        public CreditPayCardPage() {
            creditPayCardHeader.shouldHave(text("Кредит по данным карты")).shouldBe(visible);
        }
    }
}