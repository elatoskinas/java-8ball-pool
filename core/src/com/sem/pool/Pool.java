package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.sem.pool.factories.AssetLoader;
import com.sem.pool.factories.BallFactory;

import com.sem.pool.factories.CameraFactory;
import com.sem.pool.factories.CueFactory;
import com.sem.pool.factories.SceneFactory;
import com.sem.pool.factories.TableFactory;
import com.sem.pool.game.Game;
import com.sem.pool.game.GameState;
import com.sem.pool.game.Player;
import com.sem.pool.scene.Scene3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Pool Game application class that handles
 * the 3D pool scene and all the interactions.
 * TODO: Split this off into smaller components?
 */
public class Pool extends ApplicationAdapter {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;
    private static final Vector3 cameraPosition = new Vector3(0f, 100f, 0f);
    private transient Game game;

    // State flag to keep track of whether asset loading
    // has finished.
    private transient boolean loaded;

    @Override
    public void create() {
        initializeAssetLoader();

        // Initialize model batch for rendering
        modelBatch = new ModelBatch();

        // Initialize viewport to the relevant width & height
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize the Bullet wrapper used for collisions
        Bullet.init();
    }

    /**
     * Initializes the asset loader & loads the necessary
     * models into the asset loader.
     */
    private void initializeAssetLoader() {
        // Initialize objects to handle asset loading
        AssetManager manager = new AssetManager();
        assetLoader = new AssetLoader(manager);

        // Initialize models by queueing them for loading
        assetLoader.initializeAssets();
    }

    /**
     * Initializes the scene if loading of assets has
     * been completed and the scene is not yet initialized.
     */
    private void initializeScene() {
        // If the game has not yet been loaded, and an
        // assetLoader update event is received in current iteration,
        // then load the game.
        if (!loaded && assetLoader.getAssetManager().update()) {

            float width = Gdx.graphics.getWidth();
            float height = Gdx.graphics.getHeight();
            CameraFactory cameraFactory = new CameraFactory(width, height, cameraPosition);

            Texture cueTexture = null;
            CueFactory cueFactory = new CueFactory(cueTexture, assetLoader);

            List<Texture> ballTextures = assetLoader.getBallTextures();
            BallFactory ballFactory = new BallFactory(ballTextures, assetLoader);

            Texture tableTexture = null;
            TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);

            SceneFactory sceneFactory = new SceneFactory(tableFactory,
                    ballFactory, cameraFactory, cueFactory, modelBatch);

            // Instantiate the scene
            scene = sceneFactory.createScene();

            // Update the camera of the scene to point to the right location
            scene.getCamera().update();

            initializeGame();

            // The assets of the game are now fully loaded
            loaded = true;
        }
    }

    /**
     * Initializes the Game instance for the current Pool Game.
     */
    private void initializeGame() {
        // Create players with IDs 0 and 1
        List<Player> players = new ArrayList<>();
        players.add(new Player(0));
        players.add(new Player(1));

        // Create game state with the scene's pool balls and created players
        GameState gameState = new GameState(players, scene.getPoolBalls());

        // Create game instance with GDX input, the scene and the created game state
        game = new Game(scene, Gdx.input, gameState);

        // Start the game
        game.startGame();
    }

    /**
     * Updates the scene & game for the current render iteration.
     * Handles rendering the scene and advancing the game loop.
     */
    private void update() {
        // Render the scene only if the game is loaded
        if (loaded) {
            // Advance the game loop of the game & render scene
            game.advanceGameLoop();
            scene.render();
        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Scene3D getScene() {
        return scene;
    }

    @Override
    public void render() {
        // Initialize scene if uninitialized]
        initializeScene();

        // Clear depth buffer & color buffer masks
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Update the scene & game for the current iteration
        update();
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();

    }
}