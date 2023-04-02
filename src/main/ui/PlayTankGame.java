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
THIS CODE HAS BEEN EXTENSIVELY BASED ON THE "SpaceInvaders" CLASS IN THE "B2-SpaceInvadersBase" PROJECT
PROVIDED TO US AS EXAMPLE. I APPRECIATE THE WISDOM GAINED FROM THIS PROJECT.
THE PROJECT CAN BE FOUND AS OF 2022/03/02 AT:
https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase

LAST REVISED: 03/31/2022
 */

import model.eventlog.EventLog;
import model.tankgame.TankGame;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashSet;

public class PlayTankGame extends JFrame {
    public static final String SAVE_FILE_ADDRESS = "./data/tankGameSaveFile.json";
    public static final String INITIAL_FILE_ADDRESS = "./data/tankGameInitialGameFile.json";
    public static final int INTERVAL = 15;
    public static final int RESET_KEY_EVENT = KeyEvent.VK_R;
    public static final int SAVE_KEY_EVENT = KeyEvent.VK_ESCAPE;
    public static final int GENERAL_COOL_DOWN_FOLLOWING_FIRE = 2;
    public static final int FIRE_COOL_DOWN = 15;

    private static final String UP_ACTION_NAME = "UP";
    private static final String DOWN_ACTION_NAME = "DOWN";
    private static final String LEFT_ACTION_NAME = "LEFT";
    private static final String RIGHT_ACTION_NAME = "RIGHT";
    private static final String ANTI_CLOCKWISE_ACTION_NAME = "ANTI-CLOCKWISE";
    private static final String CLOCKWISE_ACTION_NAME = "CLOCKWISE";
    private static final String FIRE_ACTION_NAME = "FIRE";
    private static final String RESET_ACTION_NAME = "RESET";

    private TankGame gameCurrentlyPlayed;
    private final GamePanel gp;
    private final MenuPanel mp;
    private Timer timer;
    private TankGameControlThroughKeyBindings control;
    private int previousPlayerBulletNumber = 0;

    //EFFECTS: creates a new instance of the tank game which controls everything including
    // the ui side
    public PlayTankGame() {
        super("Tank Game");
        setWindowClosingAction();
        setUndecorated(false);
        setLayout(new FlowLayout());

        initializeNewTankGame();
        gp = new GamePanel(this);
        gp.setVisible(true);
        mp = new MenuPanel(this);
        mp.setVisible(false);
        add(gp);
        add(mp);

        this.control = setTankGameKeyBoardCommands();
        setMenuPanelCommands();
        setGameResetCommand();

        pack();
        centreFrameOnScreen();
        setVisible(true);

        addTimer();
    }

    //EFFECTS: returns the tank game in this JFrame object
    public TankGame getTankGame() {
        return this.gameCurrentlyPlayed;
    }

    //EFFECTS: sets the tank game in this JFrame object to the given one
    public void setTankGame(TankGame newTankGame) {
        this.gameCurrentlyPlayed = newTankGame;
    }

    //EFFECTS: returns the game panel in this JFrame object
    public GamePanel getGamePanel() {
        return this.gp;
    }

    //MODIFIES: this
    //EFFECTS: opens the menu panel by painting it onto this frame
    public void switchPanelVisibility() {
        if (gp.isVisible()) {
            timer.stop();
            gp.setVisible(false);
            mp.setVisible(true);
            mp.repaint();
        } else if (mp.isVisible()) {
            timer.start();
            gp.setVisible(true);
            mp.setVisible(false);
            gp.repaint();
            executeInitialCountDown();
        }
    }

    //MODIFIES: this
    //EFFECTS: resets the TankGameControlThroughKeyBindings object by refreshing its instantiation and
    // redirecting its reference to the current game (needed each time the TankGame referred to by this
    // object changes)
    public void resetTankGameKeyBoardCommands() {
        control = new TankGameControlThroughKeyBindings(this.gp, gameCurrentlyPlayed);
        control.clearKeyAction(UP_ACTION_NAME, KeyEvent.VK_W);
        control.clearKeyAction(LEFT_ACTION_NAME, KeyEvent.VK_A);
        control.clearKeyAction(DOWN_ACTION_NAME, KeyEvent.VK_S);
        control.clearKeyAction(RIGHT_ACTION_NAME, KeyEvent.VK_D);

        control.clearKeyAction(ANTI_CLOCKWISE_ACTION_NAME, KeyEvent.VK_Q);
        control.clearKeyAction(CLOCKWISE_ACTION_NAME, KeyEvent.VK_E);

        control.clearKeyAction(FIRE_ACTION_NAME, KeyEvent.VK_SPACE);

        setTankGameKeyBoardCommands();
    }

    //MODIFIES: this
    //EFFECTS: disposes this JFrame, prints the event logs saved so far and
    // exit the system using status 0
    public void exitProcedure() {
        dispose();
        EventLog logToBePrinted = EventLog.getInstance();
        logToBePrinted.forEach(System.out::println);
        System.exit(0);
    }

    //MODIFIES: this
    //EFFECTS: execute the countdown that needs to be done each time that we start a new game or
    // move to the Menu Panel
    public void executeInitialCountDown() {
        this.timer.stop();
        gp.executeInitialCountDown();
    }

    //EFFECTS: start the timer that updates the game
    public void startGameTimer() {
        this.timer.start();
    }

