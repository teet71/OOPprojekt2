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


}
