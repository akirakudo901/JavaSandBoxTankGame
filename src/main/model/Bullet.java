package model;

/*
Class representing a bullet object fired from the tanks.
Can be moved around with changing velocities in the x / y directions, and denoted with
its x / y positions at its TOP LEFT CORNER (as done with JLabels).
Parameters:
- WIDTH = width of the general bullet object
- HEIGHT = height of the general bullet object
- velX = velocity to the x direction of the bullet
- velY = velocity to the y direction of the bullet
- bounceCount = count of how many times this bullet has bounced against walls.
 Once this reaches a certain number, the bullet will be removed from the TankGame.

For velocities of objects, the positive direction is the direction as x and y increase.

LAST REVISED: 03/31/2022
 */

import org.json.JSONObject;

import java.awt.*;

public class Bullet extends TankGameObject {
    public static final Color COLOR = Color.black;
    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;
    private int velX;
    private int velY;
    private int bounceCount;

    //EFFECTS: creates a bullet at given coordinates with given velocity and bounce count 0
    public Bullet(int posX, int posY, int velX, int velY) {
        super(posX, posY);
        this.velX = velX;
        this.velY = velY;
        this.bounceCount = 0;
    }

    //MODIFIES: this
    //EFFECTS: moves the bullet by the bullet's velocity
    public void moveBullet() {
        setLocation(getX() + this.velX, getY() + this.velY);
    }

    //SETTER SEEMING SIMPLE ENOUGH TO NOT NEED ANY TESTS
    //MODIFIES: this
    //EFFECTS: sets the bullet's velocity to the given one
    public void setBulletVelocity(int velX, int velY) {
        this.velX = velX;
        this.velY = velY;
    }

    //MODIFIES: this
    //EFFECTS: increases the bounce count against walls by 1
    public void increaseBounceCount() {
        this.bounceCount++;
    }

    @Override
    //EFFECTS: returns a JSON object equivalent to this bullet
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("velX", this.velX);
        jsonObject.put("velY", this.velY);
        jsonObject.put("bounceCount", this.bounceCount);
        return jsonObject;
    }

    //EFFECTS: returns true if the given bullet and this bullet have the same parameters
    public boolean hasIdenticalProperties(Bullet comparedBullet) {
        return (super.hasIdenticalProperties(comparedBullet)
                && (this.getVelX() == comparedBullet.getVelX())
                && (this.getVelY() == comparedBullet.getVelY()));
    }

    public int getVelX() {
        return this.velX;
    }

    public int getVelY() {
        return this.velY;
    }

    public int getBounceCount() {
        return this.bounceCount;
    }

    @Override
    public int getTopEdgeY() {
        return getY();
    }

    @Override
    public int getBottomEdgeY() {
        return getY() + HEIGHT;
    }

    @Override
    public int getLeftEdgeX() {
        return getX();
    }

    @Override
    public int getRightEdgeX() {
        return getX() + WIDTH;
    }

    @Override
    public int getHeight() {
        return Bullet.HEIGHT;
    }

    @Override
    public int getWidth() {
        return Bullet.WIDTH;
    }

    @Override
    public Color getColor() {
        return Bullet.COLOR;
    }

}
