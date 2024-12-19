package ru.natali.figures;

public class Ellipse extends Figure {
    protected final double a;
    protected final double b;

    public Ellipse(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getPerimeter() {
        // Формула Раманаджана, a и b — длины полуосей эллипса
        return Math.PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
    }
}
