package ru.natali.figures;

public class Square extends Rectangle implements Moveable {
    private int topLeftX;
    private int topLeftY;

    // Конструктор, который принимает только сторону
    public Square(double side) {
        super(side, side);
        this.topLeftX = 0;
        this.topLeftY = 0;
    }

    public Square(int topLeftX, int topLeftY, double side) {
        super(side, side);
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
    }

    @Override
    public void moveTo(int x, int y) {
        this.topLeftX = x;
        this.topLeftY = y;
    }

    // Геттеры для получения текущих координат верхнего левого угла квадрата
    public int getTopLeftX() {
        return topLeftX;
    }

    public int getTopLeftY() {
        return topLeftY;
    }
}
