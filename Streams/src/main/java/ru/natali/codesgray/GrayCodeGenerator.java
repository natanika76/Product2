package ru.natali.codesgray;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GrayCodeGenerator {
    public static Stream<Integer> generateGrayCodes(int n) {
        GrayCodeIterator iterator = new GrayCodeIterator(n);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
}
