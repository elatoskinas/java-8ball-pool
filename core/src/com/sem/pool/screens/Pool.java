package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.sem.pool.game.GameObserver;
import com.sem.pool.game.GameState;
import com.sem.pool.game.Player;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Scene3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Pool Game application class that handles
 * the 3D pool scene and all the interactions.
 * TODO: Split this off into smaller components?
 */
public class Pool implements Screen, GameObserver {
    private transient MainGame mainGame;

    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;
    private static final Vector3 cameraPosition = new Vector3(0f, 100f, 0f);
    private transient Game game;

    // State flag to keep track of whether asset loading
    // has finished.
    private transient boolean loaded;

    /**
     * Creates an instance of the Pool game screen,
     * and handles all of the initialization for the
     * game.
     */
    public Pool(MainGame game) {
        this.mainGame = game;

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

            Texture cueTexture = assetLoader.getCueTexture();
            CueFactory cueFactory = new CueFactory(cueTexture, assetLoader);

            List<Texture> ballTextures = assetLoader.getBallTextures();
            BallFactory ballFactory = new BallFactory(ballTextures, assetLoader);

            Texture tableTexture = assetLoader.getTableTexture();
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
        // Create new game with local scene & GDX input
        game = Game.createNewGame(scene, Gdx.input);

        // Observe the created Game
        game.addObserver(this);

        // Start the game
        game.startGame();
    }

    /**
     * Updates the scene & game for the current render iteration.
     * Handles rendering the scene and advancing the game loop.
     * @param deltaTime deltaTime, time between the last and current frame.
     */
    private void update(float deltaTime) {
        // Render the scene only if the game is loaded
        if (loaded) {
            // Advance the game loop of the game & render scene
            game.advanceGameLoop(deltaTime);
            scene.render();
        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Scene3D getScene() {
        return scene;
    }

    /**
     * Render the scene.
     * @param delta Time in seconds between last render.
     */
    @Override
    public void render(float delta) {
        // Initialize scene if uninitialized
        initializeScene();

        // Clear depth buffer & color buffer masks
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Update the scene & game for the current iteration
        update(delta);
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void onGameStarted() {

    }

    @Override
    public void onBallPotted(Ball3D ball) {

    }

    @Override
    public void onMotion() {

    }

    @Override
    public void onMotionStop() {

    }

    @Override
    public void onGameEnded() {
        // Go back to login screen when Game is ended
        // TODO: Change to stats/leaderboards screen
        mainGame.create();
    }
}