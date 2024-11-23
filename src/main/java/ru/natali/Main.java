package ru.natali;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Product> products = new ArrayList<>();

        products.add(new Product(1L, "Шкаф", 3655.80, 5));
        products.add(new Product(2L, "Стул", 1386.20, 12));
        products.add(new Product(3L, "Комод", 5631.77, 4));
        products.add(new Product(4L, "Стол", 2691.50, 9));
        products.add(new Product(5L, "Тумба", 1739.15, 6));

        for (Product product : products) {
            System.out.println(product);
        }

        System.out.println("\r\nВСЕГО товаров на складе " + products.size() + " наименований: ");
        for (Product product : products) {
            Double totalPrice = printCost(product);
            System.out.printf("%s %.2f %s", product.getName(), totalPrice, "\r\n");
        }
    }

    private static Double printCost(Product product) {
        return product.getPrice() * product.getQuantity();
    }
}