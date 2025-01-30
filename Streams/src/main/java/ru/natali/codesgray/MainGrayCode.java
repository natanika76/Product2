package ru.natali.codesgray;

import ru.natali.oldfiles.GrayCodeGenerator;

import java.util.stream.Stream;

import static ru.natali.codesgray.GrayCodeGenerator.generateGrayCodes;

public class MainGrayCode {
    public static void main(String[] args) {
        Stream<Integer> grayCodeStream = generateGrayCodes(2);

        // Вывод первых 10 элементов
        grayCodeStream.limit(10).forEach(System.out::println);
    }
}
