package de.tum.cit.ase.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * The PathFinding class is responsible for computing paths for the enemy movement using the A* algorithm.
 * A* algorithm helps enemies navigate the maze intelligently, avoiding walls,
 * and finding the quickest route to the player's position
 * It calculates paths to a specified target or a random target based on the character's position and game map layout.
 * The class utilizes the Node class to represent grid coordinates during pathfinding.
 */
public class PathFinding {
    private int targetTileX;
    private int targetTileY;
    private float enemyX;
    private float enemyY;
    private float direction;
    private Array<Vector2> path; // The path the enemy will follow
    private float speed;
    private boolean pathNull=false;
    public void update(float delta, float characterX, float characterY, GameMap gameMap, ArrayList<Enemy> enemies) {

        if (path == null || path.size == 0) {
            // No path or reached the target, find a new target

                findNewRandomTarget(characterX, characterY, gameMap, enemies);

        } else {
            // Follow the path
            if ((path.first() != null)) {
                Vector2 nextTile = path.first();
                float distance = speed * delta;

                // Move towards the next tile
                if (enemyX < nextTile.x) {
                    direction=1;
                    enemyX += Math.min(distance, nextTile.x - enemyX);
                } else if (enemyX > nextTile.x) {
                    direction=3;
                    enemyX -= Math.min(distance, enemyX - nextTile.x);
                } else if (enemyY < nextTile.y) {
                    direction=2;
                    enemyY += Math.min(distance, nextTile.y - enemyY);
                } else if (enemyY > nextTile.y) {
                    direction=0;
                    enemyY -= Math.min(distance, enemyY - nextTile.y);
                }

                // Check if reached the next tile
                if (enemyX == nextTile.x && enemyY == nextTile.y) {
                    // Remove the reached tile from the path
                    path.removeIndex(0);
                }

            }
        }
    }

    private void findNewTarget(float characterX, float characterY, GameMap gameMap, ArrayList<Enemy> enemies) {
        // Replace this with your logic to find a new target tile

            targetTileX = (int) (characterX/GameMap.TILE_SIZE);
            targetTileY = (int) characterY/GameMap.TILE_SIZE;
        // Replace this with your A* pathfinding implementation
        path = calculatePathToTarget(targetTileX, targetTileY, gameMap, enemies);
    }
    private void findNewRandomTarget(float characterX, float characterY, GameMap gameMap, ArrayList<Enemy> enemies) {
        float distanceToCharacter = calculateDistanceToCharacter(characterX, characterY);

        // Set a threshold distance; adjust this value based on your game requirements
        float followThreshold = 500f;
        targetTileX = (int) ((characterX + 32) / 128);
        targetTileY = (int) ((characterY + 64) / 128);
        if (distanceToCharacter < followThreshold&&hasPossiblePath(targetTileX, targetTileY,gameMap,enemies,10)) {


            // Replace this with your A* pathfinding implementation
            path = calculatePathToTarget(targetTileX, targetTileY, gameMap, enemies);

            // Return true if a valid path is found

        } else {
            // Return false if no valid path is found

        }
    }
    /**
     * Calculates the path from the enemy's current position to the target position using the A* pathfinding algorithm.
     * The algorithm intelligently navigates through the maze, avoiding obstacles and finding the shortest path.
     *
     * @param targetX The x-coordinate of the target position.
     * @param targetY The y-coordinate of the target position.
     * @param gameMap The GameMap object representing the game's map.
     * @param enemies The ArrayList of Enemy objects representing other game entities.
     * @return An Array of Vector2 representing the calculated path from the enemy's position to the target position.
     */
    private Array<Vector2> calculatePathToTarget(int targetX, int targetY, GameMap gameMap, ArrayList<Enemy> enemies) {
        // A* pathfinding algorithm
        Array<Node> openList = new Array<>();
        Array<Node> closedList = new Array<>();
        Array<Vector2> path = new Array<>();

        // Convert enemy position to grid coordinates
        int startX = (int) (enemyX / GameMap.TILE_SIZE);
        int startY = (int) (enemyY / GameMap.TILE_SIZE);
        // Create the start node
        Node startNode = new Node(startX, startY);
        startNode.g = 0;
        startNode.h = heuristic(startX, startY, targetX, targetY);
        startNode.f = startNode.g + startNode.h;

        openList.add(startNode);

        while (openList.size > 0) {
            // Find the node with the lowest F cost
            Node current = findLowestCostNode(openList);

            // Move the current node from open to closed list
            openList.removeValue(current, true);
            closedList.add(current);

            // Check if the current node is the target
            if (current.enemyX == targetX && current.enemyY == targetY) {
                // Reconstruct the path
                path.add(new Vector2(targetX * GameMap.TILE_SIZE, targetY * GameMap.TILE_SIZE));
                while (current.parent != null) {
                    path.add(new Vector2(current.parent.enemyX * GameMap.TILE_SIZE, current.parent.enemyY * GameMap.TILE_SIZE));
                    current = current.parent;
                }
                path.reverse();
                break;
            }

            // Explore neighbors
            Array<Node> neighbors = getNeighbors(current, targetX, targetY, gameMap, enemies);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor, true) || !isValidMove(neighbor.enemyX,neighbor.enemyY, gameMap)) {
                    continue; // Skip if already in the closed list or not a valid move
                }

                float tentativeG = current.g + distance(current, neighbor);

