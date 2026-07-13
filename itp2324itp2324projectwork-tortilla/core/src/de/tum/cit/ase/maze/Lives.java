package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * The Lives class represents a collectable life in the game.
 *  key-pair coordinate is "6".
 * It extends GameObject and includes information about the life's position and whether it has been collected.
 */
public class Lives extends GameObject {
    private boolean collected;
    //Constructors

    /**
     * Constructs lives with coordinates and texture
     * @param x
     * @param y
     * @param texture
     */
    public Lives(float x, float y, TextureRegion texture) {
        super(x, y, texture);
    }

    /**
     * Constructs lives with coordinates and status
     * @param x
     * @param y
     * @param collected whether the life has been collected or not.
     */
    public Lives(float x, float y, boolean collected) {
        super(x, y);
        this.collected = false;

    }
    //Getters and setters

    public boolean isCollected() {
        return collected;
    }

    /**
     * Sets the collected status of the life pickup.
     * @param collected
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
    //method when lives are collected
    public void collect() {
        collected = true;
    }
}
