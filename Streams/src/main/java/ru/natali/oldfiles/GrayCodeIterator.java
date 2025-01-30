package ru.natali.oldfiles;

import java.util.Iterator;

/*
Создадим бесконечный поток данных с помощью итератора,
который будет циклически возвращать элементы массива кодов Грея
 */
public class GrayCodeIterator implements Iterator<Integer> {
    private final int[] grayCodes;
    private int currentIndex = 0;

    public GrayCodeIterator(int n) {
        if (n < 1 || n > 16) {
            throw new IllegalArgumentException("n must be between 1 and 16");
        }
/*
Для каждой битности мы создаём массив размером 2 в степени n.
Каждый элемент массива является соответствующим кодом Грея, полученным через
операцию XOR с самим собой, сдвинутым вправо на один бит (i ^ (i >> 1)).
 */
        this.grayCodes = new int[(int) Math.pow(2, n)];
        for (int i = 0; i < grayCodes.length; i++) {
            grayCodes[i] = i ^ (i >> 1);
        }
    }

    @Override
    public boolean hasNext() {
        return true; // Это бесконечный итератор
    }

    @Override
    public Integer next() {
        if (currentIndex >= grayCodes.length) {
            currentIndex = 0;
        }

        return grayCodes[currentIndex++];
    }
}
