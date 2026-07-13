package de.tum.cit.ase.maze;

/**
 * The Node class represents a grid coordinate in the A* pathfinding algorithm (refer to PathFinding class)
 * It holds information about the node's position, cost, heuristic, and parent node.
 */
public class Node {
    int enemyX;
    int enemyY;
    float g; // Cost from start node to this node
    float h; // Heuristic (estimated cost from this node to the goal)
    float f; // Total cost (g + h)
    Node parent; // Parent node in the path

    Node(int enemyX, int enemyY) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
    }
}
