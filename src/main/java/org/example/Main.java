package org.example;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static Sprite player1 = new Sprite(Tag.PLAYER, "lineguy.png", new Point(500, 500));
    public static Sprite player2 = new Sprite(Tag.PLAYER, "lineguy.png", new Point(1000, 400));
    public static boolean[] keys = new boolean[256];

    public static int step = 1;

    public static void main(String[] args) {
        System.out.println("Beginning program...");


        // sprites
        List<Sprite> sprites = List.of(
                player1, player2
        );

        Window window = new Window(sprites);
        window.setFocusable(true);
        window.requestFocus();
        window.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
                //System.out.println(e.getKeyChar() + " pressed.");
            }
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });

        //start timer
        long currFrame;
        long milsPerFrame = 1000 / 120;
        long nextFrame = System.currentTimeMillis() + milsPerFrame;
        int nextSecond = LocalTime.now().getSecond() + 1;
        int inc = 0;
        int inc2 = 0;
        while(true) {
            //Print every 120th of a second
            currFrame = System.currentTimeMillis();
            if (currFrame >= nextFrame) {
                nextFrame = currFrame + milsPerFrame;
                updatePlayers();
                window.repaint();
                inc2++;
            }
            //FPS checker
            if (LocalTime.now().getSecond() >= nextSecond) {
                nextSecond = LocalTime.now().getSecond() + 1;
                System.out.println(inc + " max FPS, " + inc2 + " real FPS");
                inc = 0;
                inc2= 0;
                player1.moveX(1);
            }
            //input is checked automatically by the window
            inc++;
            //test collision changes
            //update sprites
            //update screen
        }
    }
    private static void updatePlayers() {
        // Player 1 (WASD keys)
        if (keys[KeyEvent.VK_W]) {
            player1.moveY(-step);  // Move up
        }
        if (keys[KeyEvent.VK_S]) {
            player1.moveY(step); // Move down
        }
        if (keys[KeyEvent.VK_A]) {
            player1.moveX(-step);  // Move left
        }
        if (keys[KeyEvent.VK_D]) {
            player1.moveX(step);  // Move right
        }

        // Player 2 (Arrow keys)
        if (keys[KeyEvent.VK_UP]) {
            player2.moveY(-step);  // Move up
        }
        if (keys[KeyEvent.VK_DOWN]) {
            player2.moveY(step);  // Move down
        }
        if (keys[KeyEvent.VK_LEFT]) {
            player2.moveX(-step); // Move left
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            player2.moveX(step);  // Move right
        }
    }
}