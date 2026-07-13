package de.tum.cit.ase.maze;

/**
 * The Key class represents the collectable key in the game.
 * key-pair coordinate is "5".
 * It holds information about the key's position and whether it has been collected.
 *
 */
public class Key {
    private float x;
    private float y;
    private boolean collected;

    /**
     * Constructor with coordinates
     *
     * @param x
     * @param y
     * @param collected whether the key has been collected or not.
     */
    public Key(float x, float y, boolean collected) {
        this.x = x;
        this.y = y;
        this.collected = false;

    }
    //Getters and Setters
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    /**
     * Sets the collected status of the key.
     *
     * @return collected true to mark the key as collected, false otherwise.
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Marks the key as collected.
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
    //method when key is collected
    public void collect() {
        collected = true;
}
}
