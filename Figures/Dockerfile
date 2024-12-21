# Используем официальный образ OpenJDK
FROM openjdk:21

# Создаём рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл из папки target в контейнер
COPY target/figures-0.0.1.jar app.jar

# Копируем входной файл
COPY input.txt /app/input.txt

# Устанавливаем точку входа
CMD ["java", "-jar", "app.jar"]
