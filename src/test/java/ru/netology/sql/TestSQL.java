package ru.netology.sql;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestSQL {

    static String url = "jdbc:mysql://localhost:3306/app";
    static String user = "app";
    static String password = "pass";

    public static String getStatus() {

        QueryRunner runner = new QueryRunner();
        var status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        String statusActual = null;

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
        ) {
            statusActual = runner.query(connection, status, new ScalarHandler<>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statusActual;
    }

    public static long getCountPayments() {

        QueryRunner runner = new QueryRunner();
        var count = "SELECT COUNT(*) FROM payment_entity";
        long countActual = 0;

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
        ) {
            countActual = runner.query(connection, count, new ScalarHandler<>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countActual;
    }

    public static String getStatusCredit() {

        QueryRunner runner = new QueryRunner();
        var status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        String statusActual = null;

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
        ) {
            statusActual = runner.query(connection, status, new ScalarHandler<>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statusActual;
    }

    public static long getCountCredits() {

        QueryRunner runner = new QueryRunner();
        var count = "SELECT COUNT(*) FROM credit_request_entity";
        long countActual = 0;

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
        ) {
            countActual = runner.query(connection, count, new ScalarHandler<>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countActual;
    }

    public static void dropTables() {

        QueryRunner runner = new QueryRunner();
        String PaymentEntity = "DELETE FROM payment_entity;";
        String CreditRequestEntity = "DELETE FROM credit_request_entity;";
        String OrderEntity = "DELETE FROM order_entity;";

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
        ) {
            runner.update(connection, CreditRequestEntity);
            runner.update(connection, OrderEntity);
            runner.update(connection, PaymentEntity);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
