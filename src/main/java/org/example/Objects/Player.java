package org.example.Objects;

import java.awt.*;

public class Player extends Sprite{
    private double timeLastFired;
    public final int laserCoolDown;
    public final int bombCoolDown;
    public boolean willShootLaser;
    public boolean willPlaceBomb;
    public Player(String imagePath, Point spawnPoint) {
        super(imagePath, spawnPoint);
        laserCoolDown = 1;
        bombCoolDown = 5;
    }

    /**
     * Shoot a thin straight line off the direction being faced
     */
    public Projectile shoot() {
        Point spawnPoint = new Point(getCenterPoint().x + (direction.x * (width/3*2)),
                getCenterPoint().y + (direction.y * (height/3*2)));
        Point shotDirection = new Point(direction.x, direction.y);
        int shotSpeed = 2;
        Projectile shot = new Projectile("bullet.png", spawnPoint, shotDirection, shotSpeed);
        return shot;
    }
    public double getTimeLastFired() {
        return timeLastFired;
    }

    /**
     * @return null if they can't shoot
     */
    public Projectile attemptToFireLaser() {
        Projectile projectile = shoot();
        if (!canFireLaser()) {
            return null;
        }
        willShootLaser = true;
        setTimeLastFired();
        return projectile;
    }

    /**
     * A Bomb is simply a request to clear the area around the player of lines
     */
    public void attemptToPlaceBomb() {
        if (canPlaceBomb()) {
            willPlaceBomb = true;
            setTimeLastFired();
        }
    }
    private void setTimeLastFired() {
        this.timeLastFired = System.currentTimeMillis();
    }
    private boolean canFireLaser() {
        return System.currentTimeMillis() > timeLastFired + (laserCoolDown * 100);
    }
    private boolean canPlaceBomb() {
        return System.currentTimeMillis() > timeLastFired + (bombCoolDown * 100);
    }
}
