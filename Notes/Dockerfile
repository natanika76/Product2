# Используем официальный образ OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл приложения в контейнер
COPY target/notes-0.0.1.jar notes-0.0.1.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "notes-0.0.1.jar"]