package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    // валидный пользователь
    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123"); // id 9abf2854-58df-4f11-9d63-8360ab526a85
    }

    // невалидный пользователь
    public static AuthInfo getInvalidAuthInfo() {
        Faker faker = new Faker();
        return new AuthInfo(faker.name().username(), faker.internet().password());
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    // валидный сгенерированный код из СУБД
    public static VerificationCode getValidVerificationCode(AuthInfo authInfo) {
        QueryRunner runner = new QueryRunner();
        // comment: выражение запроса для поиска последнего кода, можно использовать сортировку по полю created и оператор LIMIT
        String authCode = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";

        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            authCode = runner.query(connection, authCode, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace(); // comment: можно хотя бы в консоль ошибку вывести
        }

        return new VerificationCode(authCode);
    }

    // невалидный код
    public static VerificationCode getInvalidVerificationCode(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    // очистка всех данных из таблиц
    public static void dropTables() {
        QueryRunner runner = new QueryRunner();
        String Cards = "DELETE FROM cards;";
        String CardTransactions = "DELETE FROM card_transactions;";
        String AuthCodes = "DELETE FROM auth_codes;";
        String Users = "DELETE FROM users;"; // comment: таблица users может быть очищена только в последнюю очередь

        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(connection, Cards);
            runner.update(connection, CardTransactions);
            runner.update(connection, AuthCodes);
            runner.update(connection, Users); // comment: таблица users может быть очищена только в последнюю очередь

        } catch (SQLException throwables) {
            throwables.printStackTrace(); // comment: можно хотя бы в консоль ошибку вывести
        }
    }
}



