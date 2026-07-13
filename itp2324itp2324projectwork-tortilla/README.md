# Maze Runner Game: _The Adventures of Antonio Banderas ![Alt Text](C:\Users\DELL\IdeaProjects\itp2324itp2324projectwork-tortilla\assets\icon16.png)_

---

## Project Overview

The Maze Runner Game is a 2D game built using the LibGDX game development framework.
The game features a maze-like environment where the player navigates through obstacles, collects items, 
and encounters enemies. 
Developed by: 
-  Gonzalez Ortega Zeltzin Renata
-  Zhelyazkov Pavel

## Goal of the Game 
The goal of the game is for the player to navigate through the game map to collect a key that,
only then, will allow the player to exit the maze and win the game.

The player must strategically move to gather collectibles, such as gunshots and hearts.
While navigates, the player should avoid static traps and enemies. Additionally, the player can defend himself shooting 
at the enemies. At the same time, needs to manage their health represented by hearts, 
as running out of hearts results in a game over. 

## Project Structure 

### Class Hierarchy 
- `MazeRunnerGame`: Represents the main game class.
    - `GameScreen`: Manages the game screen and rendering.
    - `MenuScreen`: Manages the menu screen for starting the game.
    - `PauseScreen`: Handles pausing the game.
    - `GameMap`: Represents the game map, including walls and tiles. Uses key-pairs values for the objects and walls. 
    - `GameToolbar`: Displays game-related information such as the status of
  collectable hearts, collectable gunshots, and  collectable keys.
    - `MapLoader`: Loads and manages game maps from files.
    - `PathFinding`: Implements the A* pathfinding algorithm for enemy movement.
    - `Node`: Represents a node used in pathfinding.
    - `Enemy`: Represents enemy entities in the game.
    - `GameObject`: The superclass for all game objects, providing basic position and texture functionality.
        - `Player`: Represents the player entity.
        - `Lives`: Represents the collectable lives.
        - `Key`: Represents keys required for opening exit doors.
        - `Gunshot`: Represents the collectable gunshots by the player.
        - `ExitDoor`: Represents an exit door in the game.
        - `EntryDoor`: Represents an entry door in the game.
        - `Trap`: Represents traps in the game.

- `CreateMapProperties`: Generates properties files for custom map creation.
- `DesktopLauncher`: Entry point for the desktop version of the game.    
---

## How to Run the Game
### Prerequisites
- Ensure you have Java 17 installed.
- Set up the LibGDX framework and Gradle in your project.
### Steps:
1) Open Project:
- Open IntelliJ IDEA and load Maze Runner _"The Adventures of Antonio Banderas" project. 
2) Configure Gradle:
    - Ensure Gradle is configured for Java 17.
    - Confirm `DesktopLauncher` as the correct main class. 
    - Ensure correct configurations in the `build.gradle` file.
3) Run DesktopLauncher:
   - Locate the DesktopLauncher class in your project. It should be in desktop module under the de.tum.cit.ase.maze
   - Right-click on the DesktopLauncher class.
   - Select "Run DesktopLauncher.main()".
4) Game Window:
   - The game window should appear with the specified title (_Maze Runner "The Adventures of Antonio Banderas"_) and settings.
   - Make sure to load a map when the file chooser appears. You can find five different ones in maps folder inside the project
## How to Use the Game

- Movement: Use arrow keys to navigate the player character through the maze.
- Objectives: Collect the key to exit the maze, avoid enemies and static traps, and reach the exit door.
  Make sure to monitor your health status through heart icons. Running into enemies may reduce your hearts.
- Toolbar: Check the toolbar for hearts, shots, and key collection status.
- Additions:
    - Shotguns: Press 'Z' key to shoot gunshots when available.
    - Lives: Pick up the hearts to increase the player's health.
## Game mechanics beyond the minimal requirements

--- 

### Enemy Class, PathFinding Class, and Node Class  

##### Overview
The `Enemy` in the game is an intelligent entity that utilizes A* pathfinding to navigate the game map,
interact with obstacles, and avoid collisions with other enemies.

The `PathFinding` class provides A* pathfinding functionality, enabling enemies to navigate the maze intelligently. 
It calculates optimal paths based on game map details and player positions.

The `Node` class represents a cell in the grid used by the A* pathfinding algorithm. 
It stores essential information such as position, movement cost, and heuristic values.

#### Rules and Mechanics

- **A* Algorithm Components:**
    - `G`: Represents the cost of the path from the start node to the current node.
    - `H`: Heuristic estimate of the cost from the current node to the target node.
    - `F`: Combined cost (F = G + H) used for comparison and path selection.

- **Path Reconstruction:**
    - The `calculatePathToTarget` method reconstructs the path by tracing back from the target node to the start node, 
  forming the optimal path.

- **Termination Conditions:**
    - If the path exceeds a predefined length, the pathfinding process terminates to avoid unnecessary computations.
  
- **Enemy Position Update:**
    - Regularly updates the enemy's position based on the calculated path, 
  ensuring continuous adaptation to the player's movements.
- **Direction Adjustment:**
    - The enemy's movement direction is adjusted based on calculated path information.

###  Collectables: Gunshots and Hearts
##### Overview
Gunshots and Hearts use the same logic but with different purposes, one gives offensive capabilities, 
and the second one allows the player to endures collisions with `Trap` and `Enemy`.
#### Rules and Mechanics

- **Collecting Gunshots and Hearts:**
  - Players can acquire gunshots ahd hearts throughout the maze. 
  - Once the item is collected it will disappear. 
- **Firing Mechanism:**
  - Initiate gunshots using an input command ('Z' key).
  - Gunshots move in a specific direction, eliminating enemies within their path.
  
- **Logic implemented for Gunshots and Hearts:**
  - Uses an iterator through lives and gunshots collection. 
  - Checks `characterIntersectsObject` method to ensure collection and update status in `GameToolBar`.
  - Safely removes the item from the collection while iterating. 
  
---
Enjoy the adventure with Antonio Banderas! 



