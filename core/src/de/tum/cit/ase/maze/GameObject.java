package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The GameObject class represents a basic game object with position and texture
 * This super class is responsible for the game objects and entities.
 * Game objects have two constructors: 1st with coordinates and texture, and 2nd only with coordinates
 */
public class GameObject {
    private float x;
    private float y;
    TextureRegion texture;

    /**
     * First constructor of GameObject with position and texture
     * @param x
     * @param y
     * @param texture for the visual representation of the object
     */
    public GameObject(float x, float y, TextureRegion texture) {
        this.x = x;
        this.y = y;
        this.texture=texture;
    }

    /**
     * Second constructor of GameObject with specified coordinates
     * @param x
     * @param y
     */
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
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

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }
}
