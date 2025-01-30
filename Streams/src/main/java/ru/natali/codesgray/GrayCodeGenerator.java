package ru.natali.codesgray;

import java.util.stream.Stream;

import java.util.stream.Stream;

public class GrayCodeGenerator {

    public static Stream<Integer> generateGrayCodes(int n) {
        if (n < 1 || n > 16) {
            throw new IllegalArgumentException("n must be between 1 and 16");
        }

        // Генерация всех возможных кодов Грея для данной битности
        int[] grayCodes = new int[(int)Math.pow(2, n)];
        for (int i = 0; i < grayCodes.length; i++) {
            grayCodes[i] = i ^ (i >> 1);
        }

        // Создание бесконечного потока с использованием Stream.iterate
        return Stream.iterate(0, index -> (index + 1) % grayCodes.length)
                .map(index -> grayCodes[index]);
    }
}