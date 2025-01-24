# Используем официальный образ OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл приложения в контейнер
COPY target/orders-0.0.1.jar orders-0.0.1.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "orders-0.0.1.jar"]