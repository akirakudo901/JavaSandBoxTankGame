package model.tanks;

/*
Class implementing the tank that will be controlled by the player or enemy AIs.
Has constants:
- SPEED, the speed of the tank as it moves in the screen
Has variables:
- X / Y coordinate on the screen - the TOP LEFT CORNER of the object (as with JLabels)
- Angle of gun tank which increases in clockwise direction
- A list of bullets shot by this tank
 */

import model.Bullet;
import model.TankGameObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tank extends TankGameObject {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public static final int SPEED = 1;
    public static final int ROTATE_SPEED = 2;
    public static final int GUN_WIDTH = 5;
    public static final int GUN_HEIGHT = 2;
    public static final Color COLOR = Color.WHITE;

    private int gunAngle;
    private List<Bullet> bullets;

    //REQUIRES: Wall.WIDTH < x < (TankGame.WIDTH - Wall.WIDTH)
    // Wall.HEIGHT < y < (TankGame.HEIGHT - Wall.HEIGHT)
    //EFFECTS: creates the player's tank with given default x and y position
    // and tank gun angle of 0, and without any bullets shot by this tank
    public Tank(int posX, int posY, int gunAngle) {
        super(posX, posY);
        this.gunAngle = gunAngle;
        this.bullets = new ArrayList<>();
    }

    //REQUIRES: Wall.WIDTH < x < (TankGame.WIDTH - Wall.WIDTH)
    // Wall.HEIGHT < y < (TankGame.HEIGHT - Wall.HEIGHT)
    //EFFECTS: creates the player's tank with given default x and y position
    // and tank gun angle of 0, and given bullets shot by this tank
    public Tank(int posX, int posY, int gunAngle, List<Bullet> bulletList) {
        super(posX, posY);
        this.gunAngle = gunAngle;
        this.bullets = bulletList;
    }

    //TODO REMOVE THESE MOVEUP, DOWN, RIGHT AND LEFT
    //MODIFIES: this
    //EFFECTS: moves tank up by SPEED
    public void moveUp() {
        setLocation(getX(), getY() - SPEED);
    }

    //MODIFIES: this
    //EFFECTS: moves tank down by SPEED
    public void moveDown() {
        setLocation(getX(), getY() + SPEED);
    }

    //MODIFIES: this
    //EFFECTS: moves tank right by SPEED
    public void moveRight() {
        setLocation(getX() + SPEED, getY());
    }

    //MODIFIES: this
    //EFFECTS: moves tank left by SPEED
    public void moveLeft() {
        setLocation(getX() - SPEED, getY());
    }

    //MODIFIES: this
    //EFFECTS: rotate the tank gun anti-clockwise by ROTATE angle
    public void rotateGunAntiClockWise() {
        this.gunAngle -= ROTATE_SPEED;
        if (gunAngle < 0) {
            this.gunAngle += 360;
        }
    }

    //MODIFIES: this
    //EFFECTS: rotate the tank gun clockwise by ROTATE angle
    public void rotateGunClockWise() {
        this.gunAngle += ROTATE_SPEED;
        if (gunAngle >= 360) {
            this.gunAngle -= 360;
        }
    }

    //MODIFIES: this
    //EFFECTS: adds the given bullet to the tank's bullet list
    public void addBullet(Bullet newBullet) {
        this.bullets.add(newBullet);
    }

    //THIS SEEMS TO BE A SIMPLE ENOUGH SETTER TO NOT HAVE TESTS
    //REQUIRES: given angle between 0 and 359
    //MODIFIES: this
    //EFFECTS: set the tank gun angle to the given value
    public void setGunAngle(int newGunAngle) {
        this.gunAngle = newGunAngle;
    }

    //EFFECTS: returns true if the compared tank has identical x and y position and
    // gun angle as this tank
    public boolean hasIdenticalProperties(Tank comparedTank) {
        boolean haveIdenticalBulletList = true;
        if (this.bullets.size() == comparedTank.getBullets().size()) {
            for (int i = 0; i < this.bullets.size(); i++) {
                if (! this.bullets.get(i).hasIdenticalProperties(comparedTank.getBullets().get(i))) {
                    haveIdenticalBulletList = false;
                    break;
                }
            }
        } else {
            return false;
        }
        return ((super.hasIdenticalProperties(comparedTank))
                && (this.gunAngle == comparedTank.getGunAngle())
                && haveIdenticalBulletList);
    }

    @Override
    //EFFECTS: returns a JSON Object equivalent to this tank object
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("gunAngle", this.gunAngle);
        jsonObject.put("bullets", bulletsToJson());
        return jsonObject;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public int getGunAngle() {
        return this.gunAngle;
    }

    @Override
    public int getHeight() {
        return Tank.HEIGHT;
    }

    @Override
    public int getWidth() {
        return Tank.WIDTH;
    }

    @Override
    public Color getColor() {
        return Tank.COLOR;
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

    //EFFECTS: returns a JSON Array equivalent to the bullets array of this tank
    private JSONArray bulletsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Bullet b : this.bullets) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

}
