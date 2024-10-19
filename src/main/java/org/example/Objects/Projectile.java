package org.example.Objects;

import java.awt.*;


public class Projectile extends Sprite{
    public Point direction;
    public double birthTime;

    public Projectile(String path, Point spawnPoint, Point direction, int speed) {
        super(path, spawnPoint);
        this.direction = direction;
        this.speed = speed;
        birthTime = System.currentTimeMillis();
    }
    public void requestMovementUpdate() {
        requestMove(new Point(direction.x * speed, direction.y * speed));
    }

}
