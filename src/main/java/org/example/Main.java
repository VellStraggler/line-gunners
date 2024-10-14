package org.example;

import org.example.Objects.Object;
import org.example.Objects.Sprite;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Sprite player1 = new Sprite("lineguy-armless.png", new Point(500, 500));
    public static Sprite player2 = new Sprite("lineguy.png", new Point(1000, 400));
    public static boolean[] keys = new boolean[256];

    public static int step = 1;

    public static void main(String[] args) {
        System.out.println("Beginning program...");


        // sprites
        List<Object> objects = List.of(
                player1, player2
        );

        Window window = new Window(objects);
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
                //update sprites
                objects = getMovementRequests(objects);
                objects = updateMovement(objects);
                //update screen
                window.repaint();
                inc2++;
            }
            //FPS checker
            if (LocalTime.now().getSecond() >= nextSecond) {
                nextSecond = LocalTime.now().getSecond() + 1;
                System.out.println(inc + " max FPS, " + inc2 + " real FPS");
                inc = 0;
                inc2= 0;
            }
            inc++;
            //input is checked automatically by the window

        }
    }
    private static List<Object> updateMovement(List<Object> objects) {
        List<Object> movingObjects = new ArrayList<>();
        for (Object object: objects) {
            if(object.wantsToMove()) {
                movingObjects.add(object);
            }
        }
        for (Object movingObject: movingObjects) {
            boolean canMove = true;
            for(Object object: objects) {
                if (!object.equals(movingObject)) {
                    // check if this other object is in the proposed radius
//                    if (distanceBetweenTwoCircles(movingObject.getRequestedPoint(),
//                            object.getCenterPoint(),movingObject.width/2, object.width/2) < 0) {
//                        canMove = false;
//                    }
                    if (rectanglesOverlap(movingObject.getRequestedPoint(),
                            new Point(movingObject.width, movingObject.height),
                            object.getRequestedPoint(), new Point(object.width, object.height))) {
                        canMove = false;
                    }
                }
            }
            if(canMove) {
                movingObject.moveTo(movingObject.getRequestedPoint());
            } else {
                movingObject.resetRequest();
            }
        }
        return objects;
    }
    private static List<Object> getMovementRequests(List<Object> objects) {
        getPlayerMovementRequests();
        return objects;
    }
    private static void getPlayerMovementRequests() {
        // Player 1 (WASD keys)
        if (keys[KeyEvent.VK_W]) {
            player1.requestMoveY(-step);  // Move up
        }
        if (keys[KeyEvent.VK_S]) {
            player1.requestMoveY(step); // Move down
        }
        if (keys[KeyEvent.VK_A]) {
            player1.requestMoveX(-step);  // Move left
        }
        if (keys[KeyEvent.VK_D]) {
            player1.requestMoveX(step);  // Move right
        }

        // Player 2 (Arrow keys)
        if (keys[KeyEvent.VK_UP]) {
            player2.requestMoveY(-step);  // Move up
        }
        if (keys[KeyEvent.VK_DOWN]) {
            player2.requestMoveY(step);  // Move down
        }
        if (keys[KeyEvent.VK_LEFT]) {
            player2.requestMoveX(-step); // Move left
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            player2.requestMoveX(step);  // Move right
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