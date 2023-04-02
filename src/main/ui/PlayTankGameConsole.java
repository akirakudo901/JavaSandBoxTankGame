package ui;

/*
A class representing the console that will allow users to interact with the inner TankGame object.
Will allow the user to input specific commands:
(for now)
- TankGame.ANTI_CLOCKWISE for turning the gun of the tank anti-clockwise
- TankGame.CLOCKWISE for turning the gun of the tank clockwise
- ADD_BULLET for adding a bullet with fixed velocity at the player tank's coordinate
and to print the new state of the game, which includes:
1) The position of the player tank
2) The gun angle of the player tank
3) The position of the enemy tanks
4) The position and velocities of the bullet objects.
(Persistence):
This will also allow the user to
- save the state of the game once, or
- load it from the data to resume the game

ACKNOWLEDGEMENT:
THIS CODE HAS BEEN EXTENSIVELY BASED ON THE "TellerApp" CLASS IN THE "TellerApp" PROJECT
PROVIDED TO US AS EXAMPLE. I APPRECIATE THE WISDOM GAINED FROM THIS PROJECT.
THE PROJECT CAN BE FOUND AS OF 2022/03/31 AT:
https://github.students.cs.ubc.ca/CPSC210/TellerApp

LAST REVISED: 03/31/2022
 */

import model.*;
import model.tankgame.TankGame;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//I ACKNOWLEDGE THAT I BUILT THIS CODE BASED ON THE "TELLER APP" CODE.

public class PlayTankGameConsole {
    public static final String SHOW = "show";
    public static final String SAVE = "save";
    public static final String LOAD = "load";
    public static final String STOP = "stop";
    private static final String SAVE_FILE_ADDRESS = "./data/tankGameSaveFile.json";

    private Scanner input;
    private TankGame gameCurrentlyPlayed;
    private JsonReader reader;
    private JsonWriter writer;

    //EFFECTS: creates a new instance of the tank game which controls everything including
    // the ui side
    public PlayTankGameConsole() {
        runTankGame();
    }

