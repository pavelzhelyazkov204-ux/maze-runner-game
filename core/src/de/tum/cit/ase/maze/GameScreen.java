package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * GameScreen class implements LibGDX Screen interface.
 * Serves as the main gameplay screen. Gets interaction from the user.
 * Handles rendering the game map, updating game logic, processing player input, and
 * managing game entities (referred in GameMap Class).
 * This class also includes methods for handling interactions, collecting items, and
 * showing player progress.
 * It manages the camera, toolbar (Refer to Toolbar Class), and audio elements.
 * GameScreen is responsible for coordinating the different aspects of the game and,
 * transitions between screens: game over or victory scenarios.
 */
public class GameScreen implements Screen {

    //Attributes
    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final Music gunCollectSound;
    private final SpriteBatch batch;
    private final GameMap gameMap;
    private final Sound gunSound;
    private float stateTime = 0f;
    private final Player player;

    //Music and sound effects
    private final Music walkingSound;
    private final Sound keyCollectSound;
    private final Music heartCollectSound;
    private final Music gameOverSound;
    private final Sound beartrapSound;
    private final Sound hurtSound;
    private final Sound winSound;

    private boolean exitUnlocked = false;
    private ArrayList<Key> keys;
    private ArrayList<Enemy> enemies;
    private final GameToolbar toolbar;
    private ArrayList<Lives> lives;
    private ArrayList<Gunshot> gunshots;
    private ArrayList<Trap> traps;
    private float cooldownTimer = 0f;
    private static final float COOLDOWN_DURATION = 2f;
    private float angle = 0;
    private boolean right=true;
    private Enemy removedEnemy;

    boolean paused=false;
    private PauseScreen pauseScreen;

