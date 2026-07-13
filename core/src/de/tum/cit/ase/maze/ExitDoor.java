package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * ExitDoor class represents the exit point in the game
 * key-pair coordinate is "2".
 * Extends GameObject class, providing a position and texture.
 */
public class ExitDoor extends GameObject{
    public ExitDoor(float x, float y, TextureRegion textureRegion) {
        super(x,y,textureRegion);
    }
}
