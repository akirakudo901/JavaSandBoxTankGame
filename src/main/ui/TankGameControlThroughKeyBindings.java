package ui;

/*
Class representing an object allowing control of the tank game state through key bindings.
Being created from the game panel component and a tank game object, the user could
then add specific key bindings on the game panel component which will send set controls
to the tank game object when called.
This class tracks all keys pressed (which are keys pressed already but not released yet), and
sends commands according to those keys to the given tank game when the method
"executePressedKeyCommands" is called.

ACKNOWLEDGEMENT:
THIS CODE HAS BEEN EXTENSIVELY BASED ON THE "MotionWithKeyBindings" CLASS PROVIDED
BY ROB CAMICK. I APPRECIATE THE WISDOM GAINED FROM THIS CODE.
THE CODE CAN BE FOUND AS OF 2022/03/31 AT THE BOTTOM OF THE FOLLOWING PAGE:
https://tips4java.wordpress.com/2013/06/09/motion-using-the-keyboard/

LAST REVISED: 03/31/2022
 */

import model.tankgame.TankGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TankGameControlThroughKeyBindings {
    private static final String PRESSED = "-PRESSED";
    private static final String RELEASED = "-RELEASED";

    private final JComponent component;
    private final TankGame gameCurrentlyPlayed;
    private final Set<String> allKeys;
    private final Set<String> pressedKeys;
    private final Map<String, String> pressedKeyToActionMap;
    //A map for each action to its cool down, shown as two integers:
    // [0] = length of a full cool down, and [1] = frames before cool down is over
    private final Map<String, int[]> pressedKeyToCoolDownMap;

    //EFFECTS: creates a new key binding controller on the given panel from which actions can be
    // invoked on the given tank game
    public TankGameControlThroughKeyBindings(JComponent component, TankGame gameCurrentlyPlayed) {
        this.component = component;
        this.gameCurrentlyPlayed = gameCurrentlyPlayed;
        this.allKeys = new HashSet<>();
        this.pressedKeys = new HashSet<>();
        this.pressedKeyToActionMap = new HashMap<>();
        this.pressedKeyToCoolDownMap = new HashMap<>();
    }

    //MODIFIES: this
    //EFFECTS: pairs the given key name and command together, and sets the key with given name to
    // be tracked on whether it is pressed or not
    public void addKeyAction(String name, String commandToTankGame, int keyEvent, int coolDown) {
        this.allKeys.add(name);
        this.pressedKeyToActionMap.put(name, commandToTankGame);
        this.pressedKeyToCoolDownMap.put(name, new int[] {coolDown, 0});
        addPressedTankGameAction(name, keyEvent);
        addReleasedTankGameAction(name, keyEvent);
    }

    //MODIFIES: this
    //EFFECTS: removes the KeyAction binding to this component with the given name & keyEvent
    public void clearKeyAction(String name, int keyEvent) {
        this.allKeys.remove(name);
        this.pressedKeyToActionMap.remove(name);
        this.pressedKeyToCoolDownMap.remove(name);

        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(keyEvent, 0);
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(pressedKeyStroke, null);
        component.getActionMap().put(name, null);
    }

    //MODIFIES: tank game
    //EFFECTS: executes the according commands to the tank game for keys tracked to be "pressed"
    public void executePressedKeyCommands() {
        if (! gameCurrentlyPlayed.isGameOver()) {
            //execute every command in the pressedKeys list
            for (String key : this.allKeys) {
                boolean coolDownOver = handleCoolDown(key);

                if (this.pressedKeys.contains(key) && coolDownOver) {
                    String actionCorrespondingToKey = this.pressedKeyToActionMap.get(key);
                    this.gameCurrentlyPlayed.handlePlayerTank(actionCorrespondingToKey);
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: adds given frames to the listed actions' cool downs
    public void addCoolDownFramesToAllActions(Set<String> actionNames, int coolDownLength) {
        for (String action : actionNames) {
            addCoolDownFramesToAction(action, coolDownLength);
        }
    }

    //MODIFIES: this
    //EFFECTS: adds given frames to the action's cool down if there; or do nothing otherwise
    public void addCoolDownFramesToAction(String actionName, int coolDownLength) {
        if (this.pressedKeyToCoolDownMap.containsKey(actionName)) {
            int[] coolDownArray = this.pressedKeyToCoolDownMap.get(actionName);
            coolDownArray[1] += coolDownLength;
        }
    }

    //EFFECTS: return the pressed keys at that moment
    public Set<String> getPressedKeys() {
        return this.pressedKeys;
    }

    //MODIFIES: this
    //EFFECTS: update the cool down state of the given key and returns true if the cool down
    // was over; or return false if the given key did not exist
    private boolean handleCoolDown(String key) {
        if (! this.pressedKeyToCoolDownMap.containsKey(key)) {
            return false;
        }

        int[] coolDownArray = this.pressedKeyToCoolDownMap.get(key);
        int coolDown = coolDownArray[1];

        if (coolDown == 0) {
            if (this.pressedKeys.contains(key)) {
                coolDownArray[1] = coolDownArray[0];
            }
            return true;
        } else {
            coolDownArray[1] = coolDownArray[1] - 1;
            return false;
        }
    }

    //MODIFIES: this
    //EFFECTS: adds one Action object to this component which tracks whether the given key has been
    // pressed or not
    private void addPressedTankGameAction(String name, int keyEvent) {
        KeyAction keyAction = new KeyAction(name, false);

        String pressedKeyName = name + PRESSED;

        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(keyEvent, 0);
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(pressedKeyStroke, pressedKeyName);
        component.getActionMap().put(pressedKeyName, keyAction);
    }

    //MODIFIES: this
    //EFFECTS: adds one Action object to this component which tracks whether the given key has been
    // released or not
    private void addReleasedTankGameAction(String name, int keyEvent) {
        KeyAction keyAction = new KeyAction(name, true);

        String releasedKeyName = name + RELEASED;

        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(keyEvent, 0, true);
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(pressedKeyStroke, releasedKeyName);
        component.getActionMap().put(releasedKeyName, keyAction);
    }

    /*
    Class that implements a key action, which reacts to either the given key being:
     - pressed (onKeyRelease = false) to add the key to pressedKeys, or
     - released (onKeyRelease = true) to remove the key from pressedKeys
     */
    private class KeyAction extends AbstractAction implements ActionListener {
        private final String name;
        private final boolean onKeyRelease;

        //EFFECTS: initiates a KeyAction object with given name that matches a keyboard input's
        // press / release event (onKeyRelease being false / true respectively) to either
        // respectively add or remove the key on pressedKeys
        public KeyAction(String name, boolean onKeyRelease) {
            super(name);
            this.name = name;
            this.onKeyRelease = onKeyRelease;
        }

        //EFFECTS: add/remove key to/from pressedKeys according to onKeyRelease being
        // false/true respectively
        @Override
        public void actionPerformed(ActionEvent e) {
            if (! onKeyRelease) {
                pressedKeys.add(this.name);
            } else {
                pressedKeys.remove(this.name);
            }
        }
    }

}