    /**
     * Constructs a new GameScreen instance
     * @param game MazeRunnerGame instance
     * @param mapFile the file handle for the game map
     */
    public GameScreen(MazeRunnerGame game, FileHandle mapFile) {
        //Initialize the cursor and basic rendering components
        Gdx.input.setCursorCatched(true);
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        camera.zoom = 1f;
        gameMap = new GameMap(mapFile);
        keys = new ArrayList<>();
        enemies = new ArrayList<>();

        //Here are some initialization for fonts, players, enemies, key, and traps
        initializeKeys();
        font = game.getSkin().getFont("font");
        player=new Player(gameMap.getEntryDoor().getX() * 128 + 32,gameMap.getEntryDoor().getY() * 128);
        initializeEnemies();
        for (Enemy enemy: enemies){
            System.out.println(enemy.toString());
        }
        initializeTraps();

        //Initizalize music and sound effects
        walkingSound = Gdx.audio.newMusic(Gdx.files.internal("walking.mp3"));
        gunCollectSound = Gdx.audio.newMusic(Gdx.files.internal("gun_collect.mp3"));
        keyCollectSound = Gdx.audio.newSound(Gdx.files.internal("key_collect.mp3"));
        heartCollectSound = Gdx.audio.newMusic(Gdx.files.internal("LifeUp.ogg"));
        gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("GameOver.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("winSound.mp3"));
        gunSound = Gdx.audio.newSound(Gdx.files.internal("gunshot.wav"));
        beartrapSound = Gdx.audio.newSound(Gdx.files.internal("beartrap.wav"));
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.mp3"));
        exitUnlocked = false;
        toolbar = new GameToolbar();

        //InitializeLives and gunshots
        initializeLives();
        initializeGunshots();


    }

    /**
     * Initializes the list of traps based on game map
      */

    private void initializeTraps() {
        traps=gameMap.getTraps();
    }

    /**
     * Initializes the list of keys based on game map
     */
    private void initializeKeys() {
        keys = gameMap.getKeys();
    }

    /**
     * Initializes the list of enemies based on game map
     */
    private void initializeEnemies() {
        enemies=gameMap.getEnemies();
    }

    /**
     * Initializes the list of collectable lives based on game map
     */
    private void initializeLives() {
        lives = new ArrayList<>();
        lives = gameMap.getLives();
    }

    /**
     * Initializes the list of collectable gunshots based on game map
     */
    private void initializeGunshots() {
        gunshots=new ArrayList<>();
        gunshots = gameMap.getGunshots();
    }

    /**
     * Called when this screen becomes the current screen.
     * (unused in the current implementation)
     */
    @Override
    public void show() {
    //Unused method
    }

    /**
     * Renders the game screen
     * Clears the screen, updates game locig, and renders game elements.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if(!paused){
        //Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        //Updates and render game elements
        update(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        gameMap.render(batch, stateTime);
        handleExitUnlock();
        drawEnemies();
        player.drawCharacter(batch,game,stateTime,angle,right,enemies,removedEnemy);
        toolbar.render(batch, camera, player);
        batch.end();
        } else {
            //Handle pause state if it's needed.
        }
    }

    /**
     * Calculates the angle between the player's pistol and the closest enemy.
     *
     * @param enemy the one for which the angle is calculated
     */
    private void calculateAngle(Enemy enemy) {
        // Calculate the angle between the pistol and the closest enemy
        if(right) {
            angle = MathUtils.atan2Deg(-player.getCharacterY() - 41 + (enemy.getEnemyY() + 64), -player.getCharacterX() - 64 + enemy.getEnemyX() + 64);
        }else{
            angle = MathUtils.atan2Deg(player.getCharacterY() + 41 - (enemy.getEnemyY() + 64), player.getCharacterX() - (enemy.getEnemyX() + 64));

        }
        System.out.println("Angle1: "+angle); //For testing purposes
    }

    /**
     * Draws the enemies on the screen
     */
    private void drawEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.render(batch);

        }

    }

    /**
     * Resize method. (Unused in the current implementation)
     * @param i
     * @param i1
     */
    @Override
    public void resize(int i, int i1) {
        //Unused method
    }

    /**
     * Unused method in the current implementation
     */
    @Override
    public void hide() {
    }

    /**
     * Disposes of resources when the screen is no longer needed
     * Disposes of the SpriteBatch, fonts, sounds and the game map.
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        walkingSound.dispose();
        gunCollectSound.dispose();
        keyCollectSound.dispose();
        heartCollectSound.dispose();
        gameOverSound.dispose();
        beartrapSound.dispose();
        hurtSound.dispose();
        winSound.dispose();

        // Dispose of animation
        gameMap.dispose();
    }

    /**
     * Updates the game state, handling player input, enemies, traps and other elements.
     *
     * @param delta Time in seconds since the last update.
     */
    private void update(float delta) {
        //Check if the player's heart are depleted, triggers a game over
        if(player.getHearts()<=0){
            Gdx.input.setCursorCatched(false);
            gameOverSound.play();
            game.goToMenu("You died!");
        }

        //Calculate player movement speed based on delta
        float speed = 400f * delta;

        //Update state time for animations
        stateTime += delta / 2;

        //Handle player input, enemy interactions, and collectables
        handleInput(speed);
        handleEnemies(delta);
        handleTraps(delta);
        handleKeyCollect();
        handleGunshotCollect();
        handleLiveCollect();

        //Ensure camera stays within valid bounds based on play position
        float minX = (float) 1280 / 2;
        float minY = (float) 720 / 2;
        float maxX = GameMap.MAP_COLUMNS * GameMap.TILE_SIZE - (float) 1280 / 2;
        float maxY = GameMap.MAP_ROWS * GameMap.TILE_SIZE - (float) 720 / 2;
        camera.position.x = player.getCharacterX() + 32;
        camera.position.y = player.getCharacterY() + 64;
        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
        camera.update();

        //Ensure player stays within valid game map bounds
        player.setCharacterX(MathUtils.clamp(player.getCharacterX(), 0, GameMap.MAP_COLUMNS * GameMap.TILE_SIZE - 64));
        player.setCharacterY(MathUtils.clamp(player.getCharacterY(), 0, GameMap.MAP_ROWS * GameMap.TILE_SIZE - 128));
    }

    /**
     * Handles traps in the game.
     * Reduces player hearts if the player intersects with a trap
     * Trap will be deleted after player's intersection
     *
     *  @param delta time in seconds since the last trap handling
     */
    private void handleTraps(float delta) {
        cooldownTimer -= delta;
        Iterator<Trap> iterator = traps.iterator();
        while(iterator.hasNext()){

            Trap trap = iterator.next();
            // Check for intersection only if the cooldown timer is less than or equal to 0
            if (cooldownTimer <= 0) {
                if (characterIntersectsObject(player.getCharacterX(), player.getCharacterY(), trap.getX(), trap.getY())) {
                    //Reduce player hearts, updates toolbar, play sound, and remove the trap
                    player.setHearts(player.getHearts() - 1);
                    toolbar.setHearts(player.getHearts());
                    beartrapSound.play();
                    gameMap.setCoordinates(trap.getX(), trap.getY(), null);
                    // Set the cooldown timer to the cooldown duration
                    cooldownTimer = COOLDOWN_DURATION;
                    //Remove the trap
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Handles interactions with enemies in the game
     * Reduces player hearts if the player intersects with an enemy
     *
     * @param delta time in seconds since the last enemy handling
     */
    private void handleEnemies(float delta) {
        cooldownTimer -= delta;
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();

            // Update the enemy state
            enemy.update(delta,gameMap, player.getCharacterX(), player.getCharacterY(), enemies);

            // Check for intersection only if the cooldown timer is less than or equal to 0
            if (cooldownTimer <= 0) {
                if (characterIntersectsEnemy(player.getCharacterX(), player.getCharacterY(), enemy)) {
                    //Reduce player hearts, update toolbar, play hurt sound, and set cooldown timer
                    player.setHearts(player.getHearts() - 1);
                    toolbar.setHearts(player.getHearts());
                    hurtSound.play();
                    // Set the cooldown timer to the cooldown duration
                    cooldownTimer = COOLDOWN_DURATION;
                }
            }
        }
    }

    /**
     * Handles player input. Includes movement with arrows, and actions
     *
     * @param speed at which the playes character moves
     */
    private void handleInput(float speed) {
        float oldCharacterX = player.getCharacterX();
        float oldCharacterY = player.getCharacterY();

        //Check if the ESC key is just pressed to pause the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.input.setCursorCatched(false);
            pause();
        }

        //Check if the Z key is just pressed to perform a gunshot
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (player.getShots()>0&&!enemies.isEmpty()&& Objects.requireNonNull(getClosestEnemy()).calculateDistanceToCharacter(player.getCharacterX(),player.getCharacterY())<=500){
                removedEnemy = getClosestEnemy();
                enemies.remove(getClosestEnemy());
            player.setShots(player.getShots()-1);
            toolbar.setShots(player.getShots());
            gunSound.play();
            }
        }else{
        //Handle player's movement based on the 4 arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setCharacterX(player.getCharacterX()- speed);
            walkingSound.play();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setCharacterX(player.getCharacterX()+ speed);
            walkingSound.play();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setCharacterY(player.getCharacterY()- speed);
            walkingSound.play();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setCharacterY(player.getCharacterY()+ speed);
            walkingSound.play();
        } else {
            //Pause walking sound when the arrows are no pressed
            walkingSound.pause();
        }
        //Check for collisions with walls and sets it up to previous position when it's necessary
        if (isCollisionWithWalls(player.getCharacterX(), player.getCharacterY(), gameMap)) {
            player.setCharacterX(oldCharacterX);
            player.setCharacterY(oldCharacterY);
            }
        }

    }

    /**
     * Shows pause screen. Initializes if it was not already.
     */
    private void showPauseScreen() {
        if (pauseScreen == null) {
            pauseScreen = new PauseScreen(this, game, game.fileChooser);
        }
        game.setScreen(pauseScreen);
    }
    /**
     * Hides the pause screen and disposes it
     */
    private void hidePauseScreen() {
        if (pauseScreen != null) {
            game.setScreen(this);
            pauseScreen.dispose();
            pauseScreen = null;
        }
    }

    /**
     * Method that belongs to LigGDX screen interface.
     * Sets pause to true
     */
    @Override
    public void pause() {
        // Pause the game when the application is paused
        paused = true;
        Gdx.input.setCursorCatched(false);
        showPauseScreen();
    }

    /**
     * Method that belongs to LigGDX screen interface
     */
    @Override
    public void resume() {
        // Resume the game when the application is resumed.
        paused = false;
        hidePauseScreen();
    }

    /**
     * Brings the closest enemy to the player character
     * @return the closest enemy or null if the enemies list is empty
     */
    private Enemy getClosestEnemy() {
        if(!enemies.isEmpty()){
        Enemy closest = enemies.get(0);
        for (Enemy enemy: enemies){
            float dist1 = closest.calculateDistanceToCharacter(player.getCharacterX(), player.getCharacterY());
            float dist2 = enemy.calculateDistanceToCharacter(player.getCharacterX(), player.getCharacterY());
            if(dist2<dist1){
                closest=enemy;
            }
        }
        if(closest.getEnemyX()>= player.getCharacterX()){
            right=true;
        } else {
            right=false;
        }
        calculateAngle(closest);
        System.out.println("Angle: "+ angle);
        return closest;
      }
      return null;
    }

    /**
     * Checks for collision with walls based on the character's coordinates
     * @param coordinateX - character
     * @param coordinateY - character
     * @param gameMap - GameMap object representing the game map
     * @return true if there is a collision with walls, false othewise
     */

    public static boolean isCollisionWithWalls(float coordinateX, float coordinateY, GameMap gameMap) {
        int characterTileY1 = (int) ((coordinateY) / GameMap.TILE_SIZE);
        int characterTileX2 = (int) ((coordinateX + 64) / GameMap.TILE_SIZE);
        int characterTileX1 = (int) ((coordinateX) / GameMap.TILE_SIZE);
        int characterTileY2 = (int) ((coordinateY + 80) / GameMap.TILE_SIZE);

        if (gameMap.isWall(characterTileX1, characterTileY1)
                || gameMap.isWall(characterTileX1, characterTileY2)
                || gameMap.isWall(characterTileX2, characterTileY1)
                || gameMap.isWall(characterTileX2, characterTileY2)) {
            return true;
        }
        return false;
    }

    /**
     * Handles the collection of keys by the player
     */
    //Collectable Key
    private void handleKeyCollect() {
        Iterator<Key> iterator = keys.iterator();
        while(iterator.hasNext()) {
            Key key = iterator.next();
            if (characterIntersectsObject(player.getCharacterX(), player.getCharacterY(), key.getX(), key.getY())) {
                key.collect();
                exitUnlocked = true;
                toolbar.setKeyCollected(true);
                keyCollectSound.play();
                gameMap.setCoordinates(key.getX(), key.getY(), null);
                iterator.remove();
            }
        }
    }

    /**
     * Handles the unlocking of the exit door based on handleKeyCollect method
     */
    private void handleExitUnlock() {
        for (ExitDoor exitDoor : gameMap.getExits()) {
            if (characterIntersectsObject(player.getCharacterX(), player.getCharacterY(), exitDoor.getX(), exitDoor.getY())) {
                if (exitUnlocked) {
                    font.draw(batch, "Press Y to exit the maze", player.getCharacterX(), player.getCharacterY());
                    if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
                        Gdx.input.setCursorCatched(false);
                        winSound.play();
                        game.goToMenu("You successfully escaped!");
                    }
                }
            }

        }
    }

    /**
     * Checks if the character intersect with an object
     *
     * @param characterX character coordinate
     * @param characterY character coordinate
     * @param objectX object coordinate
     * @param objectY object coordinate
     * @return true if the character intersects with the object, false otherwise
     */
    private boolean characterIntersectsObject(float characterX, float characterY, float objectX, float objectY) {
        int characterTileX1 = (int) ((characterX+32) / GameMap.TILE_SIZE);
        int characterTileY1 = (int) ((characterY+64) / GameMap.TILE_SIZE);
        int objectTileX = (int) objectX;
        int objectTileY = (int) objectY;
        return (characterTileX1 == objectTileX && characterTileY1 == objectTileY);
    }
    /**
     * Checks if the character intersect with an enemy
     *
     * @param characterX character coordinate
     * @param characterY character coordinate
     * @param enemy to check for intersection
     * @return true if the character intersects with an enemy, false otherwise
     */
    private boolean characterIntersectsEnemy(float characterX, float characterY, Enemy enemy) {
        int characterTileX1 = (int) ((characterX+64) / GameMap.TILE_SIZE);
        int characterTileY1 = (int) ((characterY+64) / GameMap.TILE_SIZE);
        int characterTileX2 = (int) ((characterX + 64) / GameMap.TILE_SIZE);
        int characterTileY2 = (int) ((characterY + 80) / GameMap.TILE_SIZE);
        int enemyTileX = (int) (enemy.getEnemyX()+32) / GameMap.TILE_SIZE;
        int enemyTileY = (int) enemy.getEnemyY()/GameMap.TILE_SIZE;
        return (characterTileX1 == enemyTileX && characterTileY1 == enemyTileY);
    }

    /**
     * Handles the collection of lives by the player
     */

    //Collectable Lives
    private void handleLiveCollect() {
        Iterator<Lives> iterator = lives.iterator();
        while (iterator.hasNext()) {
            Lives live = iterator.next();
            if (characterIntersectsObject(player.getCharacterX(), player.getCharacterY(), live.getX(), live.getY())) {
                live.collect();
                player.setHearts(player.getHearts()+1);
                toolbar.setHearts(player.getHearts());
                heartCollectSound.play();
                gameMap.setCoordinates(live.getX(), live.getY(), null);
                iterator.remove(); // Removes the current item from the list
            }
        }
    }
    /**
     * Handles the collection of gunshots by the player
     */

    //Collectable Gunshots
    private void handleGunshotCollect(){
        Iterator<Gunshot> iterator = gunshots.iterator();
        while (iterator.hasNext()) {
            Gunshot gunshot = iterator.next();
            if (characterIntersectsObject(player.getCharacterX(), player.getCharacterY(), gunshot.getX(), gunshot.getY())) {
                gunshot.collect();
                gunCollectSound.play();
                player.setShots(player.getShots()+1);
                toolbar.setShots(player.getShots());
                gameMap.setCoordinates(gunshot.getX(), gunshot.getY(), null);
                iterator.remove(); // Removes the current item from the list

            }
        }
    }

    /**
     * Resumes the game after being paused.
     * Reverses the actions taken during the pause and hides pause screen
     * Sets pause to false
     */
    public void resumeGame() {
        paused=false;
        hidePauseScreen();
    }
}




