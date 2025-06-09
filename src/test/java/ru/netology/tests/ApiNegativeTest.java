package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.netology.data.ApiHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

import java.net.http.HttpResponse;

public class ApiNegativeTest {

    @AfterEach
    void verifyDataInsertedAndClean() {
        boolean recordInserted = SQLHelper.isRecordInsertedInLastHalfMinute();
        assertFalse(recordInserted, "Записи не были добавлены в базу данных за последние 30 сек.");
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
    public void shouldSendNotValidDataPay() {
        try {
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(null);
            String jsonBody = ApiHelper.createJsonBody(cardData);

            HttpResponse<String> response = ApiHelper.sendPostRequestPay(jsonBody);

            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Ответ API: " + response.body());

            assertTrue(
                    response.statusCode() == 500 && response.body().contains("Internal Server Error"),
                    "Ответ не содержит ожидаемого статуса или статус не 500"
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест завершился исключением: " + e.getMessage());
        }
    }

    @Test
    public void shouldSendNotValidDataPayCredit() {
        try {
            DataHelper.FullCardData cardData = DataHelper.generateFullCardData(null);
            String jsonBody = ApiHelper.createJsonBody(cardData);

            HttpResponse<String> response = ApiHelper.sendPostRequestPayCredit(jsonBody);

            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Ответ API: " + response.body());

            assertTrue(
                    response.statusCode() == 500 && response.body().contains("Internal Server Error"),
                    "Ответ не содержит ожидаемого статуса или статус не 500"
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест завершился исключением: " + e.getMessage());
        }
    }
}