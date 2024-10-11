package org.example;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Window extends Frame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final Point CENTER = new Point(WIDTH/2,HEIGHT/2);

    Image offScreenImage;
    Graphics offScreenGraphics;

    public List<Sprite> sprites;

    public Window(List<Sprite> sprites) {
        this.sprites = sprites;
        // Set up the window
        setSize(WIDTH, HEIGHT);
        setTitle("Line Gunner");
        setVisible(true);

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
        paint(g);
    }

    // Override the paint method for double-buffered drawing
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(WIDTH, HEIGHT);
            offScreenGraphics = offScreenImage.getGraphics();
        }

        // Clear the offscreen image first
        offScreenGraphics.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the images onto the offscreen image
        for (Sprite sprite: sprites) {
            if (sprite.image != null) {
                Point cornerPoint = sprite.getCornerPoint();
                offScreenGraphics.drawImage(sprite.image, cornerPoint.x, cornerPoint.y, this);
            }
        }

        // Now draw the offscreen image onto the actual window
        g.drawImage(offScreenImage, 0, 0, this);
    }
}
