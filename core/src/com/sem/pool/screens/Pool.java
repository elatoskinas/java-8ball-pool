package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;

import com.sem.pool.factories.AssetLoader;

import com.sem.pool.factories.GameInitializer;
import com.sem.pool.game.Game;
import com.sem.pool.game.GameObserver;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.GameUI;
import com.sem.pool.scene.Scene3D;

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
    private static final Vector3 CAMERA_POSITION = new Vector3(0f, 100f, 0f);
    private transient Game game;

    private transient GameUI gameUI;

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

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Scene3D getScene() {
        return scene;
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
        // Retrieve resolution of the game from inner GDX graphics parameters.
        Vector2 resolution = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create a Game initializer with the Game's internal parameters
        // required for making a Game.
        GameInitializer gameInitializer = new GameInitializer(assetLoader, modelBatch,
                Gdx.input, resolution, CAMERA_POSITION, mainGame);

        // Instantiate the game & retrieve the scene from the game
        game = gameInitializer.createGame();
        scene = game.getScene();

        // Update the camera of the scene to point to the right location
        scene.getCamera().update();

        // Make the Pool game observe the Game loop class to receive events.
        game.addObserver(this);

        // Start the Game.
        game.startGame();

        // The assets of the game are now fully loaded
        loaded = true;
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

    /**
     * Render the scene and loads UI elements.
     * @param delta Time in seconds between last render.
     */
    @Override
    public void render(float delta) {
        // If the game has not yet been loaded, and an
        // assetLoader update event is received in current iteration,
        // then load the game.
        if (!loaded && assetLoader.getAssetManager().update()) {
            initializeScene();
        }

        // Clear depth buffer & color buffer masks
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Update the scene & game for the current iteration
        update(delta);

        updateUI();
    }

    /**
     * Updates the UI elements and renders them.
     */
    public void updateUI() {
        if (loaded) {
            gameUI.updateForceLabel(scene);
            gameUI.updatePlayerTurnLabel(game);
            gameUI.updateBallTypeLabels(game);
        }
        gameUI.render();
    }

    /**
     * Create gameUI and shows all elements.
     */
    public void showUI() {
        gameUI = new GameUI();

        gameUI.createUI(game);

        gameUI.render();
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
        showUI();
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
    public void onMotionStop(Ball3D lastTouched) {

    }

    @Override
    public void onGameEnded() {
        // Go back to login screen when Game is ended
        // TODO: Change to stats/leaderboards screen
        mainGame.create();
    }
}