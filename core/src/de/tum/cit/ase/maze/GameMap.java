package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents the game map with tiles, entities and objects.
 * Manages rendering and provides methods to retrieve various game elements.
 * Provides visual representation and functionality of the game map.
 * 1) Tiles: layout of the map texture files,free tiles, doors and walls.
 * 2) Entities: subject to interactions and behaviors (that belong to a separated classes).
 * such as Keys, Enemies, Exit Doors, Entry Doors, Lives, Gunshots, and Traps.
 * 3) Objects: visual representation for in-game entities textures regions.
 */

public class GameMap {
    /**
     * Constants for tile size and map dimensions
     */
    final static int TILE_SIZE=128;
    static int MAP_ROWS;
    static int MAP_COLUMNS;
    private final ArrayList<Key> keys = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<ExitDoor> exits = new ArrayList<>();
    private EntryDoor entryDoor;
    /**
     * Lists to store different game elements
     * */
    private final ArrayList<Lives> lives = new ArrayList<>();
    private final ArrayList<Gunshot> gunshots = new ArrayList<>();
    private final ArrayList<Trap> traps = new ArrayList<>();
    /**
     *  Map to store tile information about key-pairs values
     *  Represents game map's Layout.
     */
    Map<String, String> map;

    /**
    Texture regions for wall and free tiles
     */
    //Wall and free textures tiles
    private final TextureRegion wallTexture = new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 0, 0, 16, 16);
    private final TextureRegion wallTexture2 = new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 112, 64, 16, 16);
    private final TextureRegion freeTileTexture= new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 16, 0, 16, 16);
    private final TextureRegion freeTileTexture2= new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 48, 0, 16, 16);
    //Objects textures
    private final TextureRegion keyTexture = new TextureRegion(new Texture(Gdx.files.internal("character.png")), 242, 3, 28, 64);
    private final TextureRegion gunshotTexture = new TextureRegion(new Texture(Gdx.files.internal("objects.png")), 302, 92, 64, 41);
    private final Texture objectSheet = new Texture(Gdx.files.internal("objects.png"));
    /**
     * Animation for hearts
    */
    private Animation<TextureRegion> heartAnimation;
    /**
     * Map to store wall information about if a tile is a wall or not
     *Important for wall collision and pathfinding
     */
    Map<String, Boolean> booleanList;
    /**
     * Constructs a GameMap by loading the map from the given file.
     *
     * @param mapFile The file containing the map data.
     */
    public GameMap(FileHandle mapFile) {
        initializeMap(mapFile);
    }
    /**
     * Initializes the game map by loading the map data.
     * Sets up animations, and configures map dimensions.
     * Load the map data from the specified file using the mapLoader.
     * @param mapFile
     */
    private void initializeMap(FileHandle mapFile) {
        map = MapLoader.loadMap(mapFile);

        /**
         * Create an array that stores texture regions for heart animation
         */
        Array<TextureRegion> heartFrames = new Array<>(TextureRegion.class);
        //Runs 4 times that create 4 frames for the animation
        for (int col = 0; col < 4; col++) {
            heartFrames.add(new TextureRegion(objectSheet, col * 16+2, 51, 11, 11));
        }
        //Creat an animation using heartFrames array with duration 0.1sec
        heartAnimation = new Animation<>(0.1f,heartFrames);
        /**
         * Set number of rows and columns in the map by extracting the following from the loaded map
         */
        MAP_ROWS=MapLoader.getRows(map)+1;
        MAP_COLUMNS=MapLoader.getColumns(map)+1;

        /**
         * Add hearts and guns randomly to the map
         * Generates a map of random wall and initialize the booleanlist.
         */
        addHeartsAndGuns();
        booleanList = getWalls();

        setExits();
        setEntryDoor();
    }

    /**
     * Renders the game the given SpriteBatch and state time
     *
     * @param batch SpriteBatch used for rendering
     * @param stateTime State time for animations
     */
    public void render(SpriteBatch batch, float stateTime) {
        // Iterates through each row and columns of the map
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                //Calculate the screen coordinates for the current tile
                float x = j * TILE_SIZE;
                float y = i * TILE_SIZE;
                // Get the type of the current tile from the map
                String tileType = map.get(getCoordinateKey(j, i));
                TextureRegion texture;
                //Selects the appropriate texture region based on the tile
                if(tileType!=null){
                    if(booleanList.get(getCoordinateKey(j,i))){
                // Draw textures based on the tile type
                    texture = switch (tileType) {
                    case "0" -> wallTexture2;
                    case "1" -> entryDoor.getTexture();
                    case "2" -> exits.get(0).getTexture();
                    case "3" -> Trap.TRAP_TEXTURE;
                    default -> freeTileTexture;
                    };
                }else{
                        texture = switch (tileType) {
                            case "0" -> wallTexture;
                            case "1" -> entryDoor.getTexture();
                            case "2" -> exits.get(0).getTexture();
                            case "3" -> Trap.TRAP_TEXTURE;
                            default -> freeTileTexture;};
                    }
                //Draw the texture on the batch  at the calculated position and size
                batch.draw(texture, x, y, TILE_SIZE, TILE_SIZE);
                    if(tileType.equals("5")){
                    batch.draw(keyTexture,x+50,y+32,28,64);
                }

                    if(tileType.equals("6")){
                        batch.draw(heartAnimation.getKeyFrame(stateTime, true),x+32,y+32,64,64);
                    }

                    if(tileType.equals("7")){
                        batch.draw(gunshotTexture,x+32,y+32,64,60);
                    }

                }else{
                    //Draw a free tile texture if the tile type is null
                    batch.draw(freeTileTexture, x, y, TILE_SIZE, TILE_SIZE);

                }

            }
        }
    }
    /**
     * Gets coordinate key for specific row and column.
     * @param row index
     * @param col index
     * @return coordinate key as String
     */
    public String getCoordinateKey(int row, int col) {
        return row + "," + col;
    }

    /**
     * Disposes of resources used by the game map
     */
    public void dispose() {
        objectSheet.dispose();

    }

    /**
     * Checks if the given tile indices correspond to a wall
     *
     * @param tileX
     * @param tileY
     * @return true if the tile is a wall, false otherwise
     */
    public boolean isWall(int tileX, int tileY) {
        // Check if the given tile indices correspond to a wall
        String tileType = map.get(getCoordinateKey(tileX, tileY));
        return tileType != null && tileType.equals("0");
    }

    /**
     * Checks if the given tile indices correspond to a key
     *
     * @param tileX
     * @param tileY
     * @return true if tile contains a key, false otherwise
     */
    public boolean isKey(int tileX, int tileY) {
        String tileType = map.get(getCoordinateKey(tileX, tileY));
        return tileType != null && (tileType.equals("5"));
    }

    /**
     * Creates a list of keys on the game map. Key are type "5"
     * @return ArrayList containing Key objects representing the locations
     */
    public ArrayList<Key> getKeys(){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                if(tileType.equals("5")){
                    Key key = new Key(j,i,false);
                    keys.add(key);
                }
                }
            }
        }
        return keys;

    }

    /**
     * Creates a list of enemies on the game map
     *
     * @return ArrayList containing Enemy objects representing the locations
     */
    public ArrayList<Enemy> getEnemies(){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                    if(tileType.equals("4")){
                        Enemy enemy = new Enemy(j,i);
                        enemies.add(enemy);
                    }
                }
            }
        }
        return enemies;
    }

    /**
     * Sets the tile type at the specified coordinates
     * @param x
     * @param y
     * @param type new tile type to set.
     */
    public void setCoordinates(float x, float y, String type){
        map.replace(getCoordinateKey((int) x, (int) y),type);
    }

    /**
     * Sets exit on game map. Exits are type "2"
     */
    public void setExits(){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                if(tileType.equals("2")){
                    ExitDoor exitDoor = new ExitDoor(j,i,new TextureRegion(new Texture(Gdx.files.internal("basictiles.png")), 0, 224, 16, 16));
                    exits.add(exitDoor);
                }
                }
            }
        }
    }

    /**
     * @return EntryDoor object
     */
    public EntryDoor getEntryDoor() {
        return entryDoor;
    }

    /**
     * Sets entry door on game map. Entries are type "1"
     */
    public void setEntryDoor() {
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                    if(tileType.equals("1")){
                        entryDoor = new EntryDoor(j,i);
                    }
                }
            }
        }
    }

    /**
     * List of exits on game map
     * @return ArrayList containing ExitDoor objects representing the locations
     */
    public ArrayList<ExitDoor> getExits() {
        return exits;
    }

    /**
     * Checks if the given tile is a life. Lives type are "6"
     * @param tileX
     * @param tileY
     * @return true if it's a life, false otherwise
     */
    public boolean isLives(int tileX, int tileY) {
        String tileType = map.get(getCoordinateKey(tileX, tileY));
        return tileType != null && (tileType.equals("6")); //key-pairs
    }
    /**
     * List of lives on game map
     * @return ArrayList containing Lives objects representing the locations
     */
    public ArrayList<Lives> getLives(){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                    if(tileType.equals("6")){
                        Lives live = new Lives(j,i, false);
                        lives.add(live);
                    }
                }
            }
        }
        return lives; }

    /**
     * Checks if the given tile is a gunshot. Gunshot type are "7"
     * @param tileX
     * @param tileY
     * @return true if it's a gunshot, false otherwise
     */
    public boolean isGunshot(int tileX, int tileY) {
        String tileType = map.get(getCoordinateKey(tileX, tileY));
        return tileType != null && (tileType.equals("7")); //key-pairs
    }

    /**
     * List of gunshot on game map
     *
     * @return ArrayList containing Gunshot objects representing the locations
     */
    public ArrayList<Gunshot> getGunshots(){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                    if(tileType.equals("7")){
                        Gunshot gunshot = new Gunshot(j,i, false);
                        gunshots.add(gunshot);

                    }
                }
            }
        }
        return gunshots; }

    /**
     * Gets a list of free tiles on the map
      * @return ArrayList of coordinate key representing free tiles
     */
    public ArrayList<String> getFreeTiles(){
        ArrayList<String> freeTiles = new ArrayList<>();
            for (int i = 0; i < MAP_ROWS; i++) {
                for (int j = 0; j < MAP_COLUMNS; j++) {
                    String tileType = map.get(getCoordinateKey(j, i));
                    if(tileType==null) {
                        freeTiles.add(getCoordinateKey(j,i));
                    }
                }
            }
            return freeTiles;
        }

    /**
     * Gets a list of trap on the map
      * @return ArrayList of trap objects
     */
    public ArrayList<Trap> getTraps(){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                if(tileType!=null){
                    if(tileType.equals("3")){
                        Trap trap = new Trap(j,i);
                        traps.add(trap);
                    }
                }
            }
        }
        return traps;
    }

    /**
     * Generates a map of random walls
     * @return map of coordinate key and wall status. True if it's a wall
     */
    public Map<String, Boolean> getWalls(){
        Random random = new Random();
        Map<String, Boolean> wallsRandom = new HashMap<>();
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                boolean rand;
                if(i==0||i==MAP_ROWS-1||j==0||j==MAP_COLUMNS-1){
                    rand=false;
                }else {
                rand = random.nextInt(4)==0;
                }
                wallsRandom.put(getCoordinateKey(j,i),rand);
            }
        }
        return wallsRandom;
    }

    /**
     * Adds hearts and guns randomly to the maps
     */
    private void addHeartsAndGuns(){
        Random random = new Random();
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLUMNS; j++) {
                String tileType = map.get(getCoordinateKey(j, i));
                int randomNumber = random.nextInt(50);
                boolean heart = MathUtils.randomBoolean();
                if(tileType==null&&randomNumber==0) {
                    if(heart){
                        map.put(getCoordinateKey(j,i),"6");
                    }else {
                        map.put(getCoordinateKey(j,i),"7");
                    }
                }
            }
        }
    }

}

/**
 * Represents different states of animation
 */
enum AnimationState {
    PLAYING,
    REMOVED
}
