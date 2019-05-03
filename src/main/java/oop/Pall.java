package oop;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pall extends Circle {
    private double kiirus_x;
    private double kiirus_y;
    public Pall(double x, double y, double raadius, Color värv){
        super(x, y, raadius, värv);
        this.kiirus_x = 0;
        this.kiirus_y = 0;
    }

    public void setKiirus_x(double kiirus_x) {
        this.kiirus_x = kiirus_x;
    }

    public void setKiirus_y(double kiirus_y) {
        this.kiirus_y = kiirus_y;
    }

    public double getKiirus_x() {
        return kiirus_x;
    }

    public double getKiirus_y() {
        return kiirus_y;
    }
}
