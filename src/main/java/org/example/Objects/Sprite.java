package org.example.Objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.signum;

/**
 * Sprites can be linked to children sprites
 * circular collision geometry for players
 */
public class Sprite extends Object{
    private static final String PATH_PREFIX = "images/";

    public List<Sprite> childrenSprites;
    public Sprite parentSprite; // This is the highest-level sprite if parentSprite is null.
    // Have a coordinate relative to whatever this sprite is connected to
    // Also store the top-left corner stored for display purposes.
    private double rotation; // 0 to 360 degrees
    private double sizeMultiplier; //Some sprites will grow and shrink.

    public String imagePath;
    public Image image;

    public Point direction;
    public int speed;
    private boolean facingLeft;

    public double timeOfLastFrame = 0;


    /**
     * By default, the sprite is facing east
     * @param imagePath: Do not include "images/", this is automatically added
     * @param spawnPoint: This reflects the center of the sprite, not the corner
     */
    public Sprite(String imagePath, Point spawnPoint) {
        super(spawnPoint);
        // Find the Image with the path
        this.imagePath = imagePath;
        rotation = 0.0;
        facingLeft = false;
        direction = new Point(1, 0);
        speed = 0;

        setImage(imagePath);
        updateCorner(spawnPoint); // accounts for previously null width and height
    }
    public Image setImage(String imagePath) {
        this.imagePath = imagePath;
        Image oldImage = image;
        try {
            image = ImageIO.read(new File(PATH_PREFIX + imagePath));
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            System.out.println("Image could not be loaded with path: " + PATH_PREFIX + imagePath);
            e.printStackTrace();
        }
        return oldImage;

    }

    /**
     * Assumes a syntax of "Name" + frame number + ".png"
     * there's a maximum of 10 frames you can have
     * @return
     */
    public void nextFrame() {
        int nextFrame = Integer.parseInt(imagePath.substring(imagePath.length()-5, imagePath.length()-4)) + 1;
        nextFrame = Math.min(nextFrame, 9);
        setImage(imagePath.substring(0,imagePath.length()-5) + nextFrame + ".png");
        timeOfLastFrame = System.currentTimeMillis();
    }

    /**
     * see nextFrame()
     */
    public void prevFrame() {
        int prevFrame = Integer.parseInt(imagePath.substring(imagePath.length()-5, imagePath.length()-4)) - 1;
        prevFrame = Math.max(prevFrame, 1);
        setImage(imagePath.substring(0,imagePath.length()-5) + prevFrame + ".png");
        timeOfLastFrame = System.currentTimeMillis();
    }

    /**
     * I probably should just store this as a variable, but oh well.
     * @return
     */
    public int getFrame() {
        return Integer.parseInt(imagePath.substring(imagePath.length()-5, imagePath.length()-4)) - 1;
    }
    public void setFrame(int f) {
        setImage(imagePath.substring(0,imagePath.length()-5) + f + ".png");
        timeOfLastFrame = System.currentTimeMillis();
    }
    public void addChildSprite(Sprite child) {
        if(childrenSprites == null) {
            childrenSprites = new ArrayList<>();
        }
        childrenSprites.add(child);
    }
    public boolean hasChildren() {
        return childrenSprites != null;
    }
    /**
     * Rotates the current sprite and all childrenSprites
     * @param addedRotation: simplifies this with the rotation for somewhere 0 to 360 degrees
     */
    public void rotate(int addedRotation) {
        calculateRotation(addedRotation);
        for (Sprite sprite: childrenSprites) {
            // get the new coordinate of this sprite
            Point newPoint = calculateChildPoint(sprite);
            sprite.moveTo(newPoint);
            sprite.rotate(addedRotation);
        }
    }
    public boolean isFacingLeft() {
        return facingLeft;
    }
    private static Point calculateChildPoint(Sprite childSprite) {
        return new Point(0,0);
    }
    private void calculateRotation(int addedRotation) {
        rotation = (rotation + addedRotation)%360;
        if (rotation < 0) {
            rotation += 360;
        }
    }

    /**
     * These can never both be 0.
     * @param x
     * @param y
     */
    public void setFacing(int x, int y) {
        if (x != 0 || y != 0) {
            direction.x = signum(x);
            direction.y = signum(y);
        }
        if (x < 0) {
            facingLeft = true;
        } else if (x > 0) {
            facingLeft = false;
        }
    }
}
