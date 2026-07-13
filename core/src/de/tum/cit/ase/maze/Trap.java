package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * The Trap class represents a trap object in the game.
 * key-pair coordinate is "3".
 * Static traps are deleted after intersection (refer to GameScreen class)
 * It extends GameObject and uses a static trap texture for all trap instances.
 */
public class Trap extends GameObject{
    public static final TextureRegion TRAP_TEXTURE = new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 16, 192, 16, 16);
    public Trap(float x, float y) {
        super(x, y);
    }
}
