package ui;

/*
Class representing the menu panel which displays the choices for the game, such as
closing the frame, saving the game and loading a game

LAST REVISED: 03/31/2022
 */

import model.tankgame.TankGame;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private final JsonReader saveReader;
    private final JsonReader initialReader;
    private final JsonWriter writer;
    private final PlayTankGame playTankGame;

    //EFFECTS: creates a menu panel that displays choices such as closing
    // the frame, saving the game and loading a game
    public MenuPanel(PlayTankGame playTankGame) {
        this.playTankGame = playTankGame;
        String fileAddress = PlayTankGame.SAVE_FILE_ADDRESS;
        String initialAddress = PlayTankGame.INITIAL_FILE_ADDRESS;
        setPreferredSize(new Dimension(TankGame.WIDTH, TankGame.HEIGHT));
        setBackground(new Color(215, 206, 206));
        setLayout(new FlowLayout());

        saveReader = new JsonReader(fileAddress);
        initialReader = new JsonReader(initialAddress);
        writer = new JsonWriter(fileAddress);
        addButtons();
    }

    //MODIFIES: this
    //EFFECTS: adds all buttons to be added to the panel:
    // SAVE - LOAD - CLOSE
    private void addButtons() {
        addResetButton();
        addSaveButton();
        addLoadButton();
        addCloseButton();
    }

    //MODIFIES: this
    //EFFECTS: adds a reset button to the panel which allows the player to play the game
    // from default state
    private void addResetButton() {
        JButton resetButton = new JButton("RESET TO DEFAULT MAP");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 40));
        resetButton.addActionListener(e -> loadGameFromGivenReader(initialReader));

        add(resetButton);
    }

    //MODIFIES: this
    //EFFECTS: adds a save button to the panel
    private void addSaveButton() {
        JButton saveButton = new JButton("SAVE THE CURRENT GAME");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 40));
        saveButton.addActionListener(e -> saveGame());

        add(saveButton);
    }

    //MODIFIES: this
    //EFFECTS: saves the current game state
    private void saveGame() {
        try {
            writer.open();
            writer.write(playTankGame.getTankGame());
            writer.close();
            System.out.println("The current game state has been saved!");
        } catch (FileNotFoundException e) {
            System.err.println("The file to be written to could not be opened for some reason!");
        }
    }

    //MODIFIES: this
    //EFFECTS: adds a load button to the panel
    private void addLoadButton() {
        JButton loadButton = new JButton("LOAD THE SAVED GAME");
        loadButton.setFont(new Font("Arial", Font.PLAIN, 40));
        loadButton.addActionListener(e -> loadGameFromGivenReader(saveReader));

        add(loadButton);
    }

    //MODIFIES: this
    //EFFECTS: loads the currently file-saved game from the given reader
    public void loadGameFromGivenReader(JsonReader r) {
        try {
            TankGame newGame = r.read();
            this.playTankGame.setTankGame(newGame);
            this.playTankGame.getGamePanel().setUp();
            this.playTankGame.resetTankGameKeyBoardCommands();
            System.out.println("The saved game state has been loaded!");
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file!");
        } catch (JSONException e) {
            System.err.println("Something went wrong converting the file into a JSON!");
        }
    }

    //MODIFIES: this
    //EFFECTS: adds a close button to the menu panel
    private void addCloseButton() {
        JButton closeButton = new JButton("CLOSE THIS GAME");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 40));
        closeButton.addActionListener(e -> playTankGame.exitProcedure());

        add(closeButton);
    }

}
