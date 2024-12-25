package ru.natali;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {
    //javadoc -d docs src/main/java/ru/natali/Main.java  - команда в терминале создает Javadoc
    // в той же директории, откуда была запущена команда,
    // создается папка docs, там файл index.html открыть в браузере

    /**
     * Метод для построения тернарной операции из предиката и двух функций.
     *
     * @param condition   Предикат, определяющий условие выполнения одной из функций.
     * @param ifTrue      Функция, которая будет выполнена, если предикат вернет true.
     * @param ifFalse     Функция, которая будет выполнена, если предикат вернет false.
     * @return Новая функция, которая возвращает результат применения ifTrue или ifFalse в зависимости от условия.
     */
    private static <T, R> Function<T, R> ternaryOperator(Predicate<? super T> condition,
                                                         Function<? super T, ? extends R> ifTrue,
                                                         Function<? super T, ? extends R> ifFalse) {
        return x -> condition.test(x) ? ifTrue.apply(x) : ifFalse.apply(x);
    }

    public static void main(String[] args) throws IOException {

        // Путь к входному файлу
        String inputFilePath = "src/main/resources/input.txt";
        // Путь к выходному файлу
        String outputFilePath = "src/main/resources/output.txt";

        // Использование try-with-resources для автоматического закрытия потоков
        try (
                FileReader fileReader = new FileReader(inputFilePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            // Считывание всей строки из файла
            //если в файле input.txt нет данных или он содержит только
            // пробелы, программа бросит исключение IOException
            String line = bufferedReader.readLine();
            if (line == null || line.isEmpty()) {
                throw new IOException("Файл пустой или содержит только пробельные символы.");
            }

            // Создание предиката, проверяющего, является ли строка пустой или нулевой
            Predicate<String> isEmptyOrNull = s -> s == null || s.trim().isEmpty();

            // Функции для обработки пустых/не пустых строк
            Function<String, Integer> handleIfTrue = s -> 0; //handleIfTrue возвращает 0, если строка пустая или равна null
            Function<String, Integer> handleIfFalse = String::length; //handleIfFalse возвращает длину строки, если она непуста и не равна null

            // Комбинируем функции через тернарный оператор
            Function<String, Integer> safeStringLength = ternaryOperator(isEmptyOrNull, handleIfTrue, handleIfFalse);

            // Применяем результирующую функцию к строке из файла
            int length = safeStringLength.apply(line);

            // Формируем строку для записи в выходной файл
            String result = "Длина строки: " + length;

            // Запись всего результата в файл
            Files.write(Paths.get(outputFilePath), result.getBytes(StandardCharsets.UTF_8));

            System.out.println("Данные успешно записаны в файл: " + outputFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования строки в число.");
        }
    }
}