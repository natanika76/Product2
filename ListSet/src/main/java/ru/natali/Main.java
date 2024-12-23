package ru.natali;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Путь к входному файлу
        String inputFilePath = "input.txt";
        // Путь к выходному файлу
        String outputFilePath = "output.txt";

        // Использование try-with-resources для автоматического закрытия потоков
        try (
                FileReader fileReader = new FileReader(inputFilePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            // Считывание всей строки из файла
            String line = bufferedReader.readLine();
            if (line == null || line.isEmpty()) {
                throw new IOException("Файл пустой или содержит только пробельные символы.");
            }

            // Создаем строку для записи в файл
            StringBuilder result = new StringBuilder();

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("a) Массив из чисел, прочитанный из файла:\n");
            // Разделение строки на отдельные числа
            String[] parts = line.split("\\s+"); // Разделяем по одному или нескольким пробелам

            // Создание массива чисел
            int[] numbersArray = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                if (!parts[i].isBlank()) { // Проверка на пустоту
                    numbersArray[i] = Integer.parseInt(parts[i].trim());
                }
            }
            //Записываем в файл
            for (int num : numbersArray) {
                result.append(num + " ");
            }

            // Создание списка на основе массива
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("b) Список List, созданный на основе массива:\n");
            List<Integer> numbersList = new ArrayList<>(Arrays.stream(numbersArray).boxed().toList());

            for (int num : numbersList) {
                result.append(num + " : ");
            }

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("с) Отсортировать список в натуральном порядке:\n");
            Collections.sort(numbersList);
            String sortedListAsString = numbersList.toString();
            result.append(sortedListAsString);

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("d) Отсортировать список в обратном порядке:\n");
            Collections.sort(numbersList, Collections.reverseOrder());
            String reversedListAsString = numbersList.toString();
            result.append(reversedListAsString);

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("e) Перемешивание списка:\n");
            Collections.shuffle(numbersList);
            result.append(numbersList);

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("f) Циклический сдвиг на 1 элемент:\n");
            Collections.rotate(numbersList, 1);
            result.append(numbersList);

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("g) Оставить в списке только уникальные элементы:\n");
            Set<Integer> uniqueElements = new LinkedHashSet<>(numbersList); //сохранит порядок
            result.append(uniqueElements);

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("h) Оставить в списке только дублирующиеся элементы:\n");
            Set<Integer> allElements = new HashSet<>();
            Set<Integer> duplicateElements = new LinkedHashSet<>();
            for (Integer num : numbersList) {
                if(!allElements.add(num)) {
                    duplicateElements.add(num);
                }
            }
            result.append(duplicateElements);

            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("i) Получить из списка массив:\n");
            //Передача new Integer[0] означает создание нового пустого массива типа Integer
            Integer[] arrayFromList = numbersList.toArray(new Integer[0]); //метод сам создаст массив правильного размера и типа
            result.append(Arrays.toString(arrayFromList));

            //Исследование HashSet вынесено в отдельный метод
            exploreHashSet(result);

            // Запись всего результата в файл
            Files.write(Paths.get(outputFilePath), result.toString().getBytes(StandardCharsets.UTF_8));

            System.out.println("Данные успешно записаны в файл: " + outputFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования строки в число.");
        }
    }

    private static void exploreHashSet(StringBuilder result) {
        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("a) Создать HashSet и добавить 10 строк:\n");
        HashSet<String> hashSet =  new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            hashSet.add("String" + i);
        }
        result.append(hashSet);
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("b) Добавить в множество минимум 5 объектов, которые являются совместимыми с типом данных коллекции:\n");
        hashSet.add("Extra1");
        hashSet.add("Extra2");
        hashSet.add("Extra3");
        hashSet.add("Extra4");
        hashSet.add("Extra5");
        result.append(hashSet);
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("c) Вывести на экран элементы множества, используя цикл for:\n");
        for (String elem : hashSet) {
            result.append(elem).append(" ");
        }
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("d) Добавить новый элемент, который уже присутствует в множестве:\n");
        hashSet.add("String1");
        result.append(hashSet);
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("e) Определить, содержит ли коллекция определенный объект:\n");
        String checkElement = "String5";
        boolean contains = hashSet.contains(checkElement);
        result.append(checkElement).append(" : ").append(contains);

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("f) Удалить из коллекции любой объект:\n");
        hashSet.remove("String8");
        result.append(hashSet);
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("g) Получить количество элементов, содержащихся в коллекции на данный момент:\n");
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("h) Удалить все элементы множества:\n");
        hashSet.clear();
        result.append("\n").append("Количество элементов в коллекции: ").append(hashSet.size());

        result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        result.append("i) Проверить, является ли коллекция пустой:\n");
        boolean isEmpty = hashSet.isEmpty();
        result.append(isEmpty ? "В коллекции пусто." : "В коллекции есть элементы.");

    }

}

