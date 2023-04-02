package model;

//LAST REVISED: 03/31/2022

import model.tanks.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TankGameObjectTest {
    private Tank testTank;
    private Bullet testBullet;
    private Wall testWall;
    private Bullet bulletForTest;

    @BeforeEach
    void runBeforeEach() {
        testTank = new Tank(40, 100, 32);
        testBullet = new Bullet(34, 11, 8, -10);
        testWall = new Wall(Wall.WIDTH * 3, Wall.HEIGHT * 3);
    }

    @Test
    void testIsCollidingWithTrueAtRightBoundary() {
        //test for each type of TankGameObject
        testSuccessfulCollisionForRightBoundary(testTank);
        testSuccessfulCollisionForRightBoundary(testBullet);
        testSuccessfulCollisionForRightBoundary(testWall);
    }

    @Test
    void testIsCollidingWithFalseAtRightBoundary() {
        //test for each type of TankGameObject
        testFailedCollisionForRightBoundary(testTank);
        testFailedCollisionForRightBoundary(testBullet);
        testFailedCollisionForRightBoundary(testWall);
    }

    @Test
    void testIsCollidingWithTrueAtLeftBoundary() {
        //test for each type of TankGameObject
        testSuccessfulCollisionForLeftBoundary(testTank);
        testSuccessfulCollisionForLeftBoundary(testBullet);
        testSuccessfulCollisionForLeftBoundary(testWall);
    }

    @Test
    void testIsCollidingWithFalseAtLeftBoundary() {
        //test for each type of TankGameObject
        testFailedCollisionForLeftBoundary(testTank);
        testFailedCollisionForLeftBoundary(testBullet);
        testFailedCollisionForLeftBoundary(testWall);
    }

    @Test
    void testIsCollidingWithTrueAtTopBoundary() {
        //test for each type of TankGameObject
        testSuccessfulCollisionForTopBoundary(testTank);
        testSuccessfulCollisionForTopBoundary(testBullet);
        testSuccessfulCollisionForTopBoundary(testWall);
    }

    @Test
    void testIsCollidingWithFalseAtTopBoundary() {
        //test for each type of TankGameObject
        testFailedCollisionForTopBoundary(testTank);
        testFailedCollisionForTopBoundary(testBullet);
        testFailedCollisionForTopBoundary(testWall);
    }

    @Test
    void testIsCollidingWithTrueAtBottomBoundary() {
        //test for each type of TankGameObject
        testSuccessfulCollisionForBottomBoundary(testTank);
        testSuccessfulCollisionForBottomBoundary(testBullet);
        testSuccessfulCollisionForBottomBoundary(testWall);
    }

    @Test
    void testIsCollidingWithFalseAtBottomBoundary() {
        //test for each type of TankGameObject
        testFailedCollisionForBottomBoundary(testTank);
        testFailedCollisionForBottomBoundary(testBullet);
        testFailedCollisionForBottomBoundary(testWall);
    }

    //test whether collision was successful at right boundary
    private void testSuccessfulCollisionForRightBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getRightEdgeX(), tankGameObject.getBottomEdgeY(),
                0, 0);
        assertTrue(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was a failure at right boundary
    private void testFailedCollisionForRightBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getRightEdgeX() + 1, tankGameObject.getBottomEdgeY(),
                0, 0);
        assertFalse(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was successful at left boundary
    private void testSuccessfulCollisionForLeftBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getLeftEdgeX() - Bullet.WIDTH,
                tankGameObject.getBottomEdgeY(), 0, 0);
        assertTrue(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was a failure at left boundary
    private void testFailedCollisionForLeftBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getLeftEdgeX() - Bullet.WIDTH - 1,
                tankGameObject.getBottomEdgeY(), 0, 0);
        assertFalse(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was successful at top boundary
    private void testSuccessfulCollisionForTopBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getRightEdgeX(),
                tankGameObject.getTopEdgeY() - Bullet.HEIGHT, 0, 0);
        assertTrue(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was a failure at top boundary
    private void testFailedCollisionForTopBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getRightEdgeX(),
                tankGameObject.getTopEdgeY() - Bullet.HEIGHT - 1, 0, 0);
        assertFalse(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was successful at bottom boundary
    private void testSuccessfulCollisionForBottomBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getRightEdgeX(), tankGameObject.getBottomEdgeY(),
                0, 0);
        assertTrue(tankGameObject.isCollidingWith(bulletForTest));
    }

    //test whether collision was a failure at bottom boundary
    private void testFailedCollisionForBottomBoundary(TankGameObject tankGameObject) {
        bulletForTest = new Bullet(tankGameObject.getRightEdgeX(), tankGameObject.getBottomEdgeY() + 1,
                0, 0);
        assertFalse(tankGameObject.isCollidingWith(bulletForTest));
    }

}
