package model.tanks;

import model.Bullet;

import java.awt.*;
import java.util.List;

/*
Class that implements a "player tank" object. Specifies this object's color.
All its parameters and constructor do the same as those from the "Tank" class.
 */

public class PlayerTank extends Tank {
    public static final Color COLOR = Color.blue;

    //EFFECTS: creates a new instance of a player tank with given X / Y position, and with
    // given gun angle and an empty list of bullets.
    public PlayerTank(int posX, int posY, int gunAngle) {
        super(posX, posY, gunAngle);
    }

    //EFFECTS: creates a new instance of a player tank with given X / Y position, and with
    // given gun angle and list of bullets.
    public PlayerTank(int posX, int posY, int gunAngle, List<Bullet> bullets) {
        super(posX, posY, gunAngle, bullets);
    }

    @Override
    public Color getColor() {
        return PlayerTank.COLOR;
    }
}
