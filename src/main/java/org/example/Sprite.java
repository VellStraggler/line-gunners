package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.example.Window.CENTER;

/**
 * Sprites can be linked to children sprites
 * circular collision geometry for players
 */
public class Sprite {

    private static final String PATH_PREFIX = "images/";

    List<Sprite> childrenSprites;
    Sprite parentSprite; // This is the highest-level sprite if parentSprite is null.
    // Have a coordinate relative to whatever this sprite is connected to
    // Also store the top-left corner stored for display purposes.
    private Point relativePoint;
    private Point cornerPoint;
    private Point centerPoint;
    private double rotation; // 0 to 360 degrees
    private double sizeMultiplier; //Some sprites will grow and shrink.

    String imagePath;
    Image image;
    int width;
    int height;
    private final Tag tag;

    /**
     * @param imagePath: Do not include "images/", this is automatically added
     * @param spawnpoint: This reflects the center of the sprite, not the corner
     */
    public Sprite(Tag tag, String imagePath, Point spawnpoint) {
        // Find the Image with the path
        this.imagePath = imagePath;
        this.tag = tag;
        rotation = 0.0;

        try {
            image = ImageIO.read(new File(PATH_PREFIX + imagePath));
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            System.out.println("Image could not be loaded with path: " + PATH_PREFIX + imagePath);
            e.printStackTrace();
        }
        if (spawnpoint == null) {
            spawnpoint = CENTER;
        }
        centerPoint = new Point(0,0);
        cornerPoint = new Point(0,0);
        updateCenter(spawnpoint);

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
    public void moveTo(Point newPoint) {
        cornerPoint = newPoint;
    }
    public void moveX(int x){
        updateCorner(new Point(cornerPoint.x + x, cornerPoint.y));
    }
    public void moveY(int y){
        updateCorner(new Point(cornerPoint.x, cornerPoint.y + y));
    }
    public Point getCornerPoint() {
        return new Point(cornerPoint.x, cornerPoint.y);
    }
    private static Point calculateChildPoint(Sprite childSprite) {
        return new Point(0,0);
    }
    private void calculateRotation(int addedRotation) {

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
}
