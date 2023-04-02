package model;

/*
Class representing walls in the game. Denote a wall by the TOP LEFT CORNER coordinate (x,y),
as done with JLabels.The coordinate at x & x + width & y & y + height are the
four walls of left, right, top and bottom for this wall object, respectively.
It is possible to check if an object is colliding with a wall.

For velocities of objects, the positive direction is the direction as x and y increase.
LAST REVISED: 03/31/2022
 */

import org.json.JSONObject;

import java.awt.*;
import java.util.List;

public class Wall extends TankGameObject {
    public static final Color COLOR = Color.LIGHT_GRAY;
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    private final int leftEdgeX;
    private final int rightEdgeX;
    private final int topEdgeY;
    private final int bottomEdgeY;

    //REQUIRES: centre coordinates need to fulfill: x = n * WIDTH,
    // y = m * HEIGHT, where n and m are integers
    //EFFECTS: creates a new wall with LEFT TOP CORNER at given coordinate
    public Wall(int posX, int posY) {
        super(posX, posY);
        checkIfValidWallCoordinates(posX, posY);
        this.leftEdgeX = getX();
        this.rightEdgeX = getX() + WIDTH - 1;
        this.bottomEdgeY = getY() + HEIGHT - 1;
        this.topEdgeY = getY();
    }

    //EFFECTS: returns true if moving by speed leads to the object being inside the wall
    public boolean isCollidingWithWall(int velX, int velY, TankGameObject tankGameObject) {
        int posXDestination = tankGameObject.getX() + velX;
        int posYDestination = tankGameObject.getY() + velY;

        //object's left end to the left of wall's right end
        return (((posXDestination <= this.rightEdgeX)
                //object's right end to the right of wall's left end
                && (posXDestination + tankGameObject.getWidth() >= this.leftEdgeX))
                //object's top end above the wall's bottom end
                && ((posYDestination <= this.bottomEdgeY)
                //object's bottom end below the wall's top end
                && (posYDestination + tankGameObject.getHeight() >= this.topEdgeY)));
    }

