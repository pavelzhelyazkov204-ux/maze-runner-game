package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * The Enemy class represents an enemy entity in the game, capable of moving and animating in different directions.
 * It uses the A* pathfinding algorithm provided by the PathFinding class to intelligently navigate the game map, and
 * to find the optimal path to the player's position, while avoiding obstacles and other enemies.
 * The enemy takes information provided by the GameMap class to take the mentioned decisions.
 * Enemy Class contains logic for animation, movement and interaction; while PathFinding class handles the algorithm.
 */
public class Enemy {

    private final int animationFrames;
    private final Animation<TextureRegion> enemyDownAnimation;
    private final Animation<TextureRegion> enemyUpAnimation;
    private final Animation<TextureRegion> enemyLeftAnimation;
    private final Animation<TextureRegion> enemyRightAnimation;
    private float enemyX;
    private float enemyY;
    private float stateTime;
    private Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
    private Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);
    private Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
    private Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);
    private Rectangle bounds;
    private float speed;;
    private int direction;
    private PathFinding pathFinding = new PathFinding();

    /**
     * Constructs an Enemy object with the specified initial position.
     *
     * @param initialX x-coordinate of the enemy in grid coordinates.
     * @param initialY y-coordinate of the enemy in grid coordinates.
     */
    public Enemy(float initialX, float initialY) {

        animationFrames=4;
        this.enemyX = initialX*128;
        this.enemyY = initialY*128;
        this.stateTime = 0f;
        this.speed = 250f;
        Texture walkSheet = new Texture(Gdx.files.internal("mobs.png"));
        for (int col = 0; col < animationFrames; col++) {
            walkFramesDown.add(new TextureRegion(walkSheet, 16*col, 0, 12, 16));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesRight.add(new TextureRegion(walkSheet, col * 16, 32, 12, 16));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesUp.add(new TextureRegion(walkSheet, col * 16, 48, 12, 16));
        }
        for (int col = 0; col < animationFrames; col++) {
            walkFramesLeft.add(new TextureRegion(walkSheet,col * 16, 16, 12, 16));
        }
        enemyDownAnimation = new Animation<>(0.2f, walkFramesDown);
        enemyUpAnimation = new Animation<>(0.2f, walkFramesUp);
        enemyLeftAnimation = new Animation<>(0.2f, walkFramesLeft);
        enemyRightAnimation = new Animation<>(0.2f, walkFramesRight);
    }

    public void update(float delta, GameMap gameMap, float characterX, float characterY, ArrayList<Enemy> enemies) {
        float oldX=enemyX;
        float oldY=enemyY;

        // Set the current enemy position, direction, and speed for pathfinding
        pathFinding.setEnemyPosition(enemyX, enemyY);
        pathFinding.setDirection(direction);
        pathFinding.setSpeed(speed);

        // Update the enemy's position using A* pathfinding
        pathFinding.update(delta, characterX, characterY, gameMap, enemies);

        // Brings updated position, direction, and animation state
        enemyX = pathFinding.getEnemyX();
        enemyY = pathFinding.getEnemyY();
        direction= (int) pathFinding.getDirection();
        stateTime += delta;
    }

    private void moveRandomly(float delta) {

        switch (direction){
            case 1:enemyX += speed * delta;break;
            case 2:enemyY += speed * delta;break;
            case 3:enemyX -= speed * delta;break;
            case 0:enemyY -= speed * delta;break;
        }
        // Change direction randomly
        if (MathUtils.random() < 0.02f) {
            int random =MathUtils.random(3);
            if(direction!=random){
                direction=random;
            }
        }

    }
    //Getters and Setters
    public float getEnemyX() {
        return enemyX;
    }

    public float getEnemyY() {
        return enemyY;
    }

    public void render(SpriteBatch batch) {
        if (direction==3) {
            batch.draw(enemyLeftAnimation.getKeyFrame(stateTime, true),
                    enemyX+32, enemyY, 64, 128);
        } else if (direction==1) {
            batch.draw(enemyRightAnimation.getKeyFrame(stateTime, true),
                    enemyX+32, enemyY, 64, 128);
        } else if (direction==0) {
            batch.draw(enemyDownAnimation.getKeyFrame(stateTime, true),
                    enemyX+32, enemyY, 64, 128);
        } else if (direction==2) {
            batch.draw(enemyUpAnimation.getKeyFrame(stateTime, true),
                    enemyX+32, enemyY, 64, 128);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public String toString() { //toString method just to visualize the implementation
        return "Enemy{" +
                "enemyX=" + enemyX +
                ", enemyY=" + enemyY +
                '}';
    }
    public float calculateDistanceToCharacter(float characterX, float characterY) {
        // Calculate the distance between the enemy and the character
        float dx = characterX - enemyX;
        float dy = characterY - enemyY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}