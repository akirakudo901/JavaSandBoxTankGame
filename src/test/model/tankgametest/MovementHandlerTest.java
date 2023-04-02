package model.tankgametest;

import model.Bullet;
import model.Wall;
import model.tankgame.CollisionHandler;
import model.tankgame.MovementHandler;
import model.tankgame.TankGame;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MovementHandlerTest {
    MovementHandler movementHandler;
    CollisionHandler collisionHandler;
//    TankGame gameWithOnlyDefaultWallsAndPlayerTank;
    PlayerTank playerTank;
    EnemyTank enemyTank1;
    EnemyTank enemyTank2;
    List<EnemyTank> enemyTanks;
    Wall wall1;
    Wall wall2;
    List<Wall> walls;
    Bullet collidingBullet;
    Bullet nonCollidingBullet;

    final int P_TANK_X = TankGame.WIDTH / 2;
    final int P_TANK_Y = TankGame.HEIGHT / 2;
    final int P_TANK_GA = 0;

    final int E1_TANK_X = TankGame.WIDTH / 4;
    final int E1_TANK_Y = TankGame.HEIGHT / 4;
    final int E1_TANK_GA = 180;

    final int E2_TANK_X = TankGame.WIDTH / 4;
    final int E2_TANK_Y = TankGame.HEIGHT / 4;
    final int E2_TANK_GA = 180;

    final int COLLIDING_B_X = Wall.WIDTH * 3;
    final int COLLIDING_B_Y = Wall.HEIGHT * 2;
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
        wall1 = new Wall(Wall.WIDTH * 3, Wall.HEIGHT * 3);
        wall2 = new Wall(Wall.WIDTH * 4, Wall.HEIGHT * 2);
        walls = new ArrayList<>();
        walls.add(wall1);
        walls.add(wall2);

        collisionHandler = new CollisionHandler(playerTank, enemyTanks, walls, new ArrayList<>());
        movementHandler = new MovementHandler(collisionHandler);

//        gameWithOnlyDefaultWallsAndPlayerTank = new TankGame("game2", playerTank, new ArrayList<>(),
//                new ArrayList<>());
        collidingBullet = new Bullet(COLLIDING_B_X, COLLIDING_B_Y, COLLIDING_B_VEL_X, COLLIDING_B_VEL_Y);
        nonCollidingBullet = new Bullet(NON_COLLIDING_B_X, NON_COLLIDING_B_Y,
                NON_COLLIDING_B_VEL_X, NON_COLLIDING_B_VEL_Y);
        playerTank.addBullet(collidingBullet);
        playerTank.addBullet(nonCollidingBullet);
    }

    //Constructor is a simple setter and does not require a test

    //MoveAllEnemyTanks
    //TODO
    //THIS WILL NOT DO MUCH, SINCE ENEMY TANKS WON'T MOVE IN PHASE 1
    @Test
    void testMoveAllEnemyTanksTheyDoNotMoveYet() {
        //execute
        movementHandler.moveAllEnemyTanks();
        //check
        assertEquals(E1_TANK_X, enemyTank1.getX());
        assertEquals(E1_TANK_Y, enemyTank1.getY());
        assertEquals(E1_TANK_GA - Tank.ROTATE_SPEED, enemyTank1.getGunAngle());
        assertEquals(E2_TANK_X, enemyTank2.getX());
        assertEquals(E2_TANK_Y, enemyTank2.getY());
        assertEquals(E2_TANK_GA - Tank.ROTATE_SPEED, enemyTank2.getGunAngle());
    }

    //MoveAllBullets
    @Test
    void testMoveAllBulletsOneMovesWithoutCollisionTheOtherCollidesAndBounces() {
        //setup - non-colliding bullet
        int bulletPosX1 = nonCollidingBullet.getX();
        int bulletPosY1 = nonCollidingBullet.getY();
        int bulletVelX1 = nonCollidingBullet.getVelX();
        int bulletVelY1 = nonCollidingBullet.getVelY();
        //setup - colliding bullet
        int bulletPosX2 = collidingBullet.getX();
        int bulletPosY2 = collidingBullet.getY();
        int bulletVelX2 = collidingBullet.getVelX();
        int bulletVelY2 = collidingBullet.getVelY();
        //execute
        movementHandler.moveAllBullets();
        //check - non-colliding bullet
        assertEquals(bulletPosX1 + bulletVelX1, nonCollidingBullet.getX());
        assertEquals(bulletPosY1 + bulletVelY1,
                nonCollidingBullet.getY());
        assertEquals(bulletVelX1, nonCollidingBullet.getVelX());
        assertEquals(bulletVelY1, nonCollidingBullet.getVelY());
        assertEquals(0, nonCollidingBullet.getBounceCount());
        //check - colliding bullet
        assertEquals(bulletPosX2 + ((wall2.getLeftEdgeX() - Bullet.WIDTH - 1 - bulletPosX2) * 2
                - bulletVelX2),
                collidingBullet.getX());
        assertEquals(bulletPosY2 + bulletVelY2,
                collidingBullet.getY());
        assertEquals(-bulletVelX2, collidingBullet.getVelX());
        assertEquals(bulletVelY2, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    @Test
    void testMoveAllBulletsOneMovesWithoutCollisionTheOtherCollidesAndBouncesAndGetsRemoved() {
        //setup - non-colliding bullet
        int bulletPosX1 = nonCollidingBullet.getX();
        int bulletPosY1 = nonCollidingBullet.getY();
        int bulletVelX1 = nonCollidingBullet.getVelX();
        int bulletVelY1 = nonCollidingBullet.getVelY();
        //setup - colliding bullet; will have one less bounce count compared to the max bounce count
        int maxBounceMinusOne = TankGame.MAX_BOUNCE_COUNT - collidingBullet.getBounceCount() - 1;
        for (int i = 0; i < maxBounceMinusOne; i++) {
            collidingBullet.increaseBounceCount();
        }
        //execute
        movementHandler.moveAllBullets();
        //check - non-colliding bullet
        assertEquals(bulletPosX1 + bulletVelX1, nonCollidingBullet.getX());
        assertEquals(bulletPosY1 + bulletVelY1, nonCollidingBullet.getY());
        assertEquals(bulletVelX1, nonCollidingBullet.getVelX());
        assertEquals(bulletVelY1, movementHandler.getPlayerTank().getBullets().get(0).getVelY());
        assertEquals(0, movementHandler.getPlayerTank().getBullets().get(0).getBounceCount());
        //check - colliding bullet; is getting removed
        assertFalse(movementHandler.getPlayerTank().getBullets().contains(collidingBullet));
        assertEquals(1, movementHandler.getPlayerTank().getBullets().size());
    }

    @Test
    void moveBulletWithoutCollision() {
        //setup
        int bulletPosX = nonCollidingBullet.getX();
        int bulletPosY = nonCollidingBullet.getY();
        int bulletVelX = nonCollidingBullet.getVelX();
        int bulletVelY = nonCollidingBullet.getVelY();
        //execute
        movementHandler.moveAllBullets();
        //check
        assertEquals(bulletPosX + nonCollidingBullet.getVelX(), nonCollidingBullet.getX());
        assertEquals(bulletPosY + nonCollidingBullet.getVelY(), nonCollidingBullet.getY());
        assertEquals(bulletVelX, nonCollidingBullet.getVelX());
        assertEquals(bulletVelY, nonCollidingBullet.getVelY());
        assertEquals(0, nonCollidingBullet.getBounceCount());
    }

    @Test
    void moveBulletWithCollisionBounceToLeft() {
        //setup
        int bulletPosX = collidingBullet.getX();
        int bulletPosY = collidingBullet.getY();
        int bulletVelX = collidingBullet.getVelX();
        int bulletVelY = collidingBullet.getVelY();
        //execute
        movementHandler.moveAllBullets();
        //check
        assertEquals(bulletPosX + ((wall2.getLeftEdgeX() - Bullet.WIDTH - 1 - bulletPosX) * 2
                - bulletVelX), collidingBullet.getX());
        assertEquals(bulletPosY + bulletVelY, collidingBullet.getY());
        assertEquals(-bulletVelX, collidingBullet.getVelX());
        assertEquals(bulletVelY, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    @Test
    void moveBulletWithCollisionBounceToRight() {
        //setup
        collidingBullet.setLocation(wall2.getRightEdgeX() + Wall.WIDTH - 1, wall2.getY());
        collidingBullet.setBulletVelocity(-Wall.WIDTH, 0);
        int bulletPosX = collidingBullet.getX();
        int bulletPosY = collidingBullet.getY();
        int bulletVelX = collidingBullet.getVelX();
        int bulletVelY = collidingBullet.getVelY();
        //execute
        movementHandler.moveAllBullets();
        //check
        assertEquals(bulletPosX - ((bulletPosX - wall2.getRightEdgeX() - 1) * 2 + bulletVelX),
                collidingBullet.getX());
        assertEquals(bulletPosY + bulletVelY, collidingBullet.getY());
        assertEquals(-bulletVelX, collidingBullet.getVelX());
        assertEquals(bulletVelY, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    @Test
    void moveBulletWithCollisionBounceUp() {
        //setup
        collidingBullet.setLocation(wall2.getX(), wall2.getTopEdgeY() - Wall.HEIGHT + 1);
        collidingBullet.setBulletVelocity(0, Wall.HEIGHT);
        int bulletPosX = collidingBullet.getX();
        int bulletPosY = collidingBullet.getY();
        int bulletVelX = collidingBullet.getVelX();
        int bulletVelY = collidingBullet.getVelY();
        //execute
        movementHandler.moveAllBullets();
        //check
        assertEquals(bulletPosX + bulletVelX, collidingBullet.getX());
        assertEquals(bulletPosY + ((wall2.getTopEdgeY() - Bullet.HEIGHT - 1 - bulletPosY) * 2
                - bulletVelY), collidingBullet.getY());
        assertEquals(bulletVelX, collidingBullet.getVelX());
        assertEquals(-bulletVelY, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    @Test
    void moveBulletWithCollisionBounceDown() {
        //setup
        collidingBullet.setLocation(wall2.getX(), wall2.getBottomEdgeY() + Wall.HEIGHT - 1);
        collidingBullet.setBulletVelocity(0, -Wall.HEIGHT);
        int bulletPosX = collidingBullet.getX();
        int bulletPosY = collidingBullet.getY();
        int bulletVelX = collidingBullet.getVelX();
        int bulletVelY = collidingBullet.getVelY();
        //execute
        movementHandler.moveAllBullets();
        //check
        assertEquals(bulletPosX + bulletVelX, collidingBullet.getX());
        assertEquals(bulletPosY - ((bulletPosY - wall2.getBottomEdgeY() - 1) * 2 + bulletVelY),
                collidingBullet.getY());
        assertEquals(bulletVelX, collidingBullet.getVelX());
        assertEquals(-bulletVelY, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    //movePlayerTank
        /*
    //TODO
    //MODIFIES: this and all tank objects and all bullet objects
    //EFFECTS: update all the tank and bullets objects' state by one tick
    public void update(int keyEvent) {
        movePlayerTank(keyEvent);
        moveAllEnemyTanks();
        moveAllBullets();
    }
    */

    //THESE TESTS WOULD BE USEFUL AFTER PHASE 1, WHEN THE PLAYER TANK STARTS MOVING
    /*
    @Test
    void testMovePlayerTankMoveUpWithoutCollision() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        //execute
        game.movePlayerTank(TankGame.UP);
        //check
        assertEquals(P_TANK_X, game.getPlayerTank().getPosX());
        assertEquals(P_TANK_Y + Tank.SPEED, game.getPlayerTank().getPosY());
    }

    @Test
    void testMovePlayerTankMoveDownWithoutCollision() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        //execute
        game.movePlayerTank(TankGame.DOWN);
        //check
        assertEquals(P_TANK_X, game.getPlayerTank().getPosX());
        assertEquals(P_TANK_Y - Tank.SPEED, game.getPlayerTank().getPosY());
    }

    @Test
    void testMovePlayerTankMoveRightWithoutCollision() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        //execute
        game.movePlayerTank(TankGame.RIGHT);
        //check
        assertEquals(P_TANK_X + Tank.SPEED, game.getPlayerTank().getPosX());
        assertEquals(P_TANK_Y, game.getPlayerTank().getPosY());

    }

    @Test
    void testMovePlayerTankMoveLeftWithoutCollision() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        //execute
        game.movePlayerTank(TankGame.LEFT);
        //check
        assertEquals(P_TANK_X - Tank.SPEED, game.getPlayerTank().getPosX());
        assertEquals(P_TANK_Y, game.getPlayerTank().getPosY());
    }

     */

    @Test
    void testMovePlayerTankRotateGunClockwise() {
        //execute
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        //check
        assertEquals((P_TANK_GA + Tank.ROTATE_SPEED) % 360,
                movementHandler.getPlayerTank().getGunAngle());
    }

    @Test
    void testMovePlayerTankRotateGunAntiClockwise() {
        //execute
        movementHandler.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        //check
        assertEquals((360 + P_TANK_GA - Tank.ROTATE_SPEED) % 360,
                movementHandler.getPlayerTank().getGunAngle());
    }

    //THIS TEST STARTS TO BE USEFUL AFTER PHASE 1, WHEN TANKS START TO MOVE
    /*
    @Test
    void testMovePlayerTankMoveAroundWithoutCollision() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        //execute
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.RIGHT);
        game.movePlayerTank(TankGame.DOWN);
        game.movePlayerTank(TankGame.DOWN);
        game.movePlayerTank(TankGame.LEFT);
        game.movePlayerTank(TankGame.LEFT);
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.RIGHT);
        game.movePlayerTank(TankGame.DOWN);
        //check
        assertEquals(P_TANK_X, game.getPlayerTank().getPosX());
        assertEquals(P_TANK_Y, game.getPlayerTank().getPosY());
    }

     */

    @Test
    void testMovePlayerTankRotateGunAround() {
        //execute
        movementHandler.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.CLOCKWISE);
        movementHandler.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        //check
        assertEquals((P_TANK_GA + (3 * Tank.ROTATE_SPEED)) % 360,
                movementHandler.getPlayerTank().getGunAngle());
    }

    //THIS TEST STARTS TO BE USEFUL AFTER PHASE 1, WHEN TANKS START TO MOVE
    /*
    @Test
    void testMovePlayerTankMoveAroundWithoutCollisionAndRotateGunAround() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        //execute
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        game.movePlayerTank(TankGame.RIGHT);
        game.movePlayerTank(TankGame.DOWN);
        game.movePlayerTank(TankGame.CLOCKWISE);
        game.movePlayerTank(TankGame.ANTI_CLOCKWISE);
        game.movePlayerTank(TankGame.DOWN);
        game.movePlayerTank(TankGame.LEFT);
        game.movePlayerTank(TankGame.CLOCKWISE);
        game.movePlayerTank(TankGame.UP);
        //check
        assertEquals(P_TANK_X, game.getPlayerTank().getPosX());
        assertEquals(P_TANK_Y, game.getPlayerTank().getPosY());
        assertEquals(P_TANK_GA, game.getPlayerTank().getGunAngle());
    }

     */

    /* THE TANK WILL NOT BE MOVING IN PHASE 1, AND I WILL NOT BE DEALING WITH COLLISIONS YET
    //TODO
    @Test
    void testMovePlayerTankMoveUpWithCollision() {
        //setup
        TankGame game = gameWithOnlyDefaultWallsAndPlayerTank;
        int wallToCollidePosX = Wall.centreXOfWallBoundingThisCoordinate(playerTank.getPosX());
        int wallToCollidePosY = Wall.centreYOfWallBoundingThisCoordinate(playerTank.getPosY())
                + Tank.SPEED * 5;
        Wall wallToCollide = new Wall(wallToCollidePosX, wallToCollidePosY);
        game.addWall(wallToCollide);
        //execute
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.UP);
        game.movePlayerTank(TankGame.UP);
        //check
        assertEquals(P_TANK_X, game.getPlayerTank().getPosX());
        assertEquals(wallToCollidePosY - Wall.HEIGHT / 2, game.getPlayerTank().getPosY());
    }

     */

    /*
    //TODO
    //MODIFIES: this and tank
    //EFFECTS: moves the player's tank according to input
    public void movePlayerTank(int keyEvent) {
        //stub
    }

     */

}
