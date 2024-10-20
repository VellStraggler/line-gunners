package org.example;

import org.example.Objects.Object;
import org.example.Objects.Player;
import org.example.Objects.Projectile;
import org.example.Objects.Sprite;

import java.awt.*;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // players and projectiles both count as objects, so they are also kept in the object list
    public static Player player1 = new Player(new Point(64, Window.HEIGHT / 2));
    public static Player player2 = new Player(new Point(Window.WIDTH - 128, Window.HEIGHT / 2));
    public static boolean[] keys = new boolean[256];
    public static List<Player> players = List.of(player1, player2);
    public static List<Object> objects = new ArrayList<>();
    public static List<Projectile> projectiles = new ArrayList<>();
    public static BufferedImage drawingsLayer;
    public static boolean runGame;

    public static int step = 1;

    public static void main(String[] args) {
        System.out.println("Beginning program...");
        objects.add(player1);
        objects.add(player2);
        runGame = true;

        Window window = new Window(objects, projectiles);
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

        //get the drawingLayers image from the window now that it's been made (hopefully)


        //start timer
        long currFrame;
        long milsPerFrame = 1000 / 150;
        long nextFrame = System.currentTimeMillis() + milsPerFrame;
        int nextSecond = LocalTime.now().getSecond() + 1;
        int inc = 0;
        int inc2 = 0;
        while(runGame) {
            //Print every 120th of a second
            currFrame = System.currentTimeMillis();
            if (currFrame >= nextFrame) {
                nextFrame = currFrame + milsPerFrame;
                moveProjectiles();
                getMovementRequests();
                animatePlayers();
                approveMovement();
                //update screen
                window.repaint();
                //update sprites
                inc2++;
            }
            //FPS checker
            if (LocalTime.now().getSecond() >= nextSecond) {
                nextSecond = LocalTime.now().getSecond() + 1;
                System.out.println(inc + " max FPS, " + inc2 + " real FPS, " + objects.size() + " objects");
                inc = 0;
                inc2= 0;

                if (drawingsLayer == null) {
                    drawingsLayer = window.drawingsLayer;
                }

            }
            inc++;
            //input is checked automatically by the window

        }
        System.out.println("Game over! Not a clue who won, figure it out.");
    }
    private static void animatePlayers() {
        for(Player player: players) {
            if(player.wantsToMove()) {
                if (System.currentTimeMillis() >= player.timeOfLastFrame + Player.FRAME_LENGTH) {
                    if (player.getFrame()+1 >= Player.BODY_FRAMES) {
                        player.setFrame(1);
                    } else {
                        player.nextFrame();
                    }
                }
            }
            if (System.currentTimeMillis() >= player.timeLastReload + Player.RELOAD_TIME) {
                if (player.backpack.getFrame()+1 < Player.BACKPACK_FRAMES) {
                    player.backpack.nextFrame();
                    player.timeLastReload = System.currentTimeMillis();
                }
            }
        }
    }
    private static void moveProjectiles() {
        for (Projectile projectile: projectiles) {
            projectile.requestMovementUpdate();
            projectile.acceptMoveRequest();
        }
    }
    private static void approveMovement() {
        List<Object> movingObjects = new ArrayList<>();
        for (Object object: objects) {
            if(object.wantsToMove()) {
                movingObjects.add(object);
            }
        }

        if(drawingsLayer != null) {
            // Geometry starts from the bottom center
            for(Player player: players) {
                // Check if anyone is hit
                for (int y = player.getCornerPoint().y+player.height- player.geometry.y + 1; y < player.getCornerPoint().y + player.height; y++) {
                    for (int x = player.getCornerPoint().x+(player.width/2)-(player.geometry.x/2); x < player.getCornerPoint().x + (player.width/2) + (player.geometry.x/2); x++) {
                        Color color = new Color(drawingsLayer.getRGB(x, y), false);
                        if (color.equals(Color.red)) {
                            runGame = false;
                            player.kill();
                        }
                    }
                }
            }
        }

        // Look at each moving object, not including projectiles
        for (Object movingObject: movingObjects) {
            boolean canMove = true;
            // Compare its geometry with non-mobile objects
            if (!(movingObject instanceof Projectile)) {
                for (Object object : objects) {
                    // Make sure this object is not being compared to itself
                    // Make sure it's not a bullet
                    if (!object.equals(movingObject)) {
                        // check if this other object is in the proposed radius
    //                    if (distanceBetweenTwoCircles(movingObject.getRequestedPoint(),
    //                            object.getCenterPoint(),movingObject.width/2, object.width/2) < 0) {
    //                        canMove = false;
    //                    }


                        if (rectanglesOverlap(movingObject.getRequestedPoint(),
                                new Point((movingObject).geometry.x, (movingObject).geometry.y),
                                object.getRequestedPoint(), new Point(object.geometry.x, object.geometry.y))) {
                            canMove = false;
                        }
                    }
                }
                // Compare its geometry to the lines on the drawingLayer
                Point requestedCorner = new Point(movingObject.getCornerPoint().x + movingObject.getRequestedChange().x,
                    movingObject.getCornerPoint().y + movingObject.getRequestedChange().y);
                if (drawingsLayer != null) {
                    Object player = movingObject;
                    for (int y = player.getRequestedPoint().y+(player.height/2)- player.geometry.y; y < player.getRequestedPoint().y + (player.height/2); y++) {
                        for (int x = player.getRequestedPoint().x-(player.geometry.x/2); x < player.getRequestedPoint().x + (player.geometry.x/2); x++) {
                            Color color = new Color (drawingsLayer.getRGB(x, y), true);
                            if (color.equals(Color.red) || color.equals(Color.BLACK)) {
                                canMove = false;
                                break;
                            }
                        }
                        if (!canMove) {
                            break;
                        }
                    }
                }
            }

            if(canMove) {
                movingObject.acceptMoveRequest();
            } else {
                movingObject.resetRequest();
            }
        }
    }
    private static void getMovementRequests() {
        getPlayerMovementRequests();
    }
    private static void getPlayerMovementRequests() {
        int x = 0; int y = 0;
        // Player 1 (WASD keys)
        if (keys[KeyEvent.VK_W]) {
            y = -step;  // Move up
        }
        if (keys[KeyEvent.VK_S]) {
            y = step; // Move down
        }
        if (keys[KeyEvent.VK_A]) {
            x = -step;  // Move left
        }
        if (keys[KeyEvent.VK_D]) {
            x = step;  // Move right
        }
        player1.requestMoveX(x);
        player1.requestMoveY(y);
        // The player must record its facing direction before any shots are fired
        player1.setFacing(x,y);
        if (keys[KeyEvent.VK_SPACE]) {
            Projectile shot = player1.attemptToFireLaser();
            if (shot != null) {
                projectiles.add(shot);
                objects.add(shot);
            }
        }
        if (keys[KeyEvent.VK_SHIFT]) {
            player1.attemptToPlaceBomb();
        }
        x = 0; y = 0;
        // Player 2 (Arrow keys)
        if (keys[KeyEvent.VK_NUMPAD8]) {
            y = (-step);  // Move up
        }
        if (keys[KeyEvent.VK_NUMPAD5]) {
            y = (step);  // Move down
        }
        if (keys[KeyEvent.VK_NUMPAD4]) {
            x = (-step); // Move left
        }
        if (keys[KeyEvent.VK_NUMPAD6]) {
            x = (step);  // Move right
        }
        player2.requestMoveX(x);
        player2.requestMoveY(y);
        // The player must record its facing direction before any shots are fired
        player2.setFacing(x,y);
        if (keys[KeyEvent.VK_NUMPAD0]) {
            Projectile shot = player2.attemptToFireLaser();
            if (shot != null) {
                projectiles.add(shot);
                objects.add(shot);
            }
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            player2.attemptToPlaceBomb();
        }
    }
    /**
     * Subtracts the radius from both objects
     * @return
     */
    private static int distanceBetweenTwoCircles(Point point1, Point point2, int radius1, int radius2) {
        return (int) (Math.sqrt(Math.pow(point1.x - point2.x,2) + Math.pow(point1.y - point2.y,2))
                - (radius1) - (radius2));
    }

    /**
     * Takes the top-left and bottom-right corners of two rectangles
     * @return
     */
    private static boolean rectanglesOverlap(Point centerA, Point widthHeightA, Point centerB, Point widthHeightB) {
        int avgWidth = (widthHeightA.x + widthHeightB.x)/2;
        int avgHeight = (widthHeightA.y + widthHeightB.y)/2;
        if (Math.abs(centerA.x - centerB.x) < avgWidth) {
            return (Math.abs(centerA.y - centerB.y) < avgHeight);
        }
        return false;
        //Using center points, if the vertical distance is within the average height
        //and the horizontal distance is within the average width, they overlap
    }
    private static boolean pointInRectangle(Point point, Point topLeft, Point bottomRight) {
        if (topLeft.x < point.x && point.x < bottomRight.x) {
            return topLeft.y < point.y && point.y < bottomRight.y;
        }
        return false;
    }
}