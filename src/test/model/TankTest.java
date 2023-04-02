package model;

//LAST REVISED: 03/31/2022

import model.tanks.Tank;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TankTest {
    Tank myTank;
    Bullet bullet1;
    Bullet bullet2;
    List<Bullet> bulletList;
    int SPEED = Tank.SPEED;
    int ROTATE_SPEED = Tank.ROTATE_SPEED;

    private final int MT_POS_X = 50 + SPEED;
    private final int MT_POS_Y = 50 + SPEED;
    private final int MT_GA = 50 + ROTATE_SPEED;

    @BeforeEach
    void runBeforeEach() {
        myTank = new Tank(MT_POS_X,MT_POS_Y,MT_GA);
        bullet1 = new Bullet(30, 40, 50, 60);
        bullet2 = new Bullet(70, 30, -8, 6);
        bulletList = new ArrayList<>();
        bulletList.add(bullet1);
        bulletList.add(bullet2);
    }

    @Test
    void testConstructor() {
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals(MT_GA, myTank.getGunAngle());
        assertEquals(new ArrayList<Bullet>(), myTank.getBullets());
    }

    @Test
    void testConstructorWithBulletList() {
        //execute
        myTank = new Tank(MT_POS_X,MT_POS_Y,MT_GA, bulletList);
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals(MT_GA, myTank.getGunAngle());
        assertEquals(2, myTank.getBullets().size());
        assertTrue(myTank.getBullets().contains(bullet1));
        assertTrue(myTank.getBullets().contains(bullet2));
    }

    @Test
    void testMoveUp() {
        //execute
        myTank.moveUp();
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y - SPEED, myTank.getY());
        assertEquals(MT_GA, myTank.getGunAngle());
    }

    @Test
    void testMoveDown() {
        //execute
        myTank.moveDown();
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y + SPEED, myTank.getY());
        assertEquals(MT_GA, myTank.getGunAngle());
    }

    @Test
    void testMoveRight() {
        //execute
        myTank.moveRight();
        //check
        assertEquals(MT_POS_X + SPEED, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals(MT_GA, myTank.getGunAngle());
    }

    @Test
    void testMoveLeft() {
        //execute
        myTank.moveLeft();
        //check
        assertEquals(MT_POS_X - SPEED, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals(MT_GA, myTank.getGunAngle());
    }

    @Test
    void testRotateGunClockWiseNotAtBoundary() {
        //execute
        myTank.rotateGunClockWise();
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals(MT_GA + ROTATE_SPEED, myTank.getGunAngle());
    }

    @Test
    void testRotateGunClockWiseAcrossBoundary() {
        //check behavior when angle exceeds 360 degree
        //setup
        myTank.setGunAngle(359);
        //execute
        myTank.rotateGunClockWise();
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals((359 + ROTATE_SPEED) % 360, myTank.getGunAngle());
    }

    @Test
    void testRotateGunAntiClockWiseNotAtBoundary() {
        //execute
        myTank.rotateGunAntiClockWise();
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals(MT_GA - ROTATE_SPEED, myTank.getGunAngle());
    }

    @Test
    void testRotateGunAntiClockWiseAcrossBoundary() {
        //check behavior when angle goes below 0 degree
        //setup
        myTank.setGunAngle(0);
        //execute
        myTank.rotateGunAntiClockWise();
        //check
        assertEquals(MT_POS_X, myTank.getX());
        assertEquals(MT_POS_Y, myTank.getY());
        assertEquals((360 - ROTATE_SPEED) % 360, myTank.getGunAngle());
    }

    @Test
    void testMoveUpDownRightLeftUsedConsecutively() {
        //setup
        myTank.setLocation(150, 150);
        //execute
        myTank.moveLeft();
        myTank.moveUp();
        myTank.moveUp();
        myTank.moveRight();
        myTank.moveRight();
        myTank.moveUp();
        myTank.moveDown();
        myTank.moveDown();
        myTank.moveLeft();
        //check
        assertEquals(150, myTank.getX());
        assertEquals(150 - SPEED, myTank.getY());
    }

    @Test
    void testAddBulletAddOneBullet() {
        //execute
        myTank.addBullet(bullet1);
        //check
        assertEquals(1, myTank.getBullets().size());
        assertEquals(bullet1, myTank.getBullets().get(0));
    }

    @Test
    void testAddBulletAddTwoBullets() {
        //execute
        myTank.addBullet(bullet1);
        myTank.addBullet(bullet2);
        //check
        assertEquals(2, myTank.getBullets().size());
        assertEquals(bullet1, myTank.getBullets().get(0));
        assertEquals(bullet2, myTank.getBullets().get(1));
    }

    @Test
    void testHasIdenticalPropertiesTanksAreIdentical() {
        //setup
        Tank comparedTank = new Tank(MT_POS_X,MT_POS_Y,MT_GA);
        //execute & check
        assertTrue(myTank.hasIdenticalProperties(comparedTank));
    }

    @Test
    void testHasIdenticalPropertiesOnlyPosxAndPosyAndGunAngleAndBulletListSize() {
        //setup
        List<Bullet> bulletList2 = new ArrayList<>();
        bulletList2.add(bullet1);
        bulletList2.add(bullet1);
        Tank myTank = new Tank(MT_POS_X, MT_POS_Y,MT_GA + 10, bulletList);
        Tank comparedTank = new Tank(MT_POS_X, MT_POS_Y,MT_GA + 10, bulletList2);
        //execute & check
        assertFalse(myTank.hasIdenticalProperties(comparedTank));
    }

    @Test
    void testHasIdenticalPropertiesOnlyPosxAndPosyAndGunAngleAreIdentical() {
        //setup
        Tank comparedTank = new Tank(MT_POS_X, MT_POS_Y,MT_GA + 10, bulletList);
        //execute & check
        assertFalse(myTank.hasIdenticalProperties(comparedTank));
    }

    @Test
    void testHasIdenticalPropertiesOnlyPosxAndPosyAreIdentical() {
        //setup
        Tank comparedTank = new Tank(MT_POS_X, MT_POS_Y,MT_GA + 10);
        //execute & check
        assertFalse(myTank.hasIdenticalProperties(comparedTank));
    }

    @Test
    void testHasIdenticalPropertiesOnlyPosxIsIdentical() {
        //setup
        Tank comparedTank = new Tank(50 + SPEED,999,0);
        //execute & check
        assertFalse(myTank.hasIdenticalProperties(comparedTank));
    }

    @Test
    void testHasIdenticalPropertiesTanksAreNotIdentical() {
        //setup
        Tank comparedTank = new Tank(999,999,0);
        //execute & check
        assertFalse(myTank.hasIdenticalProperties(comparedTank));
    }

    @Test
    void testToJson() {
        //execute
        JSONObject jsonObject = myTank.toJson();
        JSONArray bulletsArray = (JSONArray) jsonObject.get("bullets");
        //check
        assertEquals(myTank.getX(), jsonObject.get("posX"));
        assertEquals(myTank.getY(), jsonObject.get("posY"));
        assertEquals(myTank.getGunAngle(), jsonObject.get("gunAngle"));
        assertEquals(0, bulletsArray.length());
    }

    @Test
    void testGetHeightAndGetWidth() {
        //execute
        assertEquals(Tank.HEIGHT, myTank.getHeight());
        assertEquals(Tank.WIDTH, myTank.getWidth());
    }

}
