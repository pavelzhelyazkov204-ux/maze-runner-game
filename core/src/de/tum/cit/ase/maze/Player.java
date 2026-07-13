package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * The Player class represents the player character in the game.
 * It manages the player's position, hearts, shots, and rendering logic.
 * Goes in hand with the Enemy class.
 */
public class Player {
    private float characterX;
    private float characterY;
    private int hearts;
    private int shots;
    private final TextureRegion gunshotLeft = new TextureRegion(new Texture(Gdx.files.internal("objects.png")), 302, 92, 64, 41);

    private Animation<TextureRegion> gunRight;
    //Constructor
    public Player(float characterX, float characterY) {
        this.characterX = characterX;
        this.characterY = characterY;
        hearts=3;
        shots=0;
    }
    //Getters and setters
    public float getCharacterX() {
        return characterX;
    }

    public void setCharacterX(float characterX) {
        this.characterX = characterX;
    }

    public float getCharacterY() {
        return characterY;
    }

    public void setCharacterY(float characterY) {
        this.characterY = characterY;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    /**
     * Draws the player character based on the input and game state
     *
     * @param batch for rendering
     * @param game MazeRunnerGame instance
     * @param stateTime for the animation
     * @param angle angle of the gunshot animation
     * @param right true if player is facing right
     * @param enemies list of enemies in the game
     * @param closest enemy to the player
     */
    public void drawCharacter(SpriteBatch batch, MazeRunnerGame game, float stateTime, float angle, boolean right, ArrayList<Enemy> enemies, Enemy closest) {
        //Rendering logic based on player input and game state
        if (Gdx.input.isKeyPressed(Input.Keys.Z)&&getShots()>=0&&!enemies.isEmpty()&&closest!=null) {
            batch.draw(game.getCharacterDownAnimation().getKeyFrame(stateTime, true),
                    characterX, characterY, 64, 128);

            if(right){
                batch.draw(game.getGunRight().getKeyFrame(stateTime, true),
                        characterX, characterY,0,0, 64, 41,1,1,angle);
            }else {
                batch.draw(game.getGunLeft().getKeyFrame(stateTime,true),
                        characterX, characterY,0,0, 64, 41,1,1,angle);
            }

        }else{
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            batch.draw(game.getCharacterLeftAnimation().getKeyFrame(stateTime, true),
                    characterX, characterY, 64, 128);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            batch.draw(game.getCharacterRightAnimation().getKeyFrame(stateTime, true),
                    characterX, characterY, 64, 128);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            batch.draw(game.getCharacterDownAnimation().getKeyFrame(stateTime, true),
                    characterX, characterY, 64, 128);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            batch.draw(game.getCharacterUpAnimation().getKeyFrame(stateTime, true),
                    characterX, characterY, 64, 128);
        } else {
            batch.draw(game.getCharacterStillAnimation().getKeyFrame(stateTime, true),
                    characterX, characterY, 64, 128);
        }
        }
    }
}
