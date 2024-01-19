package ru.netology.banklogin.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner(); //объявление константы QUERY_RUNNER

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {  //метод подключения к базе данных, далее все методы смогут его использовать
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass"); //nак мы задаем системное свойство, как в build.gradle
        // return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass"); // если будет нужно подключение к постгерсу, тут нужно будет заменить,
        // это вариант захардкоженного свойства

    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {  //метод получет код верификации, последний, верный
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<String>()); //в метод query передаем (подключение, сам запрос, хендлер для преобразования результата в сущность приложения
        return new DataHelper.VerificationCode(code); //создаем экземпляр класса VerificationCode передав туда полученный code
    }

    @SneakyThrows
    public static void cleanDatabase() {  // метод очищает таблицы
        var connection = getConn();
        QUERY_RUNNER.execute(connection, "DELETE FROM auth_codes"); //вызываем метод execute передаем(подключение, выполняем запрос)
        QUERY_RUNNER.execute(connection, "DELETE FROM card_transactions");
        QUERY_RUNNER.execute(connection, "DELETE FROM cards");
        QUERY_RUNNER.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    public static void cleanAuthCodes() {  // метод очищает только одну строку
        var connection = getConn();
        QUERY_RUNNER.execute(connection, "DELETE FROM auth_codes");
    }
}

