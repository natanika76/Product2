# Используем официальный образ OpenJDK
FROM openjdk:21

# Создаём рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл из папки target в контейнер
COPY target/functioninterface-0.0.1.jar app.jar

# Копируем входные файлы
COPY ./src/main/resources/input.txt /app/src/main/resources/input.txt

# Устанавливаем точку входа
CMD ["java", "-jar", "app.jar"]




