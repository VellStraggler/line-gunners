package org.example.Objects;

import java.awt.*;


public class Projectile extends Sprite{
    public Point direction;

    public Projectile(String path, Point spawnPoint, Point direction, int speed) {
        super(path, spawnPoint);
        this.direction = direction;
        this.speed = speed;
    }
    public void requestMovementUpdate() {
        requestMove(new Point(direction.x * speed, direction.y * speed));
    }

}
