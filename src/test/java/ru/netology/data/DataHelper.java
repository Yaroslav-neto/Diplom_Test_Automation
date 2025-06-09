package ru.netology.data;

import java.time.LocalDate;
import java.util.Random;

import lombok.Value;

public class DataHelper {
    private static final Random RANDOM = new Random();
    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final int CURRENT_YEAR = CURRENT_DATE.getYear() % 100;
    private static final int CURRENT_MONTH = CURRENT_DATE.getMonthValue();

    private static final String[] NAMES = {
            "Иван", "Сергей", "Алексей", "Дмитрий", "Андрей", "Максим",
            "Николай", "Павел", "Д'Артаньян", "Артём"
    };

    private static final String[] SURNAMES = {
            "Иванов", "Петров", "Сидоров", "Кузнецов",
            "Смирнов", "Попов", "Васильев", "Зайцев", "Анна-Мария"
    };

    public static AuthCard getApprovedCardForTestData() {
        return new AuthCard("1111 2222 3333 4444");
    }

    public static AuthCard getDeclinedCardForTestData() {
        return new AuthCard("5555 6666 7777 8888");
    }

    public static String generateCardNumber() {
        return String.format("%04d %04d %04d %04d",
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000));
    }

    public static String generateMonth(int year) {
        int month;
        if (year > CURRENT_YEAR) {
            month = RANDOM.nextInt(12) + 1;
        } else {
            month = RANDOM.nextInt(12 - CURRENT_MONTH + 1) + CURRENT_MONTH;
        }
        return String.format("%02d", month);
    }

    public static String generateYear() {
        int year = RANDOM.nextInt(2) + CURRENT_YEAR;
        return String.format("%02d", year);
    }

    public static String generateOwner() {
        String name = NAMES[RANDOM.nextInt(NAMES.length)];
        String surname = SURNAMES[RANDOM.nextInt(SURNAMES.length)];
        return surname + " " + name;
    }

    public static String generateCVC() {
        return String.format("%03d", RANDOM.nextInt(1000));
    }


    public static FullCardData generateFullCardData(Boolean isApproved) {
        String year = generateYear();
        String month = generateMonth(Integer.parseInt(year));
        String number;

        if (isApproved == null) {
            number = generateCardNumber();
        } else if (isApproved) {
            number = getApprovedCardForTestData().getNumber();
        } else {
            number = getDeclinedCardForTestData().getNumber();
        }

        return new FullCardData(number, month, year, generateOwner(), generateCVC());
    }

    @Value
    public static class AuthCard {
        String number;
    }

    @Value
    public static class FullCardData {
        String number;
        String month;
        String year;
        String owner;
        String cvc;
    }
}

