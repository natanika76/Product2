package ru.natali.figures;

public class Circle extends Ellipse implements Moveable {
    private int centerX;
    private int centerY;

    public Circle(int centerX, int centerY, double radius) {
        super(radius, radius); // Оба параметра равны радиусу
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public double getPerimeter() {
        // Периметр круга
        return 2 * Math.PI * a;
    }

    @Override
    public void moveTo(int x, int y) {
        this.centerX = x;
        this.centerY = y;
    }

    // Геттеры для получения текущих координат центра круга
    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }
}