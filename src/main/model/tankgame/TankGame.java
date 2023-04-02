package model.tankgame;

/*
Class representing the tank game that will be taking place.
Manges:
- the movement of:
1) all tanks, ally or foe
2) all bullets, ally or foe
- the collision of:
1) tanks and tanks
2) tanks and a wall
3) bullets and bullets
4) bullets and a wall
5) bullets and a tank
Has fields:
- NUMBER_OF_WALLS_FITTING_VERTICALLY = the number of walls fitting vertically in game
- NUMBER_OF_WALLS_FITTING_HORIZONTALLY = the number of walls fitting horizontally in game
- HEIGHT = the height of the tank game
- WIDTH = the width of the tank game
- MAX_BOUNCE_COUNT = the maximum number of bounces a bullet in this game can have;
a bullet having bounced this many times is removed
- name = the name of the game that identifies it uniquely
- playerTank = the player's tank that will be manipulated on command
- enemyTanks = a list of all enemy tanks manipulated by the program
- walls = a list of all walls placed in this game

LAST REVISED: 03/31/2022
 */

import model.Bullet;
import model.Wall;
import model.Writable;
import model.eventlog.Event;
import model.eventlog.EventLog;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class TankGame implements Writable {
    public static final int NUMBER_OF_WALLS_FITTING_VERTICALLY = 20;
    public static final int NUMBER_OF_WALLS_FITTING_HORIZONTALLY = 15;
    //have to be multiples of the wall width / height to be reasonably displayed, as the walls
    // will be placed all around the map
    public static final int HEIGHT = Wall.HEIGHT * NUMBER_OF_WALLS_FITTING_VERTICALLY;
    public static final int WIDTH = Wall.WIDTH * NUMBER_OF_WALLS_FITTING_HORIZONTALLY;
    public static final int MAX_BOUNCE_COUNT = 3;
    public static final int MAX_BULLET_PER_TANK = 5;
    public static final int BULLET_SPEED = 5;

    public static final String UP = "w";
    public static final String DOWN = "s";
    public static final String RIGHT = "d";
    public static final String LEFT = "a";
    public static final String CLOCKWISE = "e";
    public static final String ANTI_CLOCKWISE = "q";
    public static final String FIRE = "b";

    private final String name;
    private PlayerTank playerTank;
    private List<EnemyTank> enemyTanks;
    private List<Wall> walls;
    private List<Bullet> bulletsWithoutTankOwner;

    private CollisionHandler collisionHandler;
    private MovementHandler movementHandler;

    private boolean isGameOver;
    private boolean isWon;

    //REQUIRES: tanks and enemy tanks as well as walls need to be placed outside the default wall placements
    // which are at the boundaries of the given map
    //EFFECTS: creates a tank game with a default placement of walls, the ally tank and enemy tanks
    // and with an empty list of bullets and player bullets
    public TankGame(String name, PlayerTank playerTank, List<EnemyTank> enemyTanks, List<Wall> walls) {
        this.name = name;
        setUp(playerTank, enemyTanks, walls);
    }

    //MODIFIES: this
    //EFFECTS: sets the game up to the given configuration, with the default walls implicit
    public void setUp(PlayerTank playerTank, List<EnemyTank> enemyTanks, List<Wall> walls) {
        this.playerTank = playerTank;
        this.enemyTanks = enemyTanks;
        this.walls = walls;
        this.bulletsWithoutTankOwner = new ArrayList<>();
        this.isGameOver = false;
        this.isWon = false;
        placeDefaultWalls();

        this.collisionHandler = new CollisionHandler(this.playerTank, this.enemyTanks,
                this.walls, this.bulletsWithoutTankOwner);
        this.movementHandler = new MovementHandler(this.collisionHandler);
    }

    //MODIFIES: this and all tank objects and all bullet objects
    //EFFECTS: update all the tank and bullets objects' state by one tick; tanks can fire bullets
    // as their action
    public void update() {
        movementHandler.moveAllEnemyTanks();
        movementHandler.moveAllBullets();
        collisionHandler.dealWithCollisionForBullets();
        dealWithCollisionBetweenTanksAndBulletsAndCheckEndCondition();
    }

    //MODIFIES: this and tank
    //EFFECTS: handles the player's tank according to input, rotates its tank gun
    // according to input, make it shoot some bullets, and then deals with wall collision
    public void handlePlayerTank(String playerInput) {
        movementHandler.movePlayerTank(playerInput);
        //fire bullet
        if (playerInput.equals(FIRE)) {
            fireBullet(playerTank);
            EventLog.getInstance().logEvent(new Event("Player tank fired a new bullet."));
        }
    }

    //MODIFIES: this
    //EFFECTS: adds a bullet at the given tank's current coordinate, aimed at the angle of
    //its tank gun, but only if the number of bullets in the map is less than MAX_BULLET_PER_TANK.
    // If bullet added successfully, return true, otherwise return false.
    // The bullet's speed is determined by BULLET_SPEED.
    public boolean fireBullet(Tank t) {
        if (t.getBullets().size() < MAX_BULLET_PER_TANK) {
            Bullet newFiredBullet = new Bullet(
                    t.getX() + t.getWidth() / 2
                            + (int) (Tank.WIDTH * 0.9 * cos(toRadians(t.getGunAngle()))),
                    t.getY() + t.getHeight() / 2
                            + (int) (Tank.HEIGHT * 0.9 * sin(toRadians(t.getGunAngle()))),
                    (int) (BULLET_SPEED * cos(toRadians(t.getGunAngle()))),
                    (int) (BULLET_SPEED * sin(toRadians(t.getGunAngle()))));
            t.addBullet(newFiredBullet);
            return true;
        } else {
            return false;
        }
    }

    //REQUIRES: width and height must be integer multiples of Wall.WIDTH and Wall.HEIGHT respectively
    //EFFECTS: returns a list of walls that needs to be put by default in a tank game with
    // given width and height
    public static List<Wall> getDefaultWallsGivenGameWidthAndHeight(int width, int height) {
        List<Wall> result = new ArrayList<>();
        int numberOfWallsFittingVertically = height / Wall.HEIGHT;
        int numberOfWallsFittingHorizontally = width / Wall.WIDTH;
        //left and right column
        for (int i = 0; i < numberOfWallsFittingVertically; i++) {
            Wall newLeftWall = new Wall(0, Wall.HEIGHT * i);
            Wall newRightWall = new Wall(Wall.WIDTH * (numberOfWallsFittingHorizontally - 1),
                    Wall.HEIGHT * i);
            result.add(newLeftWall);
            result.add(newRightWall);
        }
        //for bottom and top row
        for (int i = 1; i < (numberOfWallsFittingHorizontally - 1); i++) {
            Wall newBottomWall = new Wall(Wall.WIDTH * i, 0);
            Wall newTopWall = new Wall(Wall.WIDTH * i,
                    Wall.HEIGHT * (numberOfWallsFittingVertically - 1));
            result.add(newBottomWall);
            result.add(newTopWall);
        }
        return result;
    }

    //REQUIRES: newWall is not null
    //MODIFIES: this
    //EFFECTS: adds a new wall into the list of walls of this game if a wall with identical
    // coordinate is not there yet; do nothing otherwise.
    public void addWall(Wall newWall) {
        if (!newWall.wallWithIdenticalPropertyContained(this.walls)) {
            this.walls.add(newWall);
        }
    }

    //EFFECTS: initializes the tank game to the game state stored in the given string source
    public static TankGame initializeDefaultTankGame(String source) throws IOException {
        JsonReader initialGameReader = new JsonReader(source);
        TankGame defaultGame = initialGameReader.read();
        EventLog.getInstance().logEvent(new Event("Game initialized from source " + source));
        return defaultGame;
    }

    @Override
    //EFFECTS: returns a JSON object that represents this specific TankGame object
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("playerTank", playerTank.toJson());
        jsonObject.put("enemyTanks", enemyTanksToJson());
        jsonObject.put("walls", wallsToJson());
        return jsonObject;
    }

    public String getName() {
        return this.name;
    }

    public PlayerTank getPlayerTank() {
        return this.playerTank;
    }

    public List<EnemyTank> getEnemyTanks() {
        return this.enemyTanks;
    }

    public List<Wall> getWalls() {
        return this.walls;
    }

    public List<Bullet> getBulletsWithoutTankOwner() {
        return this.bulletsWithoutTankOwner;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public boolean isWon() {
        return this.isWon;
    }

    //MODIFIES: this and all Tank objects in game (EnemyTank & PlayerTank objects)
    //EFFECTS: removes every tank that collided with a bullet, removing the colliding bullet and
    // the tank that was hit. The bullets for the hit tanks will be moved to the "bullets without
    // tank owner" list. Then, it will set the condition of this game to either win or lose.
    private void dealWithCollisionBetweenTanksAndBulletsAndCheckEndCondition() {
        boolean won = collisionHandler.dealWithCollisionBetweenEnemyTanksAndBullets();
        boolean lost = collisionHandler.dealWithCollisionBetweenPlayerTankAndBullets();

        if (won) {
            this.isGameOver = true;
            this.isWon = true;
        }

        if (lost) {
            this.isGameOver = true;
        }
        //TODO ADD THE FUNCTIONALITY DEALING WITH THOSE WITHOUT TANK OWNER
    }

    //MODIFIES: this
    //EFFECTS: places the default walls that surround the play ground at its boundaries
    private void placeDefaultWalls() {
        List<Wall> defaultWalls = getDefaultWallsGivenGameWidthAndHeight(WIDTH, HEIGHT);
        for (Wall w : defaultWalls) {
            addWall(w);
        }
    }

    //EFFECTS: returns a JSON Array object of the EnemyTank objects
    private JSONArray enemyTanksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (EnemyTank enemyTank : this.enemyTanks) {
            jsonArray.put(enemyTank.toJson());
        }
        return jsonArray;
    }

    //EFFECTS: returns a JSON Array object of the Wall objects
    private JSONArray wallsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Wall w : this.walls) {
            jsonArray.put(w.toJson());
        }
        return jsonArray;
    }

}
