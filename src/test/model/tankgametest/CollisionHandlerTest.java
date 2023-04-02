package model.tankgametest;

import model.Bullet;
import model.Wall;
import model.tankgame.CollisionHandler;
import model.tankgame.TankGame;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionHandlerTest {
    CollisionHandler collisionHandler;
    //    TankGame gameWithOnlyDefaultWallsAndPlayerTank;
    PlayerTank playerTank;
    EnemyTank enemyTank1;
    EnemyTank enemyTank2;
    List<EnemyTank> enemyTanks;
    Wall wallCollidingWithBullet;
    Wall wall2;
    List<Wall> walls;
    Bullet collidingBullet;
    Bullet nonCollidingBullet;
    List<Bullet> bulletsWithoutTankOwner;

    final int P_TANK_X = TankGame.WIDTH / 2;
    final int P_TANK_Y = TankGame.HEIGHT / 2;
    final int P_TANK_GA = 0;

    final int E1_TANK_X = TankGame.WIDTH / 4;
    final int E1_TANK_Y = TankGame.HEIGHT / 4;
    final int E1_TANK_GA = 180;

    final int E2_TANK_X = TankGame.WIDTH / 4;
    final int E2_TANK_Y = TankGame.HEIGHT / 4;
    final int E2_TANK_GA = 180;

    final int WALL_COLLIDING_WITH_BULLET_X = Wall.WIDTH * 3;
    final int WALL_COLLIDING_WITH_BULLET_Y = Wall.HEIGHT * 2;

    final int COLLIDING_B_X = WALL_COLLIDING_WITH_BULLET_X - Wall.WIDTH + 1;
    final int COLLIDING_B_Y = WALL_COLLIDING_WITH_BULLET_Y;
    final int COLLIDING_B_VEL_X = Wall.WIDTH;
    final int COLLIDING_B_VEL_Y = 0;

    final int NON_COLLIDING_B_X = Wall.WIDTH;
    final int NON_COLLIDING_B_Y = Wall.HEIGHT * 5;
    final int NON_COLLIDING_B_VEL_X = Wall.WIDTH;
    final int NON_COLLIDING_B_VEL_Y = -Wall.HEIGHT;

    @BeforeEach
    void runBeforeEach() {
        playerTank = new PlayerTank(P_TANK_X, P_TANK_Y, P_TANK_GA);
        enemyTank1 = new EnemyTank(E1_TANK_X, E1_TANK_Y, E1_TANK_GA);
        enemyTank2 = new EnemyTank(E2_TANK_X, E2_TANK_Y, E2_TANK_GA);
        enemyTanks = new ArrayList<>();
        enemyTanks.add(enemyTank1);
        enemyTanks.add(enemyTank2);
        wallCollidingWithBullet = new Wall(Wall.WIDTH * 3, Wall.HEIGHT * 3);
        wall2 = new Wall(Wall.WIDTH * 4, Wall.HEIGHT * 2);
        walls = new ArrayList<>();
        walls.add(wallCollidingWithBullet);
        walls.add(wall2);
        bulletsWithoutTankOwner = new ArrayList<>();

        collisionHandler = new CollisionHandler(playerTank, enemyTanks, walls, bulletsWithoutTankOwner);
//        gameWithOnlyDefaultWallsAndPlayerTank = new TankGame("game2", playerTank, new ArrayList<>(),
//                new ArrayList<>());
        collidingBullet = new Bullet(COLLIDING_B_X, COLLIDING_B_Y, COLLIDING_B_VEL_X, COLLIDING_B_VEL_Y);
        nonCollidingBullet = new Bullet(NON_COLLIDING_B_X, NON_COLLIDING_B_Y,
                NON_COLLIDING_B_VEL_X, NON_COLLIDING_B_VEL_Y);
        playerTank.addBullet(collidingBullet);
        playerTank.addBullet(nonCollidingBullet);
    }

    //getPushedBackPositionIfCollidingOrAdvance
    @Test
    void testGetPushedBackPositionIfCollidingOrAdvance_NotColliding() {
        //execute
        int[] newPos = collisionHandler.getPushedBackPositionIfCollidingOrAdvance(
                nonCollidingBullet.getVelX(), nonCollidingBullet.getVelY(), nonCollidingBullet);
        //check
        assertEquals(NON_COLLIDING_B_X + NON_COLLIDING_B_VEL_X, newPos[0]);
        assertEquals(NON_COLLIDING_B_Y + NON_COLLIDING_B_VEL_Y, newPos[1]);
    }

    @Test
    void testGetPushedBackPositionIfCollidingOrAdvance_IsColliding() {
        //execute
        int[] newPos = collisionHandler.getPushedBackPositionIfCollidingOrAdvance(
                collidingBullet.getVelX(), collidingBullet.getVelY(), collidingBullet);
        //check
        assertEquals(wallCollidingWithBullet.coordinateXIfObjectCollidesAndIsPushedBack(
                collidingBullet.getVelX(), collidingBullet.getVelY(), collidingBullet),
                newPos[0]);
        assertEquals(wallCollidingWithBullet.coordinateYIfObjectCollidesAndIsPushedBack(
                        collidingBullet.getVelX(), collidingBullet.getVelY(), collidingBullet),
                newPos[1]);
    }

    //isCollidingWithAWallInGame
    @Test
    void testIsCollidingWithAWallInGame_IsColliding() {
        //execute & check
        assertTrue(collisionHandler.isCollidingWithAWallInGame(collidingBullet.getVelX(),
                collidingBullet.getVelY(), collidingBullet));
    }

    @Test
    void testIsCollidingWithAWallInGame_IsNotColliding() {
        //execute & check
        assertFalse(collisionHandler.isCollidingWithAWallInGame(nonCollidingBullet.getVelX(),
                nonCollidingBullet.getVelY(), nonCollidingBullet));
    }

}
