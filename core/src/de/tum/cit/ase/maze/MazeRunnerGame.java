package de.tum.cit.ase.maze;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.io.File;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game implements ApplicationListener {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin; //Skin for UI elements

    // Character animation different directions
    private Animation<TextureRegion> characterDownAnimation; //Downwards
    private Animation<TextureRegion> characterUpAnimation; //Upwards
    private Animation<TextureRegion> characterLeftAnimation; //Left
    private Animation<TextureRegion> characterRightAnimation; //Right
    private Animation<TextureRegion> characterStillAnimation; //Still
    private Animation<TextureRegion> gunRight; //Still
    private Animation<TextureRegion> gunLeft; //Still
    NativeFileChooser fileChooser;
    private Music backgroundMusic;
    private Music menuMusic;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser=fileChooser;

    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {

        spriteBatch = new SpriteBatch(); // Create SpriteBatch for rendering
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation

        // Play some background music
        // Background sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.3f);
        backgroundMusic.play();

        goToMenu("Adventures of Antonio Banderas"); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     *
     * @param message to display on the menu screen
     */
    public void goToMenu(String message) {
        this.setScreen(new MenuScreen(this,  message, fileChooser)); // Set the current screen to MenuScreen
        backgroundMusic.stop();
        menuMusic.play();
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     *
     * @param mapFile Filehandle of the map to be used in the game
     */
    public void goToGame(FileHandle mapFile) {
        // If the gameScreen is already created, reuse it
        menuMusic.stop();
        backgroundMusic.play();
        this.setScreen(new GameScreen(this, mapFile));

        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }

    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));
        Texture gunSheet = new Texture(Gdx.files.internal("objects.png"));

        int frameWidth = 16;
        int frameHeight = 24;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesStill = new Array<>(TextureRegion.class);
        Array<TextureRegion> gunshotRightFrames=new Array<>(TextureRegion.class);
        Array<TextureRegion> gunshotLeftFrames=new Array<>(TextureRegion.class);


        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFramesDown.add(new TextureRegion(walkSheet, col * frameWidth, 5, frameWidth, frameHeight));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesRight.add(new TextureRegion(walkSheet, col * frameWidth, 37, frameWidth, frameHeight));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesUp.add(new TextureRegion(walkSheet, col * frameWidth, 68, frameWidth, frameHeight));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesLeft.add(new TextureRegion(walkSheet, col * frameWidth, 101, frameWidth, frameHeight));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesStill.add(new TextureRegion(walkSheet, col * frameWidth, 263, frameWidth, frameHeight));
        }
        for (int col = 0; col < animationFrames; col++) {
            gunshotRightFrames.add(new TextureRegion(gunSheet, col * 88+393, 93, 88, 41));
        }
        for (int col = animationFrames-1; col >= 0; col--) {
            gunshotLeftFrames.add(new TextureRegion(gunSheet, col * 88+378, 145, 88, 41));
        }

        //Create animations for each direction with the specified frame duration
        characterDownAnimation = new Animation<>(0.1f, walkFramesDown);
        characterUpAnimation = new Animation<>(0.1f, walkFramesUp);
        characterLeftAnimation = new Animation<>(0.1f, walkFramesLeft);
        characterRightAnimation = new Animation<>(0.1f, walkFramesRight);
        characterStillAnimation = new Animation<>(0.1f, walkFramesStill);
        gunRight = new Animation<>(0.1f,gunshotRightFrames);
        gunLeft = new Animation<>(0.1f,gunshotLeftFrames);
    }

    /**
     * Resumes the game by calling the resume method of gameScreen class
     */
    public void resumeGame(){
        gameScreen.resume();
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    public Animation<TextureRegion> getCharacterStillAnimation() {
        return characterStillAnimation;
    }

    public Animation<TextureRegion> getGunRight() {
        return gunRight;
    }

    public Animation<TextureRegion> getGunLeft() {
        return gunLeft;
    }
}
