package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * EntryDoor class represents the entry point in the game
 * key-pair coordinate is "1".
 * Extends GameObject class, providing a position and texture.
 */
public class EntryDoor extends GameObject{
    /**
     * Constructor with coordinates
     * @param x
     * @param y
     */
    public EntryDoor(float x, float y) {
        super(x,y,new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 32, 96, 16, 16));
    }
}
