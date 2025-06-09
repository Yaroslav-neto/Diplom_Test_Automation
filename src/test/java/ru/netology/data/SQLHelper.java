package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void cleanDatabase() {
        int maxAttempts = 2;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try (var conn = getConn()) {
                conn.setAutoCommit(false);
                try {
                    QUERY_RUNNER.execute(conn, "DELETE FROM payment_entity");
                    QUERY_RUNNER.execute(conn, "DELETE FROM order_entity");
                    QUERY_RUNNER.execute(conn, "DELETE FROM credit_request_entity");
                    conn.commit();
                    break; // успех, выходим из цикла
                } catch (SQLException e) {
                    conn.rollback();
                    attempt++;
                    System.err.println("Ошибка при выполнении запроса, попытка " + attempt + ": " + e.getMessage());
                    if (attempt >= maxAttempts) {
                        System.err.println("Достигнуто максимальное количество попыток");
                    }
                }
            }
        }
    }

    @SneakyThrows
    public static boolean isRecordInsertedInLastHalfMinute() {
        String queryCredit = "SELECT COUNT(*) FROM credit_request_entity WHERE created >= NOW() - INTERVAL 0.5 MINUTE";
        String queryOrder = "SELECT COUNT(*) FROM order_entity WHERE created >= NOW() - INTERVAL 0.5 MINUTE";
        String queryPayment = "SELECT COUNT(*) FROM payment_entity WHERE created >= NOW() - INTERVAL 0.5 MINUTE";
        try (var conn = getConn()) {
            Long countCredit = QUERY_RUNNER.query(conn, queryCredit, new org.apache.commons.dbutils.handlers.ScalarHandler<>());
            Long countOrder = QUERY_RUNNER.query(conn, queryOrder, new org.apache.commons.dbutils.handlers.ScalarHandler<>());
            Long countPayment = QUERY_RUNNER.query(conn, queryPayment, new org.apache.commons.dbutils.handlers.ScalarHandler<>());
            return (countCredit != null && countCredit > 0) ||
                    (countOrder != null && countOrder > 0) ||
                    (countPayment != null && countPayment > 0);
        }
    }
}
