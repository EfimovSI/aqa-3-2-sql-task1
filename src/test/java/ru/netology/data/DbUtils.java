package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DbUtils {
    public DbUtils() {}

    @SneakyThrows
    public static String getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes);";
        var runner = new QueryRunner();
        String verificationCode;

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var code = runner.query(conn, codeSQL, new ScalarHandler<>());
            verificationCode = (String) code;
        }
        return verificationCode;
    }

    @SneakyThrows
    public static void clearDb () {
        var authSQL = "DELETE FROM auth_codes";
        var cardsSQL = "DELETE FROM cards";
        var usersSQL = "DELETE FROM users";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.execute(conn, authSQL, new ScalarHandler<>());
            runner.execute(conn, cardsSQL, new ScalarHandler<>());
            runner.execute(conn, usersSQL, new ScalarHandler<>());
        }
    }
}
