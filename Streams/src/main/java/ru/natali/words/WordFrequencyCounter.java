package ru.natali.words;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequencyCounter {

    public static void main(String[] args) throws IOException {
        String inputFilePath = "src/main/resources/input.txt"; // Путь к входному файлу
        String outputFilePath = "src/main/resources/output.txt"; // Путь к выходному файлу
        int n = 10; // Количество наиболее часто встречающихся слов для вывода

        // Чтение файла и подсчет частоты слов
        Map<String, Long> wordFrequencyMap = Files.lines(Paths.get(inputFilePath))
                .flatMap(line -> Arrays.stream(line.split("[^\\p{L}\\p{N}]+"))) // Разделение на слова
                .filter(word -> !word.isEmpty()) // Игнорирование пустых строк
                .map(String::toLowerCase) // Приведение к нижнему регистру
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); // Группировка и подсчет

        // Сортировка слов по частоте и лексикографическому порядку
        String result = wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey)
                .limit(n) // Ограничение количеством n
                .collect(Collectors.joining(" ")); // Объединение слов в одну строку с пробелом

        // Запись результата в файл
        Files.write(Paths.get(outputFilePath), result.getBytes());
        System.out.println("Результат записан в файл: " + outputFilePath);
    }
}