                if (!openList.contains(neighbor, true) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(neighbor.enemyX, neighbor.enemyY, targetX, targetY);
                    neighbor.f = neighbor.g + neighbor.h;

                    if (!openList.contains(neighbor, true)) {
                        openList.add(neighbor);
                    }
                }
            }
            if (path.size > 10) {
                // Terminate the path and break out of the loop
                path.clear();
                pathNull=true;
                return path;
            }
        }
        return path;
    }

    private Node findLowestCostNode(Array<Node> nodes) {
        Node lowest = nodes.first();
        for (Node node : nodes) {
            if (node.f < lowest.f) {
                lowest = node;
            }
        }
        return lowest;
    }
    private Array<Vector2> avoidEnemyIntersections(Array<Vector2> path, ArrayList<Enemy> enemies) {
        Array<Vector2> newPath = new Array<>(path);

            // Iterate through each enemy and check if the path intersects with the enemy's position
            for (int i = 0; i < newPath.size; i++) {
                for (Enemy enemy: enemies){
                    Vector2 pathNode = newPath.get(i);
                    // Adjust the path if it intersects with the enemy
                    if ((int) pathNode.x==(int) enemy.getEnemyX()/128 && (int)pathNode.y== (int) enemy.getEnemyY()/128)
                    {
                        newPath=null;
                    }
                }

        }

        return newPath;
    }

    private float calculateDistanceToCharacter(float x, float y, float enemyX, float enemyY) {
        float dx = x*128 - enemyX;
        float dy = y*128 - enemyY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private float distance(Node nodeA, Node nodeB) {
        return Math.abs(nodeA.enemyX - nodeB.enemyX) + Math.abs(nodeA.enemyY - nodeB.enemyY);
    }

    private float heuristic(int startX, int startY, int targetX, int targetY) {
        return Math.abs(startX - targetX) + Math.abs(startY - targetY);
    }
    private boolean isValidMove(int x, int y, GameMap gameMap) {
        // Check if the move is within the map boundaries and not a wall


        return x >= 0 && x < GameMap.MAP_COLUMNS
                && y >= 0 && y < GameMap.MAP_ROWS
                && !gameMap.isWall(x, y);
    }

    private Array<Node> getNeighbors(Node node, int targetX, int targetY, GameMap gameMap, ArrayList<Enemy> enemies) {
        Array<Node> neighbors = new Array<>();

        // Define possible movement directions (left, right, up, down)
        int[] directions = {-1, 0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int nextX = node.enemyX + directions[i];
            int nextY = node.enemyY + directions[i + 1];

            if (isValidMove(nextX, nextY, gameMap)) {
                Node neighbor = new Node(nextX, nextY);
                neighbor.h = heuristic(nextX, nextY, targetX, targetY);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private void addNeighbor(float x, float y, Array<Node> neighbors, int targetX, int targetY, GameMap gameMap) {
        // Check if the neighbor is within the map boundaries
        int gridX = (int) (x / GameMap.TILE_SIZE);
        int gridY = (int) (y / GameMap.TILE_SIZE);

        if (x >= 0 && x < GameMap.MAP_COLUMNS * GameMap.TILE_SIZE
                && y >= 0 && y < GameMap.MAP_ROWS * GameMap.TILE_SIZE
                && !gameMap.isWall(gridX, gridY)) {
            Node neighbor = new Node(gridX, gridY);
            neighbor.h = heuristic(gridX, gridY, targetX, targetY);
            neighbors.add(neighbor);
        }
    }

    public void setEnemyPosition(float enemyX, float enemyY) {
        this.enemyX=enemyX;
        this.enemyY=enemyY;
    }

    public void setSpeed(float speed) {
        this.speed=speed;
    }

    public float getEnemyX() {
        return enemyX;
    }

    public float getEnemyY() {
        return enemyY;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
    private float calculateDistanceToCharacter(float characterX, float characterY) {
        // Calculate the distance between the enemy and the character
        float dx = characterX - enemyX;
        float dy = characterY - enemyY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    private boolean isTileVisible(int x, int y) {
        // Adjust this based on your screen dimensions and the number of tiles visible
        int visibleColumns = 10;  // Adjust based on the number of visible columns
        int visibleRows = 6;      // Adjust based on the number of visible rows

        return x >= 0 && x < visibleColumns && y >= 0 && y < visibleRows;
    }
    private void stop() {
        // Implement logic to stop the enemy
        // For example, set speed to zero or perform other stop-related actions
        speed = 0;
        path = null; // Reset path to avoid further updates
    }
    private boolean hasPossiblePath(int targetX, int targetY, GameMap gameMap, ArrayList<Enemy> enemies, int maxIterations) {
        Array<Node> openList = new Array<>();
        Array<Node> closedList = new Array<>();

        int startX = (int) (enemyX / GameMap.TILE_SIZE);
        int startY = (int) (enemyY / GameMap.TILE_SIZE);

        Node startNode = new Node(startX, startY);
        startNode.g = 0;
        startNode.h = heuristic(startX, startY, targetX, targetY);
        startNode.f = startNode.g + startNode.h;

        openList.add(startNode);

        int iterations = 0;

        while (openList.size > 0 && iterations < maxIterations) {
            Node current = findLowestCostNode(openList);
            openList.removeValue(current, true);
            closedList.add(current);

            if (current.enemyX == targetX && current.enemyY == targetY) {
                return true; // Path found
            }

            Array<Node> neighbors = getNeighbors(current, targetX, targetY, gameMap, enemies);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor, true)) {
                    continue;
                }

                float tentativeG = current.g + distance(current, neighbor);

                if (!openList.contains(neighbor, true) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(neighbor.enemyX, neighbor.enemyY, targetX, targetY);
                    neighbor.f = neighbor.g + neighbor.h;

                    if (!openList.contains(neighbor, true)) {
                        openList.add(neighbor);
                    }
                }
            }

            iterations++;
        }

        return false; // No path found within the specified iterations
    }

}


