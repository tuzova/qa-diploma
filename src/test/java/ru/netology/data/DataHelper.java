package ru.netology.data;

import lombok.Value;
import com.github.javafaker.Faker;

import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String cardNumber;
        String month;
        String year;
        String cardOwner;
        String cvc;
    }

    public static class ValidData {

        public static String getApprovedCard() { // одобренная карта
            return "4444444444444441";
        }

        public static String getDeclinedCard() { // не одобренная карта
            return "4444444444444442";
        }

        public static String getRandom13NumberCard() { // 13-тизначный номер карты
            Faker faker = new Faker();
            return String.valueOf(faker.number().digits(13));
        }

        public static String getRandomCard() { // рандомная карта
            Faker faker = new Faker();
            return String.valueOf(faker.number().digits(16));
        }

        public static String getRandom19NumberCard() { // 19-тизначный номер карты
            Faker faker = new Faker();
            return String.valueOf(faker.number().digits(19));
        }

        public static String getMonth() { // Месяц – от 01 до 12, не ранее текущего месяца текущего года
            Faker faker = new Faker();
            return String.format("%02d\n", (faker.number().numberBetween(1, 12)));
        }

        public static String getYear() { // Год – две последние цифры года, не ранее текущего года + 5 лет
            Faker faker = new Faker();
            return String.valueOf(faker.number().numberBetween(22, 27));
        }

        public static String getCardOwner() { // Владелец – латинские символы
            Faker faker = new Faker(new Locale("en-US"));
            return faker.name().lastName();
        }

        public static String getCVC() { // CVC – трехзначное число от 000 до 999
            Faker faker = new Faker();
            return String.valueOf(faker.number().digits(3));
        }
    }

    public static AuthInfo getApprovedData() { // одобренная карта + валидные поля
        return new AuthInfo(ValidData.getApprovedCard(), ValidData.getMonth(), ValidData.getYear(), ValidData.getCardOwner(), ValidData.getCVC());
    }

    public static AuthInfo getDeclinedData() { // не одобренная карта + валидные поля
        Faker faker = new Faker();
        return new AuthInfo(ValidData.getDeclinedCard(), ValidData.getMonth(), ValidData.getYear(), ValidData.getCardOwner(), ValidData.getCVC());
    }

    public static AuthInfo getRandomData() { // рандомная карта + валидные поля
        return new AuthInfo(ValidData.getRandomCard(), ValidData.getMonth(), ValidData.getYear(), ValidData.getCardOwner(), ValidData.getCVC());
    }

    public static class InValidData {

        public static String getEmptyData() { // Тесты с одним незаполненным полем
            return "";
        }

        public static String getSetOfLatinCharacters() { // Тесты с вводом латинских символов
            return "setoflatincharacters";
        }

        public static String getSetOfCyrillicCharacters() { // Тесты с вводом кириллических символов
            return "кириллическиесимволы";
        }

        public static String getSetOfRandomCharacters() { // Тесты с вводом рандомных символов
            return "««==№%%**??№==»»";
        }

        public static String getCardOwnerOver() { // Тест на ввод 34х латинских символов
            return "thirtyfourlatincharactersinastring";
        }

        public static String getOneNumber() { // Тест на ввод однозначного номера
            Faker faker = new Faker();
            return String.valueOf(faker.number().digits(1));
        }

        public static String getTwoNumber() { // Тест на ввод двухзначного номера
            Faker faker = new Faker();
            return String.valueOf(faker.number().digits(2));
        }

        public static String getMonth00() { // Несуществующий номер месяца 00
            return "00";
        }

        public static String getMonth13() { // Несуществующий номер месяца 13
            return "13";
        }

        public static String getLastYear() { // Прошедшие года
            Faker faker = new Faker();
            return String.valueOf(faker.number().numberBetween(19, 21));
        }

        public static String getFutureYear() { // Будущие года
            Faker faker = new Faker();
            return String.valueOf(faker.number().numberBetween(23, 25));
        }

        public static String getYearOver() { // Номер года превышает +5 лет от текущего
            return "28";
        }
    }
}