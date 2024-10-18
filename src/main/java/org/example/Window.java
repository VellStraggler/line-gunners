package org.example;

import org.example.Objects.Object;
import org.example.Objects.Player;
import org.example.Objects.Projectile;
import org.example.Objects.Sprite;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

public class Window extends Frame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final Point CENTER = new Point(WIDTH/2,HEIGHT/2);

    Image offScreenImage;
    BufferedImage drawingsLayer;
    Graphics2D drawingsGraphics;
    Graphics2D offScreenGraphics;
    AffineTransform transform;

    public List<Object> objects;
    public List<Projectile> projectiles;

    public Window(List<Object> objects, List<Projectile> projectiles) {
        this.objects = objects;
        this.projectiles = projectiles;
        // Set up the window
        setSize(WIDTH, HEIGHT);
        setTitle("Line Gunner");
        setVisible(true);

        transform = new AffineTransform();

        // Window close handler
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }

    // Override the update method to prevent flickering (optional, part of double buffering)
    @Override
    public void update(Graphics g) {
        paint((Graphics2D) g);
    }

    // Override the paint method for double-buffered drawing
    public void paint(Graphics2D g) {
        if (offScreenImage == null || drawingsLayer == null) {
            offScreenImage = createImage(WIDTH, HEIGHT);
            drawingsLayer = (BufferedImage) createImage(WIDTH, HEIGHT);
            offScreenGraphics = (Graphics2D) offScreenImage.getGraphics();
            // this directly modifies the offScreenImage, which is
            // then drawn to the main graphics g
            drawingsGraphics = (Graphics2D) drawingsLayer.getGraphics();
        }

        // Clear the offscreen image first
        offScreenGraphics.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the images onto the drawingsGraphics image
        for (Projectile object: projectiles) {
            drawingsGraphics.setColor(Color.red);
            drawingsGraphics.drawRect(object.getCornerPoint().x + 3, object.getCornerPoint().y + 3, 2, 2);
            // this is never cleared like the offscreen
        }
        offScreenGraphics.drawImage(drawingsLayer,0 ,0, this);
        for (Object object: objects) {
            if (object instanceof Player) {
                Player player = (Player) object;
                offScreenGraphics.setColor(Color.lightGray);
                offScreenGraphics.fillOval(player.getCornerPoint().x, player.getCornerPoint().y + player.height - 10,
                        player.width, 20);
                // BOMBS
                if (player.willPlaceBomb) {
                    drawingsGraphics.fillOval(player.getCenterPoint().x - player.height, player.getCenterPoint().y - player.height,
                            player.height * 2, player.height * 2); // using height since it's the biggest
                    player.willPlaceBomb = false;
                }
            }
            if (object instanceof Sprite) {
                Sprite sprite = (Sprite) object; //Draw the parent first
                Point cornerPoint = sprite.getCornerPoint();
                transform = new AffineTransform();
                //flip images as necessary
                if (sprite.isFacingLeft()) {
                    transform.translate(cornerPoint.x + sprite.width, cornerPoint.y);
                    transform.scale(-1, 1);
                } else {
                    transform.translate(cornerPoint.x, cornerPoint.y);
                }
                offScreenGraphics.drawImage(sprite.image, transform, this);

                //Draw the children over it.
                if(sprite.hasChildren()) {
                    for(Sprite child: sprite.childrenSprites) {
                        Point childCorner = child.getCornerPoint();
                        transform = new AffineTransform();
                        //flip images as necessary
                        if (sprite.isFacingLeft()) {
                            transform.translate(cornerPoint.x + sprite.width - childCorner.x,
                                    cornerPoint.y + childCorner.y);
                            transform.scale(-1, 1);
                        } else {
                            transform.translate(cornerPoint.x + childCorner.x,
                                        cornerPoint.y + childCorner.y);
                        }
                        offScreenGraphics.drawImage(child.image, transform, this);
                    }
                }
                // draw a circle representing the geometry of each sprite
//                offScreenGraphics.drawOval(cornerPoint.x, cornerPoint.y + (sprite.height/2) - (sprite.width/2), sprite.width, sprite.width);
//                offScreenGraphics.drawRect(cornerPoint.x, cornerPoint.y, sprite.width, sprite.height);
            }
        }

        // Now draw the offscreen image onto the actual window
        g.drawImage(offScreenImage, 0, 0, this);
    }
}
