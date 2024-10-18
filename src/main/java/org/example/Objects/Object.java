package org.example.Objects;
import java.awt.*;

import static org.example.Window.CENTER;

/**
 * Objects have
 * circular collision geometry for players
 * Parent class to Sprite, HudItem, and Projectile
 */
public class Object {
    public static final int zLevel = 0;

    private Point cornerPoint;
    private Point centerPoint;
    // Geometry is calculated as the center of the largest lower square of the sprite
    private Point requestedChange;

    public int width;
    public int height;


    /**
     * @param spawnPoint: This reflects the center of the sprite, not the corner
     */
    public Object(Point spawnPoint) {
        if (spawnPoint == null) {
            spawnPoint = CENTER;
        }
        requestedChange = new Point(0,0);
        centerPoint = new Point(0,0);
        cornerPoint = new Point(0,0);
        updateCenter(spawnPoint);

    }
    public boolean wantsToMove() {
        return requestedChange.x != 0 || requestedChange.y != 0;
    }

    /**
     * This is the amount to add, not the new local position.
     * @param newPoint
     */
    public void requestMove(Point newPoint) {
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
    public void acceptMoveRequest() {
        updateCenter(getRequestedPoint());
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
     * Returns the center point with the requested change added to it
     * @return
     */
    public Point getRequestedPoint() { return new Point(centerPoint.x + requestedChange.x, centerPoint.y + requestedChange.y);}

    /**
     *
     * @return the amount of positional change this object wants
     */
    public Point getRequestedChange() {
        return new Point(requestedChange.x, requestedChange.y);
    }
    private void updateCenter(Point newCenter) {
        centerPoint.x = newCenter.x;
        centerPoint.y = newCenter.y;
        cornerPoint.x = newCenter.x - (width / 2);
        cornerPoint.y = newCenter.y - (height / 2);
    }
    void updateCorner(Point newCorner) {
        cornerPoint = newCorner;
        centerPoint.x = newCorner.x + (width / 2);
        centerPoint.y = newCorner.y + (height / 2);
    }
    public boolean equals(Object other) {
        return (this.centerPoint.equals(other.centerPoint) && this.height == other.height);
    }
}
