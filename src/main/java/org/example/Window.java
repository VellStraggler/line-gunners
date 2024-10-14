package org.example;

import org.example.Objects.Object;
import org.example.Objects.Sprite;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Window extends Frame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final Point CENTER = new Point(WIDTH/2,HEIGHT/2);

    Image offScreenImage;
    Graphics offScreenGraphics;
    AffineTransform transform;

    public List<Object> objects;

    public Window(List<Object> objects) {
        this.objects = objects;
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
        if (offScreenImage == null) {
            offScreenImage = createImage(WIDTH, HEIGHT);
            offScreenGraphics = offScreenImage.getGraphics();
        }
        Graphics2D offScreenGraphics2D = (Graphics2D) offScreenGraphics;

        // Clear the offscreen image first
        offScreenGraphics.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the images onto the offscreen image
        for (Object object: objects) {
            if (object instanceof Sprite) {
                Sprite sprite = (Sprite) object;

                transform = new AffineTransform();

                Point cornerPoint = sprite.getCornerPoint();
                //flip images as necessary
                if (sprite.isFacingLeft()) {
                    transform.translate(cornerPoint.x + sprite.width, cornerPoint.y);
                    transform.scale(-1, 1);
                } else {
                    transform.translate(cornerPoint.x, cornerPoint.y);
                }

                offScreenGraphics2D.drawImage(sprite.image, transform, this);
//                offScreenGraphics.drawImage(sprite.image, cornerPoint.x, cornerPoint.y, this);
                // draw a circle representing the geometry of each sprite
//                offScreenGraphics2D.drawOval(cornerPoint.x, cornerPoint.y + (sprite.height/2) - (sprite.width/2), sprite.width, sprite.width);
                offScreenGraphics2D.drawRect(cornerPoint.x, cornerPoint.y, sprite.width, sprite.height);
            }
        }

        // Now draw the offscreen image onto the actual window
        g.drawImage(offScreenImage, 0, 0, this);
    }
}
