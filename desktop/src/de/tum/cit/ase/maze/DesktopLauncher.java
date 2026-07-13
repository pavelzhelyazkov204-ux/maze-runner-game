package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import games.spooky.gdx.nativefilechooser.desktop.DesktopFileChooser;

/**
 * The DesktopLauncher class is the entry point for the desktop version of the Maze Runner game.
 * It sets up the game window and launches the game using LibGDX framework.
 */
public class DesktopLauncher {
	/**
	 * The main method sets up the configuration for the game window and starts the application.
	 *
	 * @param arg Command line arguments (not used in this application)
	 */
	public static void main(String[] arg) {
		// Configuration for the game window
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Adventures of Antonio Banderas"); // Set the window title

		// Get the display mode of the current monitor
		Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		// Set the window size to 80% of the screen width and height
		config.setFullscreenMode(displayMode);
		config.setWindowIcon("icon16.png");

		config.useVsync(true); // Enable vertical sync
		//config.setForegroundFPS(60); // Set the foreground frames per second

		// Launch the game
		new Lwjgl3Application(new MazeRunnerGame(new DesktopFileChooser()), config);
	}
}
