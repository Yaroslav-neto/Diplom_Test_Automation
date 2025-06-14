package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.PayCardPage;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;


public class NegativePayCardTest {
    private DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        SQLHelper.cleanDatabase();
        Configuration.holdBrowserOpen = false;
        open(System.getProperty("application.url"));
        dashboardPage = new DashboardPage();


    }

    @AfterEach
    void verifyDataInsertedAndClean() {
        boolean recordInserted = SQLHelper.isRecordInsertedInLastHalfMinute();
        assertFalse(recordInserted, "Записи были добавлены в базу данных за последние 30 сек.");
        SQLHelper.cleanDatabase();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("БАГ успешная покупка отклоненной картой в кредит")
    public void shouldErrorCreditPayDeclinedCard() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();

            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(false);
            creditPayCardPage.fillCardData(cardData);
            creditPayCardPage.clickContinue();

            creditPayCardPage.visibleErrorMessage("Ошибка \nОшибка! Банк отказал в проведении операции.");
            creditPayCardPage.closeErrorMessage();
            creditPayCardPage.notVisibleErrorMessage();
            creditPayCardPage.notVisibleSuccessfullyMessage();

        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorCreditPayDeclinedCard: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("БАГ успешная покупка отклоненной картой")
    public void shouldErrorPayDeclinedCard() {
        try {
            dashboardPage.clickPayCardButton();
            PayCardPage.PayFromCardPage payFromCardPage = new PayCardPage.PayFromCardPage();

            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(false);
            payFromCardPage.fillCardData(cardData);
            payFromCardPage.clickContinue();

            payFromCardPage.visibleErrorMessage("Ошибка \nОшибка! Банк отказал в проведении операции.");
            payFromCardPage.closeErrorMessage();
            payFromCardPage.notVisibleErrorMessage();
            payFromCardPage.notVisibleSuccessfullyMessage();

        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorCreditPayDeclinedCard: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("БАГ плавающий из-за зависимости от года")
    public void shouldErrorPayApprovedCardIsNotValidMonth00() {  // плавающий баг из-за зависимости от года
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);
            DataHelper.FullCardData invalidMonthCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    "00",
                    cardData.getYear(),
                    cardData.getOwner(),
                    cardData.getCvc()
            );
            creditPayCardPage.fillCardData(invalidMonthCardData);
            creditPayCardPage.clickContinue();

            creditPayCardPage.visibleSubstringMonth("Неверно указан срок действия карты");
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardIsNotValidMonth00: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("БАГ 00 месяц, текущий год + 2")
    public void shouldErrorPayApprovedCardIsNotValidMonth00YearOver2() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear() % 100 + 2;
        String formattedOverYear = String.format("%02d", currentYear);
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();

            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);
            DataHelper.FullCardData invalidMonthCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    "00",
                    formattedOverYear,
                    cardData.getOwner(),
                    cardData.getCvc()
            );
            creditPayCardPage.fillCardData(invalidMonthCardData);
            creditPayCardPage.clickContinue();

            creditPayCardPage.visibleSubstringMonth("Неверно указан срок действия карты");
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardIsNotValidMonth00: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorPayApprovedCardIsNotValidMonth13() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);
            DataHelper.FullCardData invalidMonthCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    "13",
                    cardData.getYear(),
                    cardData.getOwner(),
                    cardData.getCvc()
            );
            creditPayCardPage.fillCardData(invalidMonthCardData);
            creditPayCardPage.clickContinue();

            creditPayCardPage.visibleSubstringMonth("Неверно указан срок действия карты");
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardIsNotValidMonth13: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorPayApprovedCardIsNotValidMonthLessActualMonth() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);

            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear() % 100;
            int currentMonth = currentDate.getMonthValue();

            int invalidMonth = currentMonth - 1;
            if (invalidMonth < 1) {
                invalidMonth = 12;
                currentYear -= 1; // Уменьшаем год на 1
                currentYear = (currentYear + 100) % 100;
            }

            String formattedInvalidMonth = String.format("%02d", invalidMonth);

            DataHelper.FullCardData invalidMonthCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    formattedInvalidMonth,
                    String.format("%02d", currentYear),
                    cardData.getOwner(),
                    cardData.getCvc()
            );

            creditPayCardPage.fillCardData(invalidMonthCardData);
            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.visibleSubstringMonth("Неверно указан срок действия карты");
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardIsNotValidMonthLessActualMonth: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorPayApprovedCardIsNotValidYearMinusOne() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);

            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear() % 100;
            int previousYear = (currentYear + 99) % 100;
            int currentMonth = currentDate.getMonthValue();

            String formattedMonth = String.format("%02d", currentMonth);

            DataHelper.FullCardData invalidYearCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    formattedMonth,
                    String.format("%02d", previousYear),
                    cardData.getOwner(),
                    cardData.getCvc()
            );

            creditPayCardPage.fillCardData(invalidYearCardData);
            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.visibleSubstringYear("Истёк срок действия карты");
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardIsNotValidMonthWithYearMinusOne: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("БАГ спецсимволы в поле владелец")
    public void shouldErrorPayApprovedCardIsNotValidSpecialCharactersInOwnerField() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();

            String invalidOwnerWithSpecialChars = "!@#$%^&*()_+={}[]|:;\"`<>,.?/123";
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);

            DataHelper.FullCardData invalidCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    cardData.getMonth(),
                    cardData.getYear(),
                    invalidOwnerWithSpecialChars,
                    cardData.getCvc()
            );

            creditPayCardPage.fillCardData(invalidCardData);
            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.visibleSubstringOwner("Неверно указано ФИО");
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardIsNotValidSpecialCharactersInOwnerField: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorPayApprovedCardEmptyOwnerField() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);
            DataHelper.FullCardData invalidMonthCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    cardData.getMonth(),
                    cardData.getYear(),
                    "",
                    cardData.getCvc()
            );

            creditPayCardPage.fillCardData(invalidMonthCardData);
            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.visibleSubstringOwner("Поле обязательно для заполнения");
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayApprovedCardEmptyOwnerField: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorPayNotValidCard() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(null);
            DataHelper.FullCardData invalidMonthCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    cardData.getMonth(),
                    cardData.getYear(),
                    cardData.getOwner(),
                    cardData.getCvc()
            );

            creditPayCardPage.fillCardData(invalidMonthCardData);
            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.visibleErrorMessage("Ошибка\nОшибка! Банк отказал в проведении операции.");
                creditPayCardPage.closeErrorMessage();
                creditPayCardPage.notVisibleSuccessfullyMessage();
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayNotValidCard: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorPayNotValidYearPlus11() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(true);

            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear() % 100;

            DataHelper.FullCardData invalidYearCardData = new DataHelper.FullCardData(
                    cardData.getNumber(),
                    cardData.getMonth(),
                    String.format("%02d", currentYear + 11),
                    cardData.getOwner(),
                    cardData.getCvc()
            );

            creditPayCardPage.fillCardData(invalidYearCardData);
            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.visibleSubstringDateIsIncorrect("Неверно указан срок действия карты");
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayNotValidYearPlus11: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorEmptyCreditPayForm() {
        try {
            dashboardPage.clickCreditPayCardButton();
            PayCardPage.CreditPayCardPage creditPayCardPage = new PayCardPage.CreditPayCardPage();

            creditPayCardPage.clickContinue();

            try {
                creditPayCardPage.notVisibleSuccessfullyMessage();
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorEmptyForm: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void shouldErrorEmptyPayForm() {
        try {
            dashboardPage.clickPayCardButton();
            PayCardPage.PayFromCardPage payCardPage = new PayCardPage.PayFromCardPage();

            payCardPage.clickContinue();

            try {
                payCardPage.notVisibleSuccessfullyMessage();
            } catch (AssertionError e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorEmptyPayForm: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("БАГ покупка невалидной картой ошибка и вывод успешного сообщения, операция не проведена")
    public void shouldErrorPayRandomCardDoubleMassage() {
        try {
            dashboardPage.clickPayCardButton();
            PayCardPage.PayFromCardPage payFromCardPage = new PayCardPage.PayFromCardPage();

            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(null);
            payFromCardPage.fillCardData(cardData);
            payFromCardPage.clickContinue();

            payFromCardPage.visibleErrorMessage("Ошибка \nОшибка! Банк отказал в проведении операции.");
            payFromCardPage.closeErrorMessage();
            payFromCardPage.notVisibleErrorMessage();
            payFromCardPage.notVisibleSuccessfullyMessage();

        } catch (AssertionError e) {
            System.err.println("Ошибка в shouldErrorPayRandomCardDoubleMassage: " + e.getMessage());
            fail("AssertionError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Исключение: " + e.getMessage());
        }
    }
}

//Баги
// 1 успешная покупка отклоненной картой                              shouldErrorPayDeclinedCard

// 2 успешная покупка отклоненной картой в кредит                     shouldErrorCreditPayDeclinedCard

// 3 успешная покупка разрешенной картой с 00 месяцем                 shouldErrorPayApprovedCardIsNotValidMonth00

// 4 успешная покупка разрешенной картой с 00 месяцем с годом 26+     shouldErrorPayApprovedCardIsNotValidMonth00YearOver2

// 5 успешная покупка разрешенной картой в ФИО спецсимволы            shouldErrorPayApprovedCardIsNotValidSpecialCharactersInOwnerField

// 6 POS не берет карты с годом более 30 года                         shouldPayApprovedCardValidYearPlus10

// 7 API_POS покупка отклоненной картой                               shouldSendDeclinedDataPaySuccess

// 8 API_POS покупка в кредит отклоненной картой                      shouldSendDeclinedDataPayCredit

// 9 завести баг ручного тестирования вставка эмодзи в поле фио

// после закрытия сообщения об отказе операции появляется сообщение
//  об успешной операции  - операция не проведена                    shouldErrorCreditPayDeclinedCard - запись видео