package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
/**
 * The GameToolbar class represents the toolbar displaying player information during the game.
 * HUD: includes the player's heart count, key collection status, and shot count.
 */
public class GameToolbar {
    private int hearts;
    private int shots;
    private boolean keyCollected;
    private boolean liveCollected;
    private final TextureRegion fullHeartTexture = new TextureRegion(new Texture(Gdx.files.internal("objects.png")), 64, 2, 14, 14);
    private final TextureRegion emptyKey = new TextureRegion(new Texture(Gdx.files.internal("character.png")), 173, 129, 41, 64);
    private final TextureRegion fullKey = new TextureRegion(new Texture(Gdx.files.internal("character.png")), 130, 128, 41, 64);
    private final TextureRegion shotCount = new TextureRegion(new Texture(Gdx.files.internal("objects.png")), 306, 161, 55, 78);

    private BitmapFont font;

    /**
     * Constructor for a GameToolBar with default values
     */
    public GameToolbar() {
        this.hearts = 3; // Character starts with 3 lives
        this.keyCollected = false;
        this.liveCollected= false;
        this.shots=0;


        font = new BitmapFont();
        font.setColor(Color.WHITE);
        BitmapFont.BitmapFontData fontData = font.getData();

        // Set the new scale
        fontData.setScale(30 / fontData.capHeight);


    }
    //Getters and Setters
    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public void setKeyCollected(boolean keyCollected) {
        this.keyCollected = keyCollected;
    }

    /**
     * Renders toolbar on the screen
     * @param spriteBatch for rendering
     * @param camera OrthographicCamera for the game
     * @param player information
     */
    public void render(SpriteBatch spriteBatch, OrthographicCamera camera, Player player) {
       //Rendering logic for hearts
        for (int i = 0; i < hearts; i++) {
            spriteBatch.draw(fullHeartTexture, camera.position.x - camera.viewportWidth / 2 + 10 + i*74, camera.position.y + camera.viewportHeight / 2 - 74, 64, 64);

        }
        //font.draw(spriteBatch, "X: " + (int) player.getCharacterX()/GameMap.TILE_SIZE + " Y: "+(int) player.getCharacterY()/GameMap.TILE_SIZE, camera.position.x - camera.viewportWidth / 2 + 500, camera.position.y + camera.viewportHeight / 2 - 20);
        // Draw key collected status
        if (!keyCollected) {
            spriteBatch.draw(emptyKey, camera.position.x + camera.viewportWidth / 2 - 60, camera.position.y + camera.viewportHeight / 2 - 74, 41, 64);
        }else {
            spriteBatch.draw(fullKey, camera.position.x + camera.viewportWidth / 2 - 60, camera.position.y + camera.viewportHeight / 2 - 74, 41, 64);
        }
        //Draw shot count
        //font.draw(spriteBatch, "Key Collected: " + (keyCollected ? "Yes" : "No"), camera.position.x - camera.viewportWidth / 2 + 150, camera.position.y + camera.viewportHeight / 2 - 20);
        spriteBatch.draw(shotCount, camera.position.x + camera.viewportWidth / 2 - 180, camera.position.y + camera.viewportHeight / 2 - 74,45,64 );
        font.draw(spriteBatch, ""+shots,camera.position.x + camera.viewportWidth / 2 - 130, camera.position.y + camera.viewportHeight / 2 - 25);
    }
    // Rest of the getters and setters
    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public void dispose() {
        font.dispose();
    }
}
