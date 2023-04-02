package model.tanks;

import model.Bullet;

import java.awt.*;
import java.util.List;

/*
Class that implements an "enemy tank" object. Specifies this object's color.
All its parameters and constructor do the same as those from the "Tank" class.
 */

public class EnemyTank extends Tank {
    public static final Color COLOR = Color.ORANGE;

    //EFFECTS: creates a new instance of an enemy tank with given X / Y position, and with
    // given gun angle and an empty list of bullets.
    public EnemyTank(int posX, int posY, int gunAngle) {
        super(posX, posY, gunAngle);
    }

    //EFFECTS: creates a new instance of an enemy tank with given X / Y position, and with
    // given gun angle and list of bullets.
    public EnemyTank(int posX, int posY, int gunAngle, List<Bullet> bullets) {
        super(posX, posY, gunAngle, bullets);
    }

    @Override
    public Color getColor() {
        return EnemyTank.COLOR;
    }

}
