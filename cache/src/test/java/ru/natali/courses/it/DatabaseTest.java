package ru.natali.courses.it;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class DatabaseTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("dbstudents")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    public void testDatabaseConnection() throws Exception {
        // Получаем JDBC URL из контейнера
        String jdbcUrl = postgres.getJdbcUrl();

        // Подключаемся к базе
        try (Connection connection = DriverManager.getConnection(jdbcUrl, "postgres", "postgres");
             Statement statement = connection.createStatement()) {

            // Создаем схему (если нужно)
            statement.execute("CREATE SCHEMA IF NOT EXISTS courses1_schema");

            // Проверяем, что схема создана
            ResultSet resultSet = statement.executeQuery(
                    "SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'courses1_schema'");

            assertEquals(true, resultSet.next());
        }
    }

    @Test
    public void testLiquibaseMigrations() throws Exception {
        String jdbcUrl = postgres.getJdbcUrl();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, "postgres", "postgres")) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-1.0.sql",
                    new ClassLoaderResourceAccessor(),
                    database);

            // Применяем миграции
            liquibase.update("");

            // Проверяем, что миграции применились
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                        "SELECT table_name FROM information_schema.tables " +
                                "WHERE table_schema = 'courses1_schema'");

                // Проверяем наличие таблиц (пример)
                assertTrue(rs.next());
            }
        }
    }
}

