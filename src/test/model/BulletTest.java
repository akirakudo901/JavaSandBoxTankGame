package model;

//LAST REVISED: 03/31/2022

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BulletTest {
    Bullet testBullet;

    @BeforeEach
    void runBeforeEach() {
        testBullet = new Bullet(10,20, 10,20);
    }

    @Test
    void testConstructor() {
        //check
        assertEquals(10, testBullet.getX());
        assertEquals(20, testBullet.getY());
        assertEquals(10, testBullet.getVelX());
        assertEquals(20, testBullet.getVelY());
        assertEquals(0, testBullet.getBounceCount());
    }


    @Test
    void testMoveBullet() {
        //setup
        testBullet.setLocation(30, 40);
        testBullet.setBulletVelocity(-20, 4);
        //execute
        testBullet.moveBullet();
        //check
        assertEquals(30 - 20, testBullet.getX());
        assertEquals(40 + 4, testBullet.getY());
    }

    @Test
    void testIncreaseBounceCountUsedOnce() {
        //execute
        testBullet.increaseBounceCount();
        //check
        assertEquals(1, testBullet.getBounceCount());
    }

    @Test
    void testIncreaseBounceCountUsedThreeTimes() {
        //execute
        testBullet.increaseBounceCount();
        testBullet.increaseBounceCount();
        testBullet.increaseBounceCount();
        //check
        assertEquals(3, testBullet.getBounceCount());
    }

    //toJson
    @Test
    void testToJson() {
        //execute
        JSONObject jsonObject = testBullet.toJson();
        //check
        assertEquals(testBullet.getX(), jsonObject.get("posX"));
        assertEquals(testBullet.getY(), jsonObject.get("posY"));
        assertEquals(testBullet.getVelX(), jsonObject.get("velX"));
        assertEquals(testBullet.getVelY(), jsonObject.get("velY"));
        assertEquals(testBullet.getBounceCount(), jsonObject.get("bounceCount"));
    }

    //hasIdenticalProperties
    @Test
    void testHasIdenticalPropertiesIdenticalBullets() {
        //setup
        Bullet comparedBullet = new Bullet(testBullet.getX(), testBullet.getY(),
                testBullet.getVelX(), testBullet.getVelY());
        //execute & check
        assertTrue(comparedBullet.hasIdenticalProperties(testBullet));
    }

    @Test
    void testHasIdenticalPropertiesBulletsWithOnlyPositionsAndVelXIdentical() {
        //setup
        Bullet comparedBullet = new Bullet(testBullet.getX(), testBullet.getY(),
                testBullet.getVelX(), -testBullet.getVelY());
        //execute & check
        assertFalse(comparedBullet.hasIdenticalProperties(testBullet));
    }

    @Test
    void testHasIdenticalPropertiesBulletsWithOnlyPositionsIdentical() {
        //setup
        Bullet comparedBullet = new Bullet(testBullet.getX(), testBullet.getY(),
                -testBullet.getVelX(), -testBullet.getVelY());
        //execute & check
        assertFalse(comparedBullet.hasIdenticalProperties(testBullet));
    }

    //getHeight & getWidth
    @Test
    void testGetHeightAndGetWidth() {
        //execute
        assertEquals(Bullet.HEIGHT, testBullet.getHeight());
        assertEquals(Bullet.WIDTH, testBullet.getWidth());
    }

}