    //MODIFIES: this
    //EFFECTS: executes the tank game, continuously taking player input and calling the
    // appropriate functions to deal with the input.
    public void runTankGame() {
        boolean done = false;
        String command;

        input = new Scanner(System.in);
        reader = new JsonReader(SAVE_FILE_ADDRESS);
        writer = new JsonWriter(SAVE_FILE_ADDRESS);
        initializeNewTankGame();

        while (!done) {
            System.out.println("Please input your next move! " + TankGame.CLOCKWISE + " - clockwise; "
                    + TankGame.ANTI_CLOCKWISE + " - anticlockwise; " + TankGame.FIRE
                    + " - add a bullet;");
            System.out.println(SHOW + " - show the current game state; " + SAVE + " - save the game; "
                    + LOAD + " - load the game from save; " + STOP + " - terminate the game.");
            command = input.next();

            boolean[] doneAndValid = processCommandAboutGame(command);
            boolean valid = doneAndValid[0];
            done = doneAndValid[1];

            if (!done && !valid) {
                boolean correctCommandReceived = processCommandInGame(command);
                if (correctCommandReceived) {
                    printCurrentGameSituation();
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: takes string commands related to the game itself and process accordingly:
    // this.SHOW for showing the current game state, this.SAVE for saving the game,
    // this.LOAD for loading the game from save file, and this.STOP for terminating the game,
    // or nothing otherwise.
    // For the first element of the return:
    // - If the input is a valid input (any of the above), return true. Otherwise, return false.
    // For the second element of the return:
    // - If the input should result in the game being over, return true. Otherwise, return false.
    private boolean[] processCommandAboutGame(String command) {
        switch (command) {
            case SHOW:
                printCurrentGameSituation();
                return new boolean[] {true, false};
            case SAVE:
                saveGame();
                return new boolean[] {true, false};
            case LOAD:
                loadGame();
                return new boolean[] {true, false};
            case STOP:
                saveGameWhileAsking();
                System.out.println("The game is terminating.");
                return new boolean[] {true, true};
            default:
                return new boolean[] {false, false};
        }
    }

    //MODIFIES: this
    //EFFECTS: loads the currently file-saved game
    private void loadGame() {
        try {
            gameCurrentlyPlayed = reader.read();
            System.out.println("The saved game state has been loaded!");
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file!");
        } catch (JSONException e) {
            System.err.println("Something went wrong converting the file into a JSON!");
        }
    }

    //MODIFIES: this
    //EFFECTS: asks for user input for whether to save the game, and save accordingly:
    // save is "YES" is the input, do not save if "NO".
    private void saveGameWhileAsking() {
        System.out.println("Do you want to save this game? \"YES\" - save, or \"NO\" - don't save.");
        String command = input.next();
        if (command.equals("YES")) {
            saveGame();
        } else if (command.equals("NO")) {
            System.out.println("Game has not been saved.");
        } else {
            saveGameWhileAsking();
        }
    }

    //MODIFIES: this
    //EFFECTS: saves the current game state
    private void saveGame() {
        try {
            writer.open();
            writer.write(gameCurrentlyPlayed);
            writer.close();
            System.out.println("The current game state has been saved!");
        } catch (FileNotFoundException e) {
            System.err.println("The file to be written to could not be opened for some reason!");
        }
    }

    //MODIFIES: this
    //EFFECTS: takes string commands and process accordingly: TankGame.CLOCKWISE for turning the tank
    // gun clockwise, TankGame.ANTI_CLOCKWISE for anticlockwise, TankGame.FIRE to fire a bullet from the
    // player's tank, or nothing otherwise.
    // If a valid command is inputted, return true. Otherwise, return false.
    private boolean processCommandInGame(String command) {
        switch (command) {
            case TankGame.ANTI_CLOCKWISE:
                gameCurrentlyPlayed.handlePlayerTank(command);
                gameCurrentlyPlayed.update();
                System.out.println("Player's tank gun angle was rotated ANTIclockwise by " + Tank.ROTATE_SPEED
                        + " degree!");
                return true;
            case TankGame.CLOCKWISE:
                gameCurrentlyPlayed.handlePlayerTank(command);
                gameCurrentlyPlayed.update();
                System.out.println("Player's tank gun angle was rotated clockwise by " + Tank.ROTATE_SPEED
                        + " degree!");
                return true;
            case TankGame.FIRE:
                gameCurrentlyPlayed.handlePlayerTank(command);
                gameCurrentlyPlayed.update();
                System.out.println("Player tank fired a new bullet to angle "
                        + gameCurrentlyPlayed.getPlayerTank().getGunAngle() + " !");
                return true;
            default:
                System.out.println("You may have input an invalid command! Try again!");
                return false;
        }
    }

    //EFFECTS: prints out a description of the current tank game state; the player tank's position and gun angle;
    // the enemy tanks' positions and gun angles; and any bullets' position and x / y velocities
    private void printCurrentGameSituation() {
        System.out.println("Player's tank pos - X: " + gameCurrentlyPlayed.getPlayerTank().getX() + "; Y: "
                + gameCurrentlyPlayed.getPlayerTank().getY() + "; GA: "
                + gameCurrentlyPlayed.getPlayerTank().getGunAngle());
        for (Tank et : gameCurrentlyPlayed.getEnemyTanks()) {
            System.out.println("Enemies' tanks pos - X: " + et.getX() + "; Y: " + et.getY() + "; GA: "
                    + et.getGunAngle());
        }
        for (Bullet b : gameCurrentlyPlayed.getPlayerTank().getBullets()) {
            System.out.println("Bullets' pos - X: " + b.getX() + "; Y: " + b.getY() + "; velX: "
                    + b.getVelX() + "; velY: " + b.getVelY());
        }
    }

    //MODIFIES: this and TankGame
    //EFFECTS: initializes the tank game to the game state defined below
    private void initializeNewTankGame() {
        int playerTankX = TankGame.WIDTH / 2;
        int playerTankY = TankGame.HEIGHT / 2;
        int playerTankGA = 0;

        int enemy1TankX = TankGame.WIDTH / 4;
        int enemy1TankY = TankGame.HEIGHT / 4;
        int enemy1TankGA = 180;

        int enemy2TankX = TankGame.WIDTH / 4;
        int enemy2TankY = TankGame.HEIGHT / 4;
        int enemy2TankGA = 180;

        PlayerTank playerTank = new PlayerTank(playerTankX, playerTankY, playerTankGA);
        EnemyTank enemyTank1 = new EnemyTank(enemy1TankX, enemy1TankY, enemy1TankGA);
        EnemyTank enemyTank2 = new EnemyTank(enemy2TankX, enemy2TankY, enemy2TankGA);
        List<EnemyTank> enemyTanks = new ArrayList<>();
        enemyTanks.add(enemyTank1);
        enemyTanks.add(enemyTank2);
        Wall wall1 = new Wall(Wall.WIDTH * 3 + Wall.WIDTH / 2, Wall.HEIGHT * 3 + Wall.HEIGHT / 2);
        Wall wall2 = new Wall(Wall.WIDTH * 4 + Wall.WIDTH / 2, Wall.HEIGHT * 2 + Wall.HEIGHT / 2);
        List<Wall> walls = new ArrayList<>();
        walls.add(wall1);
        walls.add(wall2);
        gameCurrentlyPlayed = new TankGame("currently played", playerTank, enemyTanks, walls);
    }

}
