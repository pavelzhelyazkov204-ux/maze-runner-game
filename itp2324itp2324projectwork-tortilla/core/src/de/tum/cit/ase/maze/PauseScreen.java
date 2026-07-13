package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import java.io.File;
import java.io.FilenameFilter;


/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class PauseScreen implements Screen {

    private final Stage stage;
    private FileHandle mapFile;
    private SpriteBatch batch;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public PauseScreen(GameScreen gameScreen, MazeRunnerGame game, NativeFileChooser fileChooser) {
        NativeFileChooserConfiguration conf = new NativeFileChooserConfiguration();


        // Starting from user's dir
        conf.directory = Gdx.files.absolute(System.getProperty("user.home"));

        // Filter out all files which do not have the .ogg extension and are not of an audio MIME type - belt and braces
        conf.mimeFilter = "text/plain";
        conf.nameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        };

        // Add a nice title
        conf.title = "Choose audio file";


        var camera = new OrthographicCamera();

        camera.setToOrtho(false, 1280, 720);
        camera.zoom = 1f; // Set camera zoom for a closer view

        Viewport viewport = new FitViewport(1280, 720, camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements
        batch = new SpriteBatch();

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        Label titleLabel = new Label("Game paused", game.getSkin(), "title");
        titleLabel.setFontScale(0.6f); // Adjust the scale factor as needed
        table.add(titleLabel).padBottom(350).row();
        Label errorLabel = new Label("Choose an appropriate map file", game.getSkin(), "title");
        errorLabel.setFontScale(0.2f); // Adjust the scale factor as needed
        table.add(errorLabel).padBottom(10).row();
        errorLabel.setVisible(false);
        // Create and add a button to go to the game screen
        TextButton exitToMenu = new TextButton("Exit to main menu", game.getSkin());
        TextButton aContinue = new TextButton("Continue", game.getSkin());
        aContinue.getLabel().setFontScale(0.8f); // Adjust the scale factor as needed
        table.add(aContinue).width(300).height(50).row();
        aContinue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setCursorCatched(true);
                gameScreen.resumeGame();

            }
        });

        exitToMenu.getLabel().setFontScale(0.8f); // Adjust the scale factor as needed
        table.add(exitToMenu).width(300).height(50).row();
        exitToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu("Adventures of Antonio Banderas");
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
        batch.flush();

    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
