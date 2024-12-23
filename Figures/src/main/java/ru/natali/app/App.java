package ru.natali.app;

import ru.natali.figures.Circle;
import ru.natali.figures.Ellipse;
import ru.natali.figures.Rectangle;
import ru.natali.figures.Square;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#0.00");

        String inputFileName = "Figures/input.txt";
        String outputFileName = "Figures/output.txt";

        List<String> results = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String shapeType = parts[0].trim().toLowerCase();

                switch (shapeType) {
                    case "rectangle":
                        double rectWidth = Double.parseDouble(parts[1]);
                        double rectHeight = Double.parseDouble(parts[2]);
                        Rectangle rectangle = new Rectangle(rectWidth, rectHeight);
                        results.add("Периметр прямоугольника: " + df.format(rectangle.getPerimeter()));
                        break;

                    case "square":
                        int squareX = Integer.parseInt(parts[1]);
                        int squareY = Integer.parseInt(parts[2]);
                        double squareSide = Double.parseDouble(parts[3]);
                        Square square = new Square(squareX, squareY, squareSide);
                        results.add("Периметр квадрата: " + df.format(square.getPerimeter()));
                        results.add("Начальная позиция квадрата: (" + square.getTopLeftX() + ", " + square.getTopLeftY() + ")");
                        square.moveTo(squareX + 5, squareY + 5);
                        results.add("Новая позиция квадрата: (" + square.getTopLeftX() + ", " + square.getTopLeftY() + ")");
                        break;

                    case "circle":
                        int circleX = Integer.parseInt(parts[1]);
                        int circleY = Integer.parseInt(parts[2]);
                        double circleRadius = Double.parseDouble(parts[3]);
                        Circle circle = new Circle(circleX, circleY, circleRadius);
                        results.add("Длина окружности: " + df.format(circle.getPerimeter()));
                        results.add("Начальная позиция круга: (" + circle.getCenterX() + ", " + circle.getCenterY() + ")");
                        circle.moveTo(circleX + 10, circleY + 10);
                        results.add("Новая позиция круга: (" + circle.getCenterX() + ", " + circle.getCenterY() + ")");
                        break;

                    case "ellipse":
                        double ellipseA = Double.parseDouble(parts[1]);
                        double ellipseB = Double.parseDouble(parts[2]);
                        Ellipse ellipse = new Ellipse(ellipseA, ellipseB);
                        results.add("Длина окружности эллипса: " + df.format(ellipse.getPerimeter()));
                        break;

                    default:
                        results.add("Неизвестная форма: " + shapeType);
                        break;
                }
            }

            // Запись результатов в файл
            for (String result : results) {
                writer.println(result);
            }
            System.out.println("Запись в файл прошла успешно.");

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлами: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка в формате данных: " + e.getMessage());
        }
    }
}
