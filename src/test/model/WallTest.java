package model;

//LAST REVISED: 03/31/2022

import model.tankgame.TankGame;
import model.tanks.Tank;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WallTest {
    Wall testWall;
    Wall wallInList1;
    Wall wallInList2;
    Wall wallInList3;
    Wall identicalPropertiesToTestWall;
    List<Wall> someListOfWall;
    Tank temporalTank;
    Bullet bullet;

    @BeforeEach
    void runBeforeEach() {
        testWall = new Wall(Wall.WIDTH * 3, Wall.HEIGHT);
        wallInList1 = new Wall(0, 0);
        wallInList2 = new Wall(Wall.WIDTH * 2, Wall.HEIGHT * 5);
        wallInList3 = new Wall(0, Wall.HEIGHT * 3);
        identicalPropertiesToTestWall = new Wall(Wall.WIDTH * 3, Wall.HEIGHT);
        someListOfWall = new ArrayList<>();
        someListOfWall.add(wallInList1);
        someListOfWall.add(wallInList2);
        someListOfWall.add(wallInList3);

        temporalTank = new Tank(TankGame.WIDTH / 2, TankGame.HEIGHT / 2, 0);
        bullet = new Bullet(TankGame.WIDTH / 2, TankGame.HEIGHT / 2, 0, 0);
    }

    //Constructor not tested given it being a simple setter

    //IsCollidingWithWall
    @Test
    void testIsCollidingWithWall_NotColliding() {
        //setup
        temporalTank.setLocation(Wall.WIDTH * 8, Wall.HEIGHT * 8);
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertFalse(wallInList2.isCollidingWithWall(0, Tank.SPEED, temporalTank));

        //Overloaded method (Bullet b):
        //setup
        bullet.setLocation(Wall.WIDTH * 8, Wall.HEIGHT * 8);
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(bullet);
        //execute & check - takes object as input
        assertFalse(wallInList2.isCollidingWithWall(bullet));
    }

    @Test
    void testIsCollidingWithWall_CollidingJustAtLeftBoundary() {
        //setup
        temporalTank.setLocation(wallInList2.getLeftEdgeX() - Tank.SPEED - Tank.WIDTH, wallInList2.getY());
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertTrue(wallInList2.isCollidingWithWall(Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_NotCollidingJustAtLeftBoundary() {
        //setup
        temporalTank.setLocation( (wallInList2.getLeftEdgeX() - Tank.SPEED - Tank.WIDTH) - 1, wallInList2.getY());
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertFalse(wallInList2.isCollidingWithWall(Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_CollidingJustAtRightBoundary() {
        //setup
        temporalTank.setLocation(wallInList2.getRightEdgeX() + Tank.SPEED, wallInList2.getY());
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertTrue(wallInList2.isCollidingWithWall(-Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_NotCollidingJustAtRightBoundary() {
        //setup
        temporalTank.setLocation((wallInList2.getRightEdgeX() + Tank.SPEED) + 1, wallInList2.getY());
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertFalse(wallInList2.isCollidingWithWall(-Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_CollidingJustAtTopBoundary() {
        //setup
        temporalTank.setLocation(wallInList2.getX(), wallInList2.getTopEdgeY() - Tank.SPEED - Tank.WIDTH);
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertTrue(wallInList2.isCollidingWithWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_NotCollidingJustAtTopBoundary() {
        //setup
        temporalTank.setLocation(wallInList2.getX(), (wallInList2.getTopEdgeY() - Tank.SPEED - Tank.WIDTH) - 1);
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertFalse(wallInList2.isCollidingWithWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_CollidingJustAtBottomBoundary() {
        //setup
        temporalTank.setLocation(wallInList2.getX(), wallInList2.getBottomEdgeY() + Tank.SPEED);
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertTrue(wallInList2.isCollidingWithWall(0, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_NotCollidingJustAtBottomBoundary() {
        //setup
        temporalTank.setLocation(wallInList2.getX(), wallInList2.getBottomEdgeY() + Tank.SPEED + 1);
        //ensures the tank is not already colliding with the wall
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute & check - takes object as input
        assertFalse(wallInList2.isCollidingWithWall(0, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_CollidingInTheWall() {
        //setup
        temporalTank.setLocation(wallInList2.getX(), wallInList2.getY() - Tank.SPEED);
        //execute & check - takes object as input
        assertTrue(wallInList2.isCollidingWithWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithWall_ObjectAlreadyOnWall() {
        temporalTank.setLocation(wallInList2.getX(), wallInList2.getY());
        //execute & check - takes object as input
        try {
            assertTrue(wallInList2.isCollidingWithWall(0, Tank.SPEED, temporalTank));
            fail("Expected AssertionError not thrown.");
        } catch (AssertionError e) {
            //expected error
        }
    }

    //IsCollidingWithLeftWall
    @Test
    void testIsCollidingWithLeftWallTrueFromLeftMiddleJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getY(), 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithLeftWall(Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallFalseFromLeftMiddleJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank((wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED) - 1,
                wallInList2.getY(), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithLeftWall(Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallTrueFromLeftTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.SPEED - Tank.WIDTH,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - (Tank.SPEED / 2) + 1, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithLeftWall(Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallFalseFromLeftTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank((wallInList2.getLeftEdgeX() - Tank.SPEED - Tank.WIDTH) - 1,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - (Tank.SPEED / 2) + 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithLeftWall(Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallTrueFromLeftBottomJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.SPEED - Tank.WIDTH,
                wallInList2.getBottomEdgeY() + (Tank.SPEED / 2) - 1, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithLeftWall(Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallFalseFromLeftBottomJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank((wallInList2.getLeftEdgeX() - Tank.SPEED - Tank.WIDTH) - 1,
                wallInList2.getBottomEdgeY() + (Tank.SPEED / 2) - 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithLeftWall(Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallFalseCollisionButOnlyFromRightWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getY(), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithLeftWall(-Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallFalseCollisionButOnlyFromTopWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - (Tank.SPEED / 2),
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithLeftWall(Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallTrueCollisionAtLeftTopCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithLeftWall(Tank.SPEED + 1, Tank.SPEED + 1, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallFalseCollisionButOnlyFromBottomWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - (Tank.SPEED / 2),
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithLeftWall(Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallTrueCollisionAtLeftBottomCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithLeftWall(Tank.SPEED + 1, -Tank.SPEED - 1, temporalTank));
    }

    @Test
    void testIsCollidingWithLeftWallObjectAlreadyOnWall() {
        Tank temporalTank = new Tank(wallInList2.getX(), wallInList2.getY(), 0);
        //execute & check - takes object as input
        try {
            assertTrue(wallInList2.isCollidingWithLeftWall(Tank.SPEED, -Tank.SPEED, temporalTank));
            fail("Expected AssertionError not thrown.");
        } catch (AssertionError e) {
            //expected error
        }
    }

    //IsCollidingWithRightWall
    @Test
    void testIsCollidingWithRightWallTrueFromRightMiddleJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getY(), 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithRightWall(-Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallFalseFromRightMiddleJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED + 1,
                wallInList2.getY(), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithRightWall(-Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallTrueFromRightTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - (Tank.SPEED / 2), 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithRightWall(-Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallFalseFromRightTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED + 1,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - (Tank.SPEED / 2), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithRightWall(-Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallTrueFromRightBottomJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getBottomEdgeY() + (Tank.SPEED / 2), 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithRightWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallFalseFromRightBottomJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED + 1,
                wallInList2.getBottomEdgeY() + (Tank.SPEED / 2), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithRightWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallFalseCollisionButOnlyFromLeftWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getY(), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithRightWall(Tank.SPEED, 0, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallFalseCollisionButOnlyFromTopWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + (Tank.SPEED / 2),
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithRightWall(-Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallTrueCollisionAtRightTopCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithRightWall(-Tank.SPEED - 1, Tank.SPEED + 1, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallFalseCollisionButOnlyFromBottomWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + (Tank.SPEED / 2),
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithRightWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallTrueCollisionAtRightBottomCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithRightWall(-Tank.SPEED - 1, -Tank.SPEED - 1, temporalTank));
    }

    @Test
    void testIsCollidingWithRightWallObjectAlreadyOnWall() {
        Tank temporalTank = new Tank(wallInList2.getX(), wallInList2.getY(), 0);
        //execute & check - takes object as input
        try {
            assertTrue(wallInList2.isCollidingWithRightWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
            fail("Expected AssertionError not thrown.");
        } catch (AssertionError e) {
            //expected error
        }
    }

    //IsCollidingWithTopWall
    @Test
    void testIsCollidingWithTopWallCollidingFromMiddleTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX(),
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithTopWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallNotCollidingFromMiddleTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX(),
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED - 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithTopWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallCollidingFromLeftTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.SPEED - Tank.HEIGHT, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithTopWall(Tank.SPEED + 1, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallNotCollidingFromLeftTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX() - Tank.WIDTH - (Tank.SPEED / 2),
                wallInList2.getTopEdgeY() - Tank.SPEED - Tank.HEIGHT - 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithTopWall(Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallTrueFromRightTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + (Tank.SPEED / 2),
                wallInList2.getTopEdgeY() - Tank.SPEED - Tank.HEIGHT, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithTopWall(-Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallFalseFromRightTopJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX() + (Tank.SPEED / 2),
                wallInList2.getTopEdgeY() - Tank.SPEED - Tank.HEIGHT - 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithTopWall(-Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallFalseCollisionButOnlyFromBottomWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX(),
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithTopWall(0, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallFalseCollisionButOnlyFromRightWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - (Tank.SPEED / 2), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithTopWall(-Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallTrueCollisionAtRightTopCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithTopWall(-Tank.SPEED - 1, Tank.SPEED + 1, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallFalseCollisionButOnlyFromLeftWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - (Tank.SPEED / 2), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithTopWall(Tank.SPEED, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallTrueCollisionAtLeftTopCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithTopWall(Tank.SPEED + 1, Tank.SPEED + 1, temporalTank));
    }

    @Test
    void testIsCollidingWithTopWallObjectAlreadyOnWall() {
        Tank temporalTank = new Tank(wallInList2.getX(), wallInList2.getY(), 0);
        //execute & check - takes object as input
        try {
            assertTrue(wallInList2.isCollidingWithTopWall(Tank.SPEED, Tank.SPEED, temporalTank));
            fail("Expected AssertionError not thrown.");
        } catch (AssertionError e) {
            //expected error
        }
    }

    //IsCollidingWithBottomWall
    @Test
    void testIsCollidingWithBottomWallCollidingFromBottomMiddleJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX(),
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithBottomWall(0, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallNotCollidingFromBottomMiddleJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX(),
                wallInList2.getBottomEdgeY() + Tank.SPEED + 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(0, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallCollidingFromBottomLeftJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithBottomWall(Tank.SPEED + 1, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallNotCollidingFromBottomLeftJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - (Tank.SPEED / 2),
                wallInList2.getBottomEdgeY() + Tank.SPEED + 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallCollidingFromBottomRightJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + (Tank.SPEED / 2),
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithBottomWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallNotCollidingFromBottomRightJustAtBoundary() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + (Tank.SPEED / 2),
                wallInList2.getBottomEdgeY() + Tank.SPEED + 1, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallNotCollidingCollisionButOnlyFromTopWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX(),
                wallInList2.getTopEdgeY() - Tank.HEIGHT - Tank.SPEED, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallFalseCollisionButOnlyFromTopWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX(),
                wallInList2.getTopEdgeY() - Tank.SPEED - Tank.HEIGHT, 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(0, Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallFalseCollisionButOnlyFromRightWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getBottomEdgeY() + (Tank.SPEED / 2), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(-Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallTrueCollisionAtRightBottomCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED,
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithBottomWall(-Tank.SPEED - 1, -Tank.SPEED - 1, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallFalseCollisionButOnlyFromLeftWall() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getBottomEdgeY() + (Tank.SPEED / 2), 0);
        //execute & check
        assertFalse(wallInList2.isCollidingWithBottomWall(Tank.SPEED, -Tank.SPEED, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallTrueCollisionAtLeftBottomCorner() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getLeftEdgeX() - Tank.WIDTH - Tank.SPEED,
                wallInList2.getBottomEdgeY() + Tank.SPEED, 0);
        //execute & check
        assertTrue(wallInList2.isCollidingWithBottomWall(Tank.SPEED + 1, -Tank.SPEED - 1, temporalTank));
    }

    @Test
    void testIsCollidingWithBottomWallObjectAlreadyOnWall() {
        Tank temporalTank = new Tank(wallInList2.getX(), wallInList2.getY(), 0);
        //execute & check - takes object as input
        try {
            assertTrue(wallInList2.isCollidingWithBottomWall(Tank.SPEED, -Tank.SPEED, temporalTank));
            fail("Expected AssertionError not thrown.");
        } catch (AssertionError e) {
            //expected error
        }
    }

    //CoordinateXIfObjectCollidesAndIsPushedBack
    @Test
    void testCoordinateXIfObjectCollidesAndIsPushedBackObjectCollidesFromLeft() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX() - Tank.WIDTH - Tank.SPEED * 5,
                wallInList2.getY(), 0);
        //ensure the tank is not already colliding
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute
        assertEquals(wallInList2.getLeftEdgeX() - Tank.WIDTH - 1,
                wallInList2.coordinateXIfObjectCollidesAndIsPushedBack(Tank.SPEED * 5,
                        0, temporalTank));
    }

    @Test
    void testCoordinateXIfObjectCollidesAndIsPushedBackObjectCollidesFromRight() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + Tank.SPEED * 5,
                wallInList2.getY(), 0);
        //ensure the tank is not already colliding
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute
        assertEquals(wallInList2.getRightEdgeX() + 1,
                wallInList2.coordinateXIfObjectCollidesAndIsPushedBack(-Tank.SPEED * 5,
                        0, temporalTank));
    }

    @Test
    void testCoordinateXIfObjectCollidesAndIsPushedBackObjectDoesNotCollide() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + 1,
                wallInList2.getBottomEdgeY() + 1, 0);
        //ensure the tank is not already colliding
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute
        assertEquals(wallInList2.getRightEdgeX() + Tank.SPEED * 5 + 1,
                wallInList2.coordinateXIfObjectCollidesAndIsPushedBack(Tank.SPEED * 5,
                        0, temporalTank));
    }

    //CoordinateYIfObjectCollidesAndIsPushedBack
    @Test
    void testCoordinateYIfObjectCollidesAndIsPushedBackObjectCollidesFromBelow() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX(),
                wallInList2.getBottomEdgeY() + Tank.SPEED * 5, 0);
        //ensure the tank is not already colliding
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute
        assertEquals(wallInList2.getBottomEdgeY() + 1,
                wallInList2.coordinateYIfObjectCollidesAndIsPushedBack(0,
                        -Tank.SPEED * 5, temporalTank));
    }

    @Test
    void testCoordinateYIfObjectCollidesAndIsPushedBackObjectCollidesFromAbove() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getX(),
                wallInList2.getY() - Tank.HEIGHT - Tank.SPEED * 5, 0);
        //ensure the tank is not already colliding
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute
        assertEquals(wallInList2.getTopEdgeY() - Tank.HEIGHT - 1,
                wallInList2.coordinateYIfObjectCollidesAndIsPushedBack(0,
                        Tank.SPEED * 5, temporalTank));
    }

    @Test
    void testCoordinateYIfObjectCollidesAndIsPushedBackObjectDoesNotCollide() {
        //setup
        Tank temporalTank = new Tank(wallInList2.getRightEdgeX() + 1,
                wallInList2.getBottomEdgeY() + 1, 0);
        //ensure the tank is not already colliding
        tankGameObjectNotOnTopOfWallAlready(temporalTank);
        //execute
        assertEquals(wallInList2.getBottomEdgeY() + Tank.SPEED * 5 + 1,
                wallInList2.coordinateYIfObjectCollidesAndIsPushedBack(0, Tank.SPEED * 5, temporalTank));
    }

    //HasIdenticalProperties
    @Test
    void testHasIdenticalPropertiesWallsAreIdentical() {
        //setup
        Wall comparedWall = new Wall(Wall.WIDTH * 3, Wall.HEIGHT);
        //execute & check
        assertTrue(testWall.hasIdenticalProperties(comparedWall));
    }

    @Test
    void testHasIdenticalPropertiesOnlyPosXIsIdentical() {
        //setup
        Wall comparedWall = new Wall(Wall.WIDTH * 3, 0);
        //execute & check
        assertFalse(testWall.hasIdenticalProperties(comparedWall));
    }

    @Test
    void testHasIdenticalPropertiesPosXIsNotIdentical() {
        //setup
        Wall comparedWall = new Wall(0, Wall.HEIGHT);
        //execute & check
        assertFalse(testWall.hasIdenticalProperties(comparedWall));
    }

    //ContainsIdenticalWall
    @Test
    void testContainsIdenticalWallDoesContainIdenticalWall() {
        //setup
        someListOfWall.add(identicalPropertiesToTestWall);
        //execute & check
        assertTrue(testWall.wallWithIdenticalPropertyContained(someListOfWall));
    }

    @Test
    void testContainsIdenticalWallNoIdenticalWallContained() {
        //execute & check
        assertFalse(testWall.wallWithIdenticalPropertyContained(someListOfWall));
    }

    //TopLeftXOfWallBoundingThisCoordinate
    @Test
    void testTopLeftXOfWallBoundingThisCoordinateNotAtBoundary() {
        //execute & check
        assertEquals(0,
                Wall.topLeftXOfWallBoundingThisCoordinate(Wall.WIDTH / 2));
        //another execute & check
        assertEquals(Wall.WIDTH * 3,
                Wall.topLeftXOfWallBoundingThisCoordinate(Wall.WIDTH * 3 + Wall.WIDTH / 2));
    }

    @Test
    void testTopLeftXOfWallBoundingThisCoordinateAtRightOrLeftBoundary() {
        //execute & check
        assertEquals(Wall.WIDTH,
                Wall.topLeftXOfWallBoundingThisCoordinate(Wall.WIDTH));
        //another execute & check
        assertEquals(Wall.WIDTH * 3,
                Wall.topLeftXOfWallBoundingThisCoordinate(Wall.WIDTH * 3));
    }

    //TopLeftYOfWallBoundingThisCoordinate
    @Test
    void testTopLeftYOfWallBoundingThisCoordinateNotAtBoundary() {
        //execute & check
        assertEquals(0,
                Wall.topLeftYOfWallBoundingThisCoordinate(Wall.HEIGHT / 2));
        //another execute & check
        assertEquals(Wall.HEIGHT * 5,
                Wall.topLeftYOfWallBoundingThisCoordinate(Wall.HEIGHT * 5 + Wall.HEIGHT / 2));
    }

    @Test
    void testTopLeftYOfWallBoundingThisCoordinateAtUpperOrLowerBoundary() {
        //execute & check
        assertEquals(Wall.HEIGHT,
                Wall.topLeftYOfWallBoundingThisCoordinate(Wall.HEIGHT));
        //another execute & check
        assertEquals(Wall.HEIGHT * 5,
                Wall.topLeftYOfWallBoundingThisCoordinate(Wall.HEIGHT * 5));
    }

    //toJson
    @Test
    void testToJson() {
        //execute
        JSONObject jsonObject = testWall.toJson();
        //check
        assertEquals(testWall.getX(), jsonObject.get("posX"));
        assertEquals(testWall.getY(), jsonObject.get("posY"));
    }

    //getWidth & getHeight
    @Test
    void testGetWidthAndGetHeight() {
        //execute & check
        assertEquals(Wall.HEIGHT, wallInList1.getHeight());
        assertEquals(Wall.WIDTH, wallInList1.getWidth());
    }

    //EFFECTS: checks if the TankGameObject given is not on top of the wall already
    private void tankGameObjectNotOnTopOfWallAlready(TankGameObject tankGameObject) {
        assertFalse(wallInList2.isCollidingWith(tankGameObject));
    }

}
