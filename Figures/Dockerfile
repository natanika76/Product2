# Используем официальный образ Java
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/Figures-1.0-SNAPSHOT.jar app.jar

# Копируем файлы input.txt и output.txt в контейнер
COPY input.txt input.txt

# Указываем команду для запуска приложения
CMD ["java", "-jar", "app.jar"]

