# Используем официальный образ Maven с OpenJDK 21 (актуальный тег)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем сначала pom.xml для кеширования зависимостей
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение (пропускаем тесты)
RUN mvn clean package -DskipTests

# Финальный образ с Java 21 (используем eclipse-temurin как аналог OpenJDK)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Копируем собранный JAR
COPY --from=build /app/target/pizzeria-0.0.1-SNAPSHOT.jar ./pizzeria.jar

# Открываем порт Spring Boot
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "pizzeria.jar"]