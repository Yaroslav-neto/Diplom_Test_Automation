package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.PayCardPage;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;

public class PositivePayCardTest {
    private DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        SQLHelper.cleanDatabase();
        Configuration.holdBrowserOpen = false;
        open("http://localhost:8080");
        dashboardPage = new DashboardPage();
    }

    @AfterEach
    void verifyDataInsertedAndClean() {
        boolean recordInserted = SQLHelper.isRecordInsertedInLastHalfMinute();
        assertTrue(recordInserted, "Записи не были добавлены в базу данных за последние 30 сек.");
        SQLHelper.cleanDatabase();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());

    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        SQLHelper.cleanDatabase();
    }

    @Test
    public void shouldSuccessfullyPayApprovedCard() {
        try {
            dashboardPage.clickPayCardButton();
            PayCardPage.PayFromCardPage payFromCardPage = new PayCardPage.PayFromCardPage();

            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);
            payFromCardPage.fillCardData(cardData);
            payFromCardPage.clickContinue();

            payFromCardPage.visibleSuccessfullyMessage("Успешно \nОперация одобрена Банком.");
            payFromCardPage.closeSuccessfullyMessage();
            payFromCardPage.notVisibleSuccessfullyMessage();
            payFromCardPage.notVisibleErrorMessage();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест не прошел из-за исключения: " + e.getMessage());
        }
    }

    @Test
    public void shouldSuccessfullyCreditPayApprovedCard() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();

            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);
            creditPayCardPage.fillCardData(cardData);
            creditPayCardPage.clickContinue();

            creditPayCardPage.visibleSuccessfullyMessage("Успешно \nОперация одобрена Банком.");
            creditPayCardPage.closeSuccessfullyMessage();
            creditPayCardPage.notVisibleSuccessfullyMessage();
            creditPayCardPage.notVisibleErrorMessage();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест не прошел из-за исключения: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("БАГ отклоненная покупка разрешенной картой валидный период")
    public void shouldPayApprovedCardValidYearPlus10() {
        try {
            dashboardPage.clickPayCardButton();
            PayCardPage.PayFromCardPage payFromCardPage = new PayCardPage.PayFromCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);

            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue() - 1;
            int currentYear = currentDate.getYear() % 100;
            String formattedMonth = String.format("%02d", currentMonth);

            DataHelper.FullCardData validYearCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    formattedMonth,
                    String.format("%02d", currentYear + 10),
                    cardData.getOwner(),
                    cardData.getCvc()
            );

            payFromCardPage.fillCardData(validYearCardData);
            payFromCardPage.clickContinue();

            payFromCardPage.visibleSuccessfullyMessage("Успешно \nОперация одобрена Банком.");
            payFromCardPage.closeSuccessfullyMessage();
            payFromCardPage.notVisibleSuccessfullyMessage();
            payFromCardPage.notVisibleErrorMessage();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест не прошел из-за исключения: " + e.getMessage());
        }
    }
}
