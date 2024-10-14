package org.example.Objects;

import org.example.DoublePoint;

import java.awt.*;

import static org.example.Window.CENTER;

/**
 * Objects have
 * circular collision geometry for players
 * Parent class to Sprite, HudItem, and Projectile
 */
public class Object {
    public static final int zLevel = 0;

    private Point cornerPoint;// a rounded version of decimalPoint
//    private DoublePoint decimalPoint;
    private Point centerPoint;
    // Geometry is calculated as the center of the largest lower square of the sprite
    private Point requestedChange;

    public int width;
    public int height;


    /**
     * @param spawnPoint: This reflects the center of the sprite, not the corner
     */
    public Object(Point spawnPoint) {
        requestedChange = new Point(0,0);
        if (spawnPoint == null) {
            spawnPoint = CENTER;
        }
        centerPoint = new Point(0,0);
        cornerPoint = new Point(0,0);
//        decimalPoint = new DoublePoint(0.0, 0.0);
        updateCenter(spawnPoint);

    }
    public boolean wantsToMove() {
        return requestedChange.x != 0 || requestedChange.y != 0;
    }
    public void requestMoveTo(Point newPoint) {
        requestedChange.x = newPoint.x;
        requestedChange.y = newPoint.y;
    }
    public void requestMoveX(int x) {
        requestedChange.x += x;
    }
    public void requestMoveY(int y) {
        requestedChange.y += y;
    }

    /**
     * Moves the center point
     * @param newPoint
     */
    public void moveTo(Point newPoint) {
        updateCenter(newPoint);
        resetRequest();
    }
    public void moveX(int x){
        moveTo(new Point(cornerPoint.x + x, cornerPoint.y));
    }
    public void resetRequest() {
        requestedChange.x = 0;
        requestedChange.y = 0;
    }
    public void moveY(int y){
        moveTo(new Point(cornerPoint.x, cornerPoint.y + y));
    }
    public Point getCornerPoint() {
        return new Point(cornerPoint.x, cornerPoint.y);
    }
    public Point getCenterPoint() { return new Point(centerPoint.x, centerPoint.y);}

    /**
     * Returns the center point with the requested change
     * @return
     */
    public Point getRequestedPoint() { return new Point(centerPoint.x + requestedChange.x, centerPoint.y + requestedChange.y);}
    public Point getRequestedChange() {
        return new Point(requestedChange.x, requestedChange.y);
    }
    private void updateCenter(Point newCenter) {
        centerPoint = newCenter;
        cornerPoint.x = newCenter.x - (width / 2);
        cornerPoint.y = newCenter.y - (height / 2);
    }
    private void updateCorner(Point newCorner) {
        cornerPoint = newCorner;
        centerPoint.x = newCorner.x + (width / 2);
        centerPoint.y = newCorner.y + (height / 2);
    }
    public boolean equals(Object other) {
        return (this.centerPoint.equals(other.centerPoint) && this.height == other.height);
    }
}
