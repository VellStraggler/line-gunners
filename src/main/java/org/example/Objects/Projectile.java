package org.example.Objects;

import java.awt.*;

public class Projectile extends Object{
    public static final int zLevel = -1;
    private int direction; //out of 360
    private int speed;
    private int lifespan; //in seconds

    public Projectile(Point spawnPoint, int direction, int speed, int lifespan) {
        super(spawnPoint);
        this.direction = direction;
        this.speed = speed;
        this.lifespan = lifespan;
    }

}
