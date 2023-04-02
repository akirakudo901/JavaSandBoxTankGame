package model.tankgametest;

import model.Bullet;
import model.Wall;
import model.tankgame.TankGame;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;
import static org.junit.jupiter.api.Assertions.*;

public class TankGameTest {
    TankGame gameWithAllElements;
    TankGame gameWithOnlyDefaultWallsAndPlayerTank;
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
        gameWithAllElements = new TankGame("game1", playerTank, enemyTanks, walls);
        gameWithOnlyDefaultWallsAndPlayerTank = new TankGame("game2", playerTank, new ArrayList<>(),
                new ArrayList<>());
        collidingBullet = new Bullet(COLLIDING_B_X, COLLIDING_B_Y, COLLIDING_B_VEL_X, COLLIDING_B_VEL_Y);
        nonCollidingBullet = new Bullet(NON_COLLIDING_B_X, NON_COLLIDING_B_Y, NON_COLLIDING_B_VEL_X, NON_COLLIDING_B_VEL_Y);
        gameWithAllElements.getPlayerTank().addBullet(collidingBullet);
        gameWithAllElements.getPlayerTank().addBullet(nonCollidingBullet);
    }

    //TESTING THE CONSTRUCTOR WILL ALSO TEST PLACE_DEFAULT_WALLS
    @Test
    void testConstructorAndPlaceDefaultWalls() {
        //setup
        List<Wall> defaultWalls = TankGame.getDefaultWallsGivenGameWidthAndHeight(TankGame.WIDTH, TankGame.HEIGHT);
        //check
        assertEquals("game1", gameWithAllElements.getName());
        assertEquals(playerTank, gameWithAllElements.getPlayerTank());
        assertEquals(2, gameWithAllElements.getEnemyTanks().size());
        assertTrue(gameWithAllElements.getEnemyTanks().contains(enemyTank1));
        assertTrue(gameWithAllElements.getEnemyTanks().contains(enemyTank2));

        assertTrue(gameWithAllElements.getWalls().contains(wall1));
        assertTrue(gameWithAllElements.getWalls().contains(wall2));
        //check for default walls to be placed correctly
        boolean allDefaultWallsPlacedCorrectly = true;
        for (Wall defaultWall : defaultWalls) {
            boolean wallIsInGame = defaultWall.wallWithIdenticalPropertyContained(gameWithAllElements.getWalls());
            if (!wallIsInGame) {
                allDefaultWallsPlacedCorrectly = false;
                break;
            }
        }
        assertTrue(allDefaultWallsPlacedCorrectly);
    }

    @Test
    void testUpdateInputIsClockWiseTurn() {
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
        gameWithAllElements.update();
        gameWithAllElements.handlePlayerTank(TankGame.CLOCKWISE);
        //check - gun angle
        assertEquals((P_TANK_GA + Tank.ROTATE_SPEED) % 360,
                gameWithAllElements.getPlayerTank().getGunAngle());
        //check - move enemy tanks
        assertEquals(E1_TANK_X, enemyTank1.getX());
        assertEquals(E1_TANK_Y, enemyTank1.getY());
        assertEquals(E1_TANK_GA - Tank.ROTATE_SPEED, enemyTank1.getGunAngle());
        assertEquals(E2_TANK_X, enemyTank2.getX());
        assertEquals(E2_TANK_Y, enemyTank2.getY());
        assertEquals(E2_TANK_GA - Tank.ROTATE_SPEED, enemyTank2.getGunAngle());
        //check - move bullets (one advances, the other collides and bounces off)
        //non-colliding bullet
        assertEquals(bulletPosX1 + bulletVelX1, nonCollidingBullet.getX());
        assertEquals(bulletPosY1 + bulletVelY1, nonCollidingBullet.getY());
        assertEquals(bulletVelX1, nonCollidingBullet.getVelX());
        assertEquals(bulletVelY1, nonCollidingBullet.getVelY());
        assertEquals(0, nonCollidingBullet.getBounceCount());
        //colliding bullet
        assertEquals(bulletPosX2 + (wall2.getLeftEdgeX() - Bullet.WIDTH - 1 - bulletPosX2) * 2
                        - bulletVelX2, collidingBullet.getX());
        assertEquals(bulletPosY2 + bulletVelY2, collidingBullet.getY());
        assertEquals(-bulletVelX2, collidingBullet.getVelX());
        assertEquals(bulletVelY2, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    @Test
    void testUpdateInputIsToFireBullets() {
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
        //setup - fired bullet
        Bullet newFiredBullet = new Bullet(
                playerTank.getX() + Tank.WIDTH / 2
                        + (int) (Tank.WIDTH * 0.9 * cos(toRadians(playerTank.getGunAngle()))),
                playerTank.getY() + Tank.HEIGHT / 2
                        + (int) (Tank.HEIGHT * 0.9 * sin(toRadians(playerTank.getGunAngle()))),
                TankGame.BULLET_SPEED * (int) cos(playerTank.getGunAngle()),
                TankGame.BULLET_SPEED * (int) sin(playerTank.getGunAngle()));
        //execute
        gameWithAllElements.update();
        gameWithAllElements.handlePlayerTank(TankGame.FIRE);
        //check - if bullet has been fired
        List<Bullet> playerTankBullets = gameWithAllElements.getPlayerTank().getBullets();
        assertEquals(newFiredBullet.getX(), playerTankBullets.get(playerTankBullets.size() - 1).getX());
        assertEquals(newFiredBullet.getY(), playerTankBullets.get(playerTankBullets.size() - 1).getY());
        assertEquals(newFiredBullet.getVelX(), playerTankBullets.get(playerTankBullets.size() - 1).getVelX());
        assertEquals(newFiredBullet.getVelY(), playerTankBullets.get(playerTankBullets.size() - 1).getVelY());
        //check - move enemy tanks (they do nothing yet)
        assertEquals(E1_TANK_X, enemyTank1.getX());
        assertEquals(E1_TANK_Y, enemyTank1.getY());
        assertEquals(E1_TANK_GA - Tank.ROTATE_SPEED, enemyTank1.getGunAngle());
        assertEquals(E2_TANK_X, enemyTank2.getX());
        assertEquals(E2_TANK_Y, enemyTank2.getY());
        assertEquals(E2_TANK_GA - Tank.ROTATE_SPEED, enemyTank2.getGunAngle());
        //check - move bullets (one advances, the other collides and bounces off)
        //non-colliding bullet
        assertEquals(bulletPosX1 + bulletVelX1, nonCollidingBullet.getX());
        assertEquals(bulletPosY1 + bulletVelY1, nonCollidingBullet.getY());
        assertEquals(bulletVelX1, nonCollidingBullet.getVelX());
        assertEquals(bulletVelY1, nonCollidingBullet.getVelY());
        assertEquals(0, nonCollidingBullet.getBounceCount());
        //colliding bullet
        assertEquals(bulletPosX2 + (wall2.getLeftEdgeX() - Bullet.WIDTH - 1 - bulletPosX2) * 2
                        - bulletVelX2, collidingBullet.getX());
        assertEquals(bulletPosY2 + bulletVelY2, collidingBullet.getY());
        assertEquals(-bulletVelX2, collidingBullet.getVelX());
        assertEquals(bulletVelY2, collidingBullet.getVelY());
        assertEquals(1, collidingBullet.getBounceCount());
    }

    @Test
    void testFireBulletFireOneBullet() {
        //setup
        playerTank.setGunAngle(57);
        Bullet firedBullet = new Bullet(
                playerTank.getX() + Tank.WIDTH / 2
                        + (int) (Tank.WIDTH * 0.9 * cos(toRadians(playerTank.getGunAngle()))),
                playerTank.getY() + Tank.HEIGHT / 2
                        + (int) (Tank.WIDTH * 0.9 * sin(toRadians(playerTank.getGunAngle()))),
                (int) (TankGame.BULLET_SPEED * cos(Math.toRadians(playerTank.getGunAngle()))),
                (int) (TankGame.BULLET_SPEED * sin(Math.toRadians(playerTank.getGunAngle()))));
        //execute
        boolean addedBulletSuccessfully = gameWithAllElements.fireBullet(playerTank);
        //check
        assertTrue(addedBulletSuccessfully);
        List<Bullet> playerTankBullets = gameWithAllElements.getPlayerTank().getBullets();
        assertEquals(firedBullet.getX(), playerTankBullets.get(playerTankBullets.size() - 1).getX());
        assertEquals(firedBullet.getY(), playerTankBullets.get(playerTankBullets.size() - 1).getY());
        assertEquals(firedBullet.getVelX(), playerTankBullets.get(playerTankBullets.size() - 1).getVelX());
        assertEquals(firedBullet.getVelY(), playerTankBullets.get(playerTankBullets.size() - 1).getVelY());
    }

    @Test
    void testFireBulletFireTwoBullets() {
        //setup
        playerTank.setGunAngle(80);
        Bullet firedBullet1 = new Bullet(
                playerTank.getX() + Tank.WIDTH / 2
                        + (int) (Tank.WIDTH * 0.9 * cos(toRadians(playerTank.getGunAngle()))),
                playerTank.getY() + Tank.HEIGHT / 2
                        + (int) (Tank.WIDTH * 0.9 * sin(toRadians(playerTank.getGunAngle()))),
                (int) (TankGame.BULLET_SPEED * cos(Math.toRadians(playerTank.getGunAngle()))),
                (int) (TankGame.BULLET_SPEED * sin(Math.toRadians(playerTank.getGunAngle()))));
        int newAngle = 330;
        Bullet firedBullet2 = new Bullet(
                playerTank.getX() + Tank.WIDTH / 2
                        + (int) (Tank.WIDTH * 0.9 * cos(toRadians(newAngle))),
                playerTank.getY() + Tank.HEIGHT / 2
                        + (int) (Tank.WIDTH * 0.9 * sin(toRadians(newAngle))),
                (int) (TankGame.BULLET_SPEED * cos(Math.toRadians(newAngle))),
                (int) (TankGame.BULLET_SPEED * sin(Math.toRadians(newAngle))));
        //execute
        boolean addedBullet1Successfully = gameWithAllElements.fireBullet(playerTank);
        playerTank.setGunAngle(newAngle); //330
        boolean addedBullet2Successfully = gameWithAllElements.fireBullet(playerTank);
        //check - bullet1
        assertTrue(addedBullet1Successfully);
        List<Bullet> playerTankBullets = gameWithAllElements.getPlayerTank().getBullets();
        assertEquals(firedBullet1.getX(), playerTankBullets.get(playerTankBullets.size() - 2).getX());
        assertEquals(firedBullet1.getY(), playerTankBullets.get(playerTankBullets.size() - 2).getY());
        assertEquals(firedBullet1.getVelX(), playerTankBullets.get(playerTankBullets.size() - 2).getVelX());
        assertEquals(firedBullet1.getVelY(), playerTankBullets.get(playerTankBullets.size() - 2).getVelY());
        //bullet2
        assertTrue(addedBullet2Successfully);
        assertEquals(firedBullet2.getX(), playerTankBullets.get(playerTankBullets.size() - 1).getX());
        assertEquals(firedBullet2.getY(), playerTankBullets.get(playerTankBullets.size() - 1).getY());
        assertEquals(firedBullet2.getVelX(), playerTankBullets.get(playerTankBullets.size() - 1).getVelX());
        assertEquals(firedBullet2.getVelY(), playerTankBullets.get(playerTankBullets.size() - 1).getVelY());
    }

    @Test
    void testFireBulletFireBulletWithAlreadyMaximumNumberOfBullets() {
        //setup - creates a tank game with a player tank without any bullet
        int newAngle = 250;
        PlayerTank newPlayerTank = new PlayerTank(P_TANK_X, P_TANK_Y, P_TANK_GA);
        gameWithAllElements = new TankGame("game1", newPlayerTank, enemyTanks, walls);
        //execute & check
        for (int i = 0; i < TankGame.MAX_BULLET_PER_TANK; i++) {
            boolean addedBulletSuccessfully = gameWithAllElements.fireBullet(newPlayerTank);
            assertTrue(addedBulletSuccessfully);
        }
        playerTank.setGunAngle(newAngle);
        boolean addedNewBulletSuccessfully = gameWithAllElements.fireBullet(newPlayerTank); //has to fail
        //check
        assertFalse(addedNewBulletSuccessfully);
        assertEquals(TankGame.MAX_BULLET_PER_TANK, gameWithAllElements.getPlayerTank().getBullets().size());
    }

    @Test
    void testSetUp() {
        //setup
        List<Wall> defaultWalls = TankGame.getDefaultWallsGivenGameWidthAndHeight(TankGame.WIDTH, TankGame.HEIGHT);
        PlayerTank newPlayerTank = new PlayerTank(P_TANK_X + Wall.WIDTH, P_TANK_Y - Wall.HEIGHT,
                360 - P_TANK_GA);
        EnemyTank newEnemyTank = new EnemyTank(E1_TANK_X + Wall.WIDTH, E1_TANK_Y + Wall.HEIGHT,
                360 - E1_TANK_GA);
        List<EnemyTank> newEnemyTankList = new ArrayList<>();
        newEnemyTankList.add(newEnemyTank);
        Wall newWall = new Wall(Wall.WIDTH * 3, Wall.HEIGHT);
        List<Wall> newWallList = new ArrayList<>();
        newWallList.add(newWall);
        //execute
        gameWithAllElements.setUp(newPlayerTank, newEnemyTankList, newWallList);
        //check
        assertEquals(newPlayerTank, gameWithAllElements.getPlayerTank());
        assertEquals(1, gameWithAllElements.getEnemyTanks().size());
        assertTrue(gameWithAllElements.getEnemyTanks().contains(newEnemyTank));

        assertTrue(gameWithAllElements.getWalls().contains(newWall));
        //check for default walls to be placed correctly
        boolean allDefaultWallsPlacedCorrectly = true;
        for (Wall defaultWall : defaultWalls) {
            boolean wallIsInGame = defaultWall.wallWithIdenticalPropertyContained(gameWithAllElements.getWalls());
            if (!wallIsInGame) {
                allDefaultWallsPlacedCorrectly = false;
                break;
            }
        }
        assertTrue(allDefaultWallsPlacedCorrectly);
    }

    @Test
    void testGetDefaultWallsGivenGameWidthAndHeightCurrentGameWidthAndHeight() {
        //setup
        int numberOfWallsFittingHorizontally = TankGame.WIDTH / Wall.WIDTH;
        int numberOfWallsFittingVertically = TankGame.HEIGHT / Wall.HEIGHT;
        //execute
        List<Wall> defaultWalls = TankGame.getDefaultWallsGivenGameWidthAndHeight(TankGame.WIDTH, TankGame.HEIGHT);
        //check
        //for left & right column
        boolean leftOrRightColumnWorking = true;
        for (int i = 0; i < numberOfWallsFittingVertically; i++) {
            Wall tempLeftWall = new Wall(0, Wall.HEIGHT * i);
            Wall tempRightWall = new Wall(Wall.WIDTH * (numberOfWallsFittingHorizontally - 1),
                    Wall.HEIGHT * i);
            if ((!tempLeftWall.wallWithIdenticalPropertyContained(defaultWalls))
                    || (!tempRightWall.wallWithIdenticalPropertyContained(defaultWalls))) {
                leftOrRightColumnWorking = false;
                break;
            }
        }
        //for bottom & top row
        boolean bottomOrTopRowWorking = true;
        for (int i = 1; i < (numberOfWallsFittingHorizontally - 1); i++) {
            Wall tempBottomWall = new Wall(Wall.WIDTH * i, 0);
            Wall tempTopWall = new Wall(Wall.WIDTH * i,
                    Wall.HEIGHT * (numberOfWallsFittingVertically - 1));
            if ((!tempBottomWall.wallWithIdenticalPropertyContained(defaultWalls))
                    || (!tempTopWall.wallWithIdenticalPropertyContained(defaultWalls))) {
                bottomOrTopRowWorking = false;
                break;
            }
        }
        assertTrue(leftOrRightColumnWorking);
        assertTrue(bottomOrTopRowWorking);
    }

    @Test
    void testGetDefaultWallsGivenGameWidthAndHeightWidth400AndHeight1200() {
        //setup
        int numberOfWallsFittingHorizontally = 400 / Wall.WIDTH;
        int numberOfWallsFittingVertically = 1200 / Wall.HEIGHT;
        //execute
        List<Wall> defaultWalls = TankGame.getDefaultWallsGivenGameWidthAndHeight(400, 1200);
        //check
        //for left & right column
        boolean leftOrRightColumnWorking = true;
        for (int i = 0; i < numberOfWallsFittingVertically; i++) {
            Wall tempLeftWall = new Wall(0, Wall.HEIGHT * i);
            Wall tempRightWall = new Wall(Wall.WIDTH * (numberOfWallsFittingHorizontally - 1),
                    Wall.HEIGHT * i);
            if ((!tempLeftWall.wallWithIdenticalPropertyContained(defaultWalls))
                    || (!tempRightWall.wallWithIdenticalPropertyContained(defaultWalls))) {
                leftOrRightColumnWorking = false;
                break;
            }
        }
        //for bottom & top row
        boolean bottomOrTopRowWorking = true;
        for (int i = 1; i < (numberOfWallsFittingHorizontally - 1); i++) {
            Wall tempBottomWall = new Wall(Wall.WIDTH * i, 0);
            Wall tempTopWall = new Wall(Wall.WIDTH * i,
                    Wall.HEIGHT * (numberOfWallsFittingVertically - 1));
            if ((!tempBottomWall.wallWithIdenticalPropertyContained(defaultWalls))
                    || (!tempTopWall.wallWithIdenticalPropertyContained(defaultWalls))) {
                bottomOrTopRowWorking = false;
                break;
            }
        }
        assertTrue(leftOrRightColumnWorking);
        assertTrue(bottomOrTopRowWorking);
    }

    @Test
    void testAddWallOneTime() {
        //setup
        int originalWallNumber = gameWithOnlyDefaultWallsAndPlayerTank.getWalls().size();
        assertFalse(wall1.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        //execute
        gameWithOnlyDefaultWallsAndPlayerTank.addWall(wall1);
        //check
        assertTrue(wall1.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertEquals(originalWallNumber + 1, gameWithOnlyDefaultWallsAndPlayerTank.getWalls().size());
    }

    @Test
    void testAddWallTwoTimes() {
        //setup
        int originalWallNumber = gameWithOnlyDefaultWallsAndPlayerTank.getWalls().size();
        assertFalse(wall1.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertFalse(wall2.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        //execute
        gameWithOnlyDefaultWallsAndPlayerTank.addWall(wall1);
        gameWithOnlyDefaultWallsAndPlayerTank.addWall(wall2);
        //check
        assertTrue(wall1.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertTrue(wall2.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertEquals(originalWallNumber + 2, gameWithOnlyDefaultWallsAndPlayerTank.getWalls().size());
    }

    @Test
    void testAddWallAddTwoWallsIdenticalInPosition() {
        //setup
        Wall wallIdenticalInPositionToWall1 = new Wall(wall1.getX(), wall1.getY());
        int originalWallNumber = gameWithOnlyDefaultWallsAndPlayerTank.getWalls().size();
        assertFalse(wall1.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertFalse(wallIdenticalInPositionToWall1.wallWithIdenticalPropertyContained(
                gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        //execute
        gameWithOnlyDefaultWallsAndPlayerTank.addWall(wall1);
        gameWithOnlyDefaultWallsAndPlayerTank.addWall(wallIdenticalInPositionToWall1);
        //check
        assertTrue(wall1.wallWithIdenticalPropertyContained(gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertTrue(wallIdenticalInPositionToWall1.wallWithIdenticalPropertyContained(
                gameWithOnlyDefaultWallsAndPlayerTank.getWalls()));
        assertEquals(originalWallNumber + 1, gameWithOnlyDefaultWallsAndPlayerTank.getWalls().size());
    }

    @Test
    void testAddBulletOneTime() {
        //setup - creates a tank game with a player tank without any bullet
        PlayerTank newPlayerTank = new PlayerTank(P_TANK_X, P_TANK_Y, P_TANK_GA);
        gameWithAllElements = new TankGame("game1", newPlayerTank, enemyTanks, walls);
        //execute
        gameWithAllElements.getPlayerTank().addBullet(collidingBullet);
        //check
        assertEquals(1, gameWithAllElements.getPlayerTank().getBullets().size());
        assertTrue(gameWithAllElements.getPlayerTank().getBullets().contains(collidingBullet));
    }

    @Test
    void testAddBulletTwoTimes() {
        //setup - creates a tank game with a player tank without any bullet
        PlayerTank newPlayerTank = new PlayerTank(P_TANK_X, P_TANK_Y, P_TANK_GA);
        gameWithAllElements = new TankGame("game1", newPlayerTank, enemyTanks, walls);
        //execute
        gameWithAllElements.getPlayerTank().addBullet(collidingBullet);
        gameWithAllElements.getPlayerTank().addBullet(nonCollidingBullet);
        //check
        assertEquals(2, gameWithAllElements.getPlayerTank().getBullets().size());
        assertTrue(gameWithAllElements.getPlayerTank().getBullets().contains(collidingBullet));
        assertTrue(gameWithAllElements.getPlayerTank().getBullets().contains(nonCollidingBullet));
    }

    @Test
    void testToJson() {
        //execute & setup
        JSONObject jsonObject = gameWithAllElements.toJson();
        JSONObject jsonPlayerTank = jsonObject.getJSONObject("playerTank");
        JSONArray jsonEnemyTankArray = jsonObject.getJSONArray("enemyTanks");
        JSONArray jsonWalls = jsonObject.getJSONArray("walls");
        //check
        assertEquals(gameWithAllElements.getName(), jsonObject.getString("name"));
        assertTrue(equivalentJsonAndTankObjects(gameWithAllElements.getPlayerTank(), //playerTank
                jsonPlayerTank));
        for (int i=0; i < jsonEnemyTankArray.length(); i++) { //enemyTanks
            EnemyTank enemyTank = enemyTanks.get(i);
            JSONObject jsonEnemyTank = jsonEnemyTankArray.getJSONObject(i);
            assertTrue(equivalentJsonAndTankObjects(enemyTank, jsonEnemyTank));
        }
        for (int i=0; i < jsonWalls.length(); i++) { //walls
            Wall individualWall = walls.get(i);
            JSONObject jsonWall = jsonWalls.getJSONObject(i);
            assertTrue(equivalentJsonAndWallObjects(individualWall, jsonWall));
        }
    }

    //EFFECTS: returns true if the given tank in JSON and Tank object have equivalent values for
    // its parameters
    private boolean equivalentJsonAndTankObjects(Tank comparedTank, JSONObject jsonTank) {
        boolean bulletListContainsIdenticalBullets = true;
        for (int i=0; i < comparedTank.getBullets().size(); i++) {
            Bullet individualBullet = comparedTank.getBullets().get(i);
            JSONObject jsonBullet = jsonTank.getJSONArray("bullets").getJSONObject(i);
            if (! ((individualBullet.getX() == jsonBullet.getInt("posX"))
                    && (individualBullet.getY() == jsonBullet.getInt("posY"))
                    && (individualBullet.getVelX() == jsonBullet.getInt("velX"))
                    && (individualBullet.getVelY() == jsonBullet.getInt("velY")))) {
                bulletListContainsIdenticalBullets = false;
            }
        }
        return ((comparedTank.getX() == jsonTank.getInt("posX"))
                && (comparedTank.getY() == jsonTank.getInt("posY"))
                && (comparedTank.getGunAngle() == jsonTank.getInt("gunAngle"))
                && (comparedTank.getBullets().size() ==  jsonTank.getJSONArray("bullets").length())
                && bulletListContainsIdenticalBullets);
    }

    //EFFECTS: returns true if the given wall in JSON and Wall object have equivalent values for
    // its parameters
    private boolean equivalentJsonAndWallObjects(Wall comparedWall, JSONObject jsonWall) {
        Wall wallFromJsonWall = new Wall(jsonWall.getInt("posX"), jsonWall.getInt("posY"));
        return comparedWall.hasIdenticalProperties(wallFromJsonWall);
    }

}
