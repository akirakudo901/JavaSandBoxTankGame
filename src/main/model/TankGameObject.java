package model;

/*
Class representing a general tank game object upon which different objects could be built.
Used so that this type of object can be used to calculate collisions with walls.
It is extending a JLabel, given that every TankGameObject will be displayed as a JLabel.
Has the basic parameters of:
- An X position on the screen (inherited from Component, indicating the TOP LEFT CORNER of the object)
- A Y position on the scree (inherited from Component, indicating the TOP LEFT CORNER of the object)
- A width
- A height

LAST REVISED: 03/31/2022
 */

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

//fixme might not have to extend a jlabel, but rather just have its top left corner pos
public abstract class TankGameObject extends JLabel implements Writable {
    public static final int WIDTH = -1;
    public static final int HEIGHT = -1;
    //A set color for each TankGameObject type

    //EFFECTS: Creates a new TankGameObject with the given positions X and Y, and the width and height
    // unique of this object's class
    public TankGameObject(int posX, int posY) {
        setLocation(posX, posY);
        setSize(getPreferredSize());
    }

    //EFFECTS: convert this specific object into its JSON Object form
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("posX", this.getX());
        jsonObject.put("posY", this.getY());
        return jsonObject;
    }

    //EFFECTS: return true if the given object has the same parameters as this object
    public boolean hasIdenticalProperties(TankGameObject tankGameObject) {
        return ((this.getX() == tankGameObject.getX())
                && (this.getY() == tankGameObject.getY()));
    }

    //EFFECTS: returns true if this object is overlapping with the given tank game object
    public boolean isCollidingWith(TankGameObject tankGameObject) {
        //tank game object's left end to the left of this object's right end
        return ((tankGameObject.getLeftEdgeX() <= getRightEdgeX())
                //tank game object's right end to the right of this object's left end
                && (tankGameObject.getRightEdgeX() >= getLeftEdgeX())
                //tank game object's top end above this object's bottom end
                && (tankGameObject.getTopEdgeY() <= getBottomEdgeY())
                //tank game object's bottom end below this object's top end
                && (tankGameObject.getBottomEdgeY() >= getTopEdgeY()));
    }

    public abstract int getTopEdgeY();

    public abstract int getBottomEdgeY();

    public abstract int getLeftEdgeX();

    public abstract int getRightEdgeX();

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract Color getColor();
}