    //EFFECTS: returns true if moving the bullet by its velocity move it inside a wall
    public boolean isCollidingWithWall(Bullet bullet) {
        return isCollidingWithWall(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //REQUIRES: object must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the object colliding with the left wall
    public boolean isCollidingWithLeftWall(int velX, int velY, TankGameObject tankGameObject) {
        checkIfObjectNotOnTopOfWall(tankGameObject);
        if (isCollidingWithWall(velX, velY, tankGameObject)
                //right edge of object is originally to the left of the wall's left edge
                && (tankGameObject.getRightEdgeX() <= leftEdgeX)) {
            //if the object is colliding to the left wall with a Y position between top and bottom of wall
            if ((tankGameObject.getTopEdgeY() >= topEdgeY) && (tankGameObject.getBottomEdgeY() <= bottomEdgeY)) {
                return true;
                // if object may be colliding from top left to the left wall
            } else if (tankGameObject.getTopEdgeY() < topEdgeY) {
                return ((double) (topEdgeY - tankGameObject.getBottomEdgeY()) / velY
                        <= (double) (leftEdgeX - tankGameObject.getRightEdgeX()) / velX);
                // if object may be colliding from bottom left to the left wall
            } else { // if (tankGameObject.getBottomEdgeY() > bottomEdgeY)
                return ((double) (bottomEdgeY - tankGameObject.getTopEdgeY()) / velY
                        <= (double) (leftEdgeX - tankGameObject.getRightEdgeX()) / velX);
            }
        } else {
            return false;
        }
    }

    //REQUIRES: bullet must not be originally colliding with the wall
    //EFFECTS: returns true if moving by its velocity leads to the bullet colliding with the left wall
    public boolean isCollidingWithLeftWall(Bullet bullet) {
        return isCollidingWithLeftWall(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //REQUIRES: object must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the object colliding with the right wall
    public boolean isCollidingWithRightWall(int velX, int velY, TankGameObject tankGameObject) {
        checkIfObjectNotOnTopOfWall(tankGameObject);
        if (isCollidingWithWall(velX, velY, tankGameObject)
                //left edge of object is originally to the right of the wall's right edge
                && (tankGameObject.getLeftEdgeX() >= rightEdgeX)) {
            //if the object is colliding to the right wall with a Y position between top and bottom of wall
            if ((tankGameObject.getTopEdgeY() >= topEdgeY) && (tankGameObject.getBottomEdgeY() <= bottomEdgeY)) {
                return true;
                // if object may be colliding from top right to the right wall
            } else if (tankGameObject.getTopEdgeY() < topEdgeY) {
                return ((double) (topEdgeY - tankGameObject.getBottomEdgeY()) / velY
                        <= ((double) rightEdgeX - tankGameObject.getLeftEdgeX()) / velX);
                // if object may be colliding from bottom right to the right wall
            } else { // if (tankGameObject.getBottomEdgeY() > bottomEdgeY)
                return ((double) (bottomEdgeY - tankGameObject.getTopEdgeY()) / velY
                        <= (double) (rightEdgeX - tankGameObject.getLeftEdgeX()) / velX);
            }
        } else {
            return false;
        }
    }

    //REQUIRES: bullet must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the bullet colliding with the right wall
    public boolean isCollidingWithRightWall(Bullet bullet) {
        return isCollidingWithRightWall(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //REQUIRES: object must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the object colliding with the top wall
    public boolean isCollidingWithTopWall(int velX, int velY, TankGameObject tankGameObject) {
        checkIfObjectNotOnTopOfWall(tankGameObject);
        if (isCollidingWithWall(velX, velY, tankGameObject)
                //bottom edge of object is originally above the wall's top edge
                && (tankGameObject.getBottomEdgeY() <= topEdgeY)) {
            //if the object is colliding to the top wall with an X position between left and right of wall
            if ((tankGameObject.getLeftEdgeX() >= leftEdgeX) && (tankGameObject.getRightEdgeX() <= rightEdgeX)) {
                return true;
                // if object may be colliding from top left to the top wall
            } else if (tankGameObject.getRightEdgeX() < leftEdgeX) {
                return ((double) (topEdgeY - tankGameObject.getBottomEdgeY()) / velY
                        >= (double) (leftEdgeX - tankGameObject.getRightEdgeX()) / velX);
                // if object may be colliding from top right to the top wall
            } else { // if (tankGameObject.getLeftEdgeX() > rightEdgeX)
                return ((double) (topEdgeY - tankGameObject.getBottomEdgeY()) / velY
                        >= (double) (rightEdgeX - tankGameObject.getLeftEdgeX()) / velX);
            }
        } else {
            return false;
        }
    }

    //REQUIRES: bullet must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the bullet colliding with the top wall
    public boolean isCollidingWithTopWall(Bullet bullet) {
        return isCollidingWithTopWall(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //REQUIRES: object must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the object colliding with the bottom wall
    public boolean isCollidingWithBottomWall(int velX, int velY, TankGameObject tankGameObject) {
        checkIfObjectNotOnTopOfWall(tankGameObject);
        if (isCollidingWithWall(velX, velY, tankGameObject)
                //top edge of object is originally below the wall's bottom edge
                && (tankGameObject.getTopEdgeY() >= bottomEdgeY)) {
            //if the object is colliding to the bottom wall with an X position between left and right of wall
            if ((tankGameObject.getLeftEdgeX() >= leftEdgeX) && (tankGameObject.getRightEdgeX() <= rightEdgeX)) {
                return true;
                // if object may be colliding from bottom left to the bottom wall
            } else if (tankGameObject.getRightEdgeX() < leftEdgeX) {
                return ((double) (bottomEdgeY - tankGameObject.getTopEdgeY()) / velY
                        >= (double) (leftEdgeX - tankGameObject.getRightEdgeX()) / velX);
                // if object may be colliding from bottom right to the bottom wall
            } else { // if (tankGameObject.getLeftEdgeX() > rightEdgeX)
                return ((double) (bottomEdgeY - tankGameObject.getTopEdgeY()) / velY
                        >= (double) (rightEdgeX - tankGameObject.getLeftEdgeX()) / velX);
            }
        } else {
            return false;
        }
    }

    //REQUIRES: bullet must not be originally colliding with the wall
    //EFFECTS: returns true if moving by speed leads to the bullet colliding with the bottom wall
    public boolean isCollidingWithBottomWall(Bullet bullet) {
        return isCollidingWithBottomWall(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //REQUIRES: given object should not be already on top of the wall
    //EFFECTS: returns the x coordinate of the object's TOP LEFT CORNER that would be pushed back if it is
    // colliding with this wall; if not colliding, return the normal position the object would be advancing to
    public int coordinateXIfObjectCollidesAndIsPushedBack(int velX, int velY, TankGameObject tankGameObject) {
        checkIfObjectNotOnTopOfWall(tankGameObject);
        if (isCollidingWithLeftWall(velX, velY, tankGameObject)) {
            return (this.leftEdgeX - tankGameObject.getWidth() - 1);
        } else if (isCollidingWithRightWall(velX, velY, tankGameObject)) {
            return this.rightEdgeX + 1;
        } else {
            return (tankGameObject.getX() + velX);
        }
    }

    //REQUIRES: bullet should not be already on top of the wall
    //EFFECTS: returns the x coordinate of the bullet's TOP LEFT CORNER that would be pushed back if it is
    // colliding with this wall; if not colliding, return the normal position the bullet would be advancing to
    public int coordinateXIfObjectCollidesAndIsPushedBack(Bullet bullet) {
        return coordinateXIfObjectCollidesAndIsPushedBack(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //REQUIRES: given object should not be already on top of the wall
    //EFFECTS: returns the y coordinate of the object's TOP LEFT CORNER that would be pushed back if it is
    // colliding with this wall; if not colliding, return the normal position the object would be advancing to
    public int coordinateYIfObjectCollidesAndIsPushedBack(int velX, int velY, TankGameObject tankGameObject) {
        checkIfObjectNotOnTopOfWall(tankGameObject);
        if (isCollidingWithBottomWall(velX, velY, tankGameObject)) {
            return (this.bottomEdgeY + 1);
        } else if (isCollidingWithTopWall(velX, velY, tankGameObject)) {
            return (this.topEdgeY - tankGameObject.getHeight() - 1);
        } else {
            return (tankGameObject.getY() + velY);
        }
    }

    //REQUIRES: bullet should not be already on top of the wall
    //EFFECTS: returns the y coordinate of the bullet's TOP LEFT CORNER that would be pushed back if it is
    // colliding with this wall; if not colliding, return the position the bullet would be advancing to
    public int coordinateYIfObjectCollidesAndIsPushedBack(Bullet bullet) {
        return coordinateYIfObjectCollidesAndIsPushedBack(bullet.getVelX(), bullet.getVelY(), bullet);
    }

    //fixme change it to overriding equals and hashcode
    //EFFECTS: returns true if the given wall object has posX and posY equal to that of this wall
    public boolean hasIdenticalProperties(Wall comparedWall) {
        return (super.hasIdenticalProperties(comparedWall));
    }

    //fixme CHANGE hashCode AND equals, and then remove this method
    //EFFECTS: returns true if the given list of walls includes an object with identical posX and posY
    // to this wall object
    public boolean wallWithIdenticalPropertyContained(List<Wall> wallList) {
        boolean result = false;
        for (Wall w : wallList) {
            if (hasIdenticalProperties(w)) {
                result = true;
                break;
            }
        }
        return result;
    }

    //EFFECTS: given the x coordinate of a point, return the x coordinate of the TOP LEFT CORNER of the wall
    // which includes this coordinate; when at boundaries of walls, the wall to the right
    // arbitrary takes precedence
    public static int topLeftXOfWallBoundingThisCoordinate(int givenPosX) {
        return (givenPosX / Wall.WIDTH) * Wall.WIDTH;
    }

    //EFFECTS: given the y coordinate of a point, return the y coordinate of the TOP LEFT CORNER of the wall
    // which includes this coordinate; when at boundaries of walls, the wall above
    // arbitrary takes precedence
    public static int topLeftYOfWallBoundingThisCoordinate(int givenPosY) {
        return (givenPosY / Wall.HEIGHT) * Wall.HEIGHT;
    }

    @Override
    public JSONObject toJson() {
        return super.toJson();
    }

    @Override
    public int getHeight() {
        return Wall.HEIGHT;
    }

    @Override
    public int getWidth() {
        return Wall.WIDTH;
    }

    @Override
    public Color getColor() {
        return Wall.COLOR;
    }

    public int getBottomEdgeY() {
        return bottomEdgeY;
    }

    public int getLeftEdgeX() {
        return leftEdgeX;
    }

    public int getRightEdgeX() {
        return rightEdgeX;
    }

    public int getTopEdgeY() {
        return topEdgeY;
    }

    //EFFECTS: checks if the object is not on top of the wall
    private void checkIfObjectNotOnTopOfWall(TankGameObject tankGameObject) {
        assert (! isCollidingWithWall(0, 0, tankGameObject)) : "Object on top of wall.";
    }

    //EFFECTS: check if the coordinate of the newly created wall is valid
    private void checkIfValidWallCoordinates(int posX, int posY) {
        assert ((posX % WIDTH) == 0 && (posY % HEIGHT) == 0) : "Wall instantiated with invalid coordinates.";
    }

}
