package ru.natali.codesgray;

import java.util.stream.Stream;

public class MainGrayCode {
    public static void main(String[] args) {
        Stream<Integer> grayCodeStream = GrayCodeGenerator.generateGrayCodes(2);

        // Вывод первых 10 элементов
        grayCodeStream.limit(10).forEach(System.out::println);
    }
}
