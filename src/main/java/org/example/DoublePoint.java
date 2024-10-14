package org.example;

import java.awt.*;

public class DoublePoint {

    public double x;
    public double y;

    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point getIntPoint() {
        return new Point((int)x,(int)y);
    }
    public int getIntX() {
        return (int)x;
    }
    public int getIntY() {
        return (int)y;
    }
}
