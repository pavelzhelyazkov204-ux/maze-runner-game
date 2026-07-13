package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Gunshot class represents the collectable gunshots in the game
 * Key-pair coordinate is "7".
 * ExtendExtends GameObject class, providing a position and texture.
 */
public class Gunshot extends GameObject{
     private boolean collected;

    //Constructors

    /**
     * First constructor with the specified coordinates with the given texture region
     * @param x
     * @param y
     * @param texture
     */
    public Gunshot(float x, float y, TextureRegion texture) {
        super(x, y, texture);

    }

    /**
     * Second constructor with coordinates and status
     * @param x
     * @param y
     * @param collected whether the gunshot was collected or not.
     */
    public Gunshot(float x, float y, boolean collected) {
        super(x, y);
        this.collected = false;
    }

    /**
     * Checks if the gunshot has been collected.
     * @return true if the gunshot has been collected, false otherwise.
     */
    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    /**
     * Marks gunshot as collected
     */
    //method when gunshots are collected
    public void collect() {
        collected = true;
    }
}

