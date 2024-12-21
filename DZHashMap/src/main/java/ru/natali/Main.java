package ru.natali;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Путь к входному файлу
        String inputFilePath = "dzhashmap/input.txt";
        // Путь к выходному файлу
        String outputFilePath = "dzhashmap/output.txt";

        // Использование try-with-resources для автоматического закрытия потоков
        try {
            // Чтение всех строк из файла - под капотом Files.readAllLines() происходит открытие файла и его чтение,
            // не нужно явно создавать объект BufferedReader или другой поток,
            // Java все равно открывает файловые дескрипторы и управляет ими.
            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));

            // Создание новой коллекции Map
            Map<String, String> myHashMap = new HashMap<>();

            // Разбор каждой строки и добавление её в карту
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    myHashMap.put(key, value);
                }
            }

            // Создаем строку для записи в файл
            StringBuilder result = new StringBuilder();

            // Добавляем содержимое карты
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("c) выполнить прямой перебор коллекции, используя цикл for\n");
            for (Map.Entry<String, String> entry : myHashMap.entrySet()) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
            }

            // Добавляем новый элемент с уже имеющимся ключом
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("d) добавить новый элемент с уже имеющимся в отображении ключом\n");
            myHashMap.put("четвертый", "цуккини");
            for (Map.Entry<String, String> entry : myHashMap.entrySet()) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
            }

            // Выносим ключи в отдельную коллекцию
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("e) вынести список всех ключей из HashMap в отдельную коллекцию \n");
            Set<String> keysMyHashMap = myHashMap.keySet();
            for (String key : keysMyHashMap) {
                result.append(key).append(' ');
            }
            result.append('\n');

            // Выносим значения в коллекцию HashSet и получаем количество уникальных значений
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("f) вынести список всех значений из HashMap в коллекцию " +
                    "HashSet и получить количество уникальных значений \n");
            Set<String> valuesSet = new HashSet<>(myHashMap.values());
            result.append("Количество уникальных значений: ").append(valuesSet.size()).append('\n');

            // Определяем, содержит ли коллекция HashMap определенный ключ
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("g) определить, содержит ли коллекция HashMap определенный ключ \n");
            String findKey = "третий";
            if (myHashMap.containsKey(findKey)) {
                result.append("В HashMap есть ключ ").append(findKey).append('\n');
            } else {
                result.append("Ключ ").append(findKey).append(" отсутствует в HashMap.").append('\n');
            }

            // Определяем, содержит ли коллекция HashMap определенное значение
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("h) определить, содержит ли коллекция HashMap определенное значение \n");
            String findValue = "ананас";
            if (myHashMap.containsValue(findValue)) {
                result.append("В HashMap есть значение ").append(findValue).append('\n');
            } else {
                result.append("Значение ").append(findValue).append(" отсутствует в HashMap.").append('\n');
            }

            // Определяем, сколько раз определенное значение встречается в коллекции HashMap
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("h1) определить, сколько раз определенное значение встречается в HashMap \n");
            String findAllValues = "груша";
            int count = 0;
            for (String value : myHashMap.values()) {
                if (value.equals(findAllValues)) {
                    count++;
                }
            }
            result.append("Значение '").append(findAllValues).append("' встречается ").append(count).append(" раз в HashMap.\n");

            //Получить количество элементов, содержащихся в данный момент в коллекции HashMap
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("i) получить количество элементов, содержащихся в данный момент в коллекции HashMap \n");
            result.append("Количество элементов в коллекции HashMap в данный момент ").append(myHashMap.size()).append('\n');

            //Удалить из коллекции выбранный объект по ключу и по значению
            result.append("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
            result.append("j1) удалить из коллекции выбранный объект по ключу \n");
            myHashMap.remove(findKey);
            for (String key : myHashMap.keySet()) {
                result.append(key).append(' ');
            }
            result.append('\n');
            result.append("Количество элементов в коллекции HashMap в данный момент ").append(myHashMap.size()).append('\n');
            result.append('\n');

            // j2) удалить из коллекции выбранный объект по значению
            //Метод remove принимает ключ, а не значение,
            // поэтому попытка передать значение приведет к ошибке, потому что карта не найдет соответствующего ключа.
            result.append("\nj2) удалить из коллекции выбранный объект по значению \n");

            // Значение, которое хотим удалить
            String removingValue = "груша";

            //Чтобы удалить элемент по значению, необходимо сначала найти соответствующий ключ,
            // а затем удалить этот ключ вместе с его значением.
            // Проходим по всем элементам карты и ищем нужное значение
            Iterator<Map.Entry<String, String>> iterator = myHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (entry.getValue().equals(removingValue)) {
                    // Если нашли соответствующее значение, удаляем элемент, break не делаем, удаляем все значения
                    iterator.remove();
                }
            }

            // Выводим оставшиеся значения
            for (String value : myHashMap.values()) {
                result.append(value).append(' ');
            }
            result.append('\n');
            result.append("Количество элементов в коллекции HashMap в данный момент ").append(myHashMap.size()).append('\n');
            result.append('\n');

            // Запись всего результата в файл
            Files.write(Paths.get(outputFilePath), result.toString().getBytes(StandardCharsets.UTF_8));

            System.out.println("Данные успешно записаны в файл: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