    //MODIFIES: this
    //EFFECTS: locate the frame at the center of the desktop
    private void centreFrameOnScreen() {
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrDim.width - getWidth()) / 2, (scrDim.height - getHeight()) / 2);
    }

    //EFFECTS: creates a timer object which updates the game each INTERVAL milliseconds
    private void addTimer() {
        timer = new Timer(INTERVAL, ae -> {
            if (!gameCurrentlyPlayed.isGameOver()) {
                handleFireCoolDown();
                control.executePressedKeyCommands();
                gameCurrentlyPlayed.update();
            } else {
                timer.stop();
            }
            gp.repaint();

        });

        executeInitialCountDown();
    }

    //MODIFIES: this and TankGame
    //EFFECTS: initializes the tank game to the game state defined below
    private void initializeNewTankGame() {
        try {
            this.gameCurrentlyPlayed = TankGame.initializeDefaultTankGame(INITIAL_FILE_ADDRESS);
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file!");
        } catch (JSONException e) {
            System.err.println("Something went wrong converting the file into a JSON!");
        }
    }

    //MODIFIES: this
    //EFFECTS: adds the default KeyAction objects to the tank game:
    // - WASD for moving the tank
    // - Q and E for turing the tank gun (anti)clockwise
    // - SPACE for firing a bullet
    // and returns the tankGameControlThroughKeyBindings object
    private TankGameControlThroughKeyBindings setTankGameKeyBoardCommands() {
        control = new TankGameControlThroughKeyBindings(this.gp, gameCurrentlyPlayed);
        control.addKeyAction(UP_ACTION_NAME, TankGame.UP, KeyEvent.VK_W, 0);
        control.addKeyAction(LEFT_ACTION_NAME, TankGame.LEFT, KeyEvent.VK_A, 0);
        control.addKeyAction(DOWN_ACTION_NAME, TankGame.DOWN, KeyEvent.VK_S, 0);
        control.addKeyAction(RIGHT_ACTION_NAME, TankGame.RIGHT, KeyEvent.VK_D, 0);

        control.addKeyAction(ANTI_CLOCKWISE_ACTION_NAME, TankGame.ANTI_CLOCKWISE, KeyEvent.VK_Q, 0);
        control.addKeyAction(CLOCKWISE_ACTION_NAME, TankGame.CLOCKWISE, KeyEvent.VK_E, 0);

        control.addKeyAction(FIRE_ACTION_NAME, TankGame.FIRE, KeyEvent.VK_SPACE, FIRE_COOL_DOWN);

        return control;
    }

    //MODIFIES: tank game
    //EFFECTS: if FIRE was included in the pressed keys, adds GENERAL_COOL_DOWN_FOLLOWING_FIRE frames
    // as cool down to every action except FIRE itself
    private void handleFireCoolDown() {
        if (this.control.getPressedKeys().contains(FIRE_ACTION_NAME)
                && firedBulletSuccessfully()) {
            HashSet<String> actionsExceptFire = new HashSet<>();
            actionsExceptFire.add(UP_ACTION_NAME);
            actionsExceptFire.add(DOWN_ACTION_NAME);
            actionsExceptFire.add(RIGHT_ACTION_NAME);
            actionsExceptFire.add(LEFT_ACTION_NAME);
            actionsExceptFire.add(ANTI_CLOCKWISE_ACTION_NAME);
            actionsExceptFire.add(CLOCKWISE_ACTION_NAME);
            control.addCoolDownFramesToAllActions(actionsExceptFire, GENERAL_COOL_DOWN_FOLLOWING_FIRE);
        }
    }

    //MODIFIES: this
    //EFFECTS: returns true if the FIRE action executed in this frame successfully added the bullet
    // to the tank game, against maximum number of bullets
    private boolean firedBulletSuccessfully() {
        int storedPreviousNumber = this.previousPlayerBulletNumber;
        this.previousPlayerBulletNumber = this.gameCurrentlyPlayed.getPlayerTank().getBullets().size();
        return (storedPreviousNumber <= TankGame.MAX_BULLET_PER_TANK - 1);
    }

    //MODIFIES: gp
    //EFFECTS: adds a new menu opener action to the game panel and menu panel
    private void setMenuPanelCommands() {
        setMenuOpeningActionToJComponent(gp);
        setMenuOpeningActionToJComponent(mp);
    }

    //MODIFIES: component
    //EFFECTS: adds a new menu opener action to the given component
    private void setMenuOpeningActionToJComponent(JComponent component) {
        MenuOpeningHandler menuOpener = new MenuOpeningHandler();

        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(SAVE_KEY_EVENT, 0);
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(pressedKeyStroke, MenuOpeningHandler.NAME);
        component.getActionMap().put(MenuOpeningHandler.NAME, menuOpener);
    }

    //MODIFIES: gp
    //EFFECTS: adds a new command to the Game Panel, which reacts to the RESET_KEY_EVENT and
    // resets the currently played game to its initial state if it is already over
    private void setGameResetCommand() {
        Action resetActionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameCurrentlyPlayed.isGameOver()) {
                    initializeNewTankGame();
                    gp.setUp();
                    resetTankGameKeyBoardCommands();
                    executeInitialCountDown();
                }
            }
        };

        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(RESET_KEY_EVENT, 0);
        InputMap inputMap = this.gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(pressedKeyStroke, RESET_ACTION_NAME);
        gp.getActionMap().put(RESET_ACTION_NAME, resetActionListener);
    }

    //EFFECTS: sets a window listener object which monitors the closing of the frame
    // and execute the exitProcedure() method.
    private void setWindowClosingAction() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });
    }

    /*
    A class that represents the action corresponding to opening the menu in the game
     */
    private class MenuOpeningHandler extends AbstractAction implements ActionListener {
        private static final String NAME = "OPEN_MENU";

        //EFFECTS: initiates an action object which opens the menu panel on command
        private MenuOpeningHandler() {
            super(NAME);
        }

        //EFFECTS: opens the menu panel and stops the game from advancing for a while
        @Override
        public void actionPerformed(ActionEvent e) {
            switchPanelVisibility();
        }
    }

}
