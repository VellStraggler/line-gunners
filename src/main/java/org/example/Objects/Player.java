package org.example.Objects;

import org.example.Window;

import java.awt.*;

public class Player extends Sprite{
    public static final Point PLAYER_SIZE = new Point(80,118);
    public static final Point faceSpawnPoint = new Point(28,10);
    public static final int BODY_FRAMES = 4;
    public static final int BACKPACK_FRAMES = 5;
    public static final int RELOAD_TIME = 650;
    public static final int FRAME_LENGTH = 250; // in milliseconds

    private double timeLastFired;
    public double timeLastReload;
    public final int laserCoolDown;
    public final int bombCoolDown;
    public boolean willShootLaser;
    public boolean willPlaceBomb;

    private Sprite face;
    private Sprite arms;
    public Sprite backpack;


    public Player(Point spawnPoint) {
        super("body_frame_1.png", spawnPoint);
        laserCoolDown = 100;
        bombCoolDown = 500;
        geometry = new Point(PLAYER_SIZE.x - 10,PLAYER_SIZE.y);
        face = new Sprite("face.png", faceSpawnPoint);
        arms = new Sprite("arms.png", new Point(15,45));
        backpack = new Sprite("backpack_frame_5.png", new Point(-14,0));
        //Maintain this order always \/
        addChildSprite(face);
        addChildSprite(backpack);
        addChildSprite(arms);
    }

    /**
     * Shoot a thin straight line off the direction being faced
     */
    public Projectile shoot() {
        Point spawnPoint = new Point(getCenterPoint().x + (direction.x * ((width/2)+ 10)),
                getCenterPoint().y + (direction.y * ((height/2) + 10)));
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
            willShootLaser = false;
            return null;
        }
        willShootLaser = true;
        backpack.prevFrame();
        setTimeLastFired();
        return projectile;
    }
    public void setFacing(int x, int y) {
        super.setFacing(x,y);
        // Move the face up or down or not at all
        if (y == 0) {
            face.updateCorner(faceSpawnPoint);
        } else if (y > 0) {
            face.updateCorner(new Point(faceSpawnPoint.x, faceSpawnPoint.y + 8));
        } else { // y < -1
            face.updateCorner(new Point(faceSpawnPoint.x, faceSpawnPoint.y - 8));
        }
    }

    /**
     * A Bomb is simply a request to clear the area around the player of lines
     */
    public void attemptToPlaceBomb() {
        if (canPlaceBomb()) {
            willPlaceBomb = true;
            setTimeLastFired();
            backpack.prevFrame();
        }
    }
    private void setTimeLastFired() {
        this.timeLastFired = System.currentTimeMillis();
        this.timeLastReload = timeLastFired;
    }

    /**
     * resets your reload time every time you try to shoot
     * @return
     */
    private boolean canFireLaser() {
        this.timeLastReload = System.currentTimeMillis();
        if (timeLastReload > timeLastFired + (laserCoolDown)) {
            int frame = backpack.getFrame();
            if (frame < 1) {
                return false;
            }
            return true;
        }
        return false;
//        return (timeLastReload > timeLastFired + (laserCoolDown) && getFrame() != 1);
    }
    private boolean canPlaceBomb() {
        return System.currentTimeMillis() > timeLastFired + (bombCoolDown);
    }
}
