package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.ResultController;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.User;

import com.sem.pool.factories.AssetLoader;
import com.sem.pool.factories.GameInitializer;
import com.sem.pool.game.Game;
import com.sem.pool.game.Player;
import com.sem.pool.scene.Scene3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Pool Game application class that handles
 * the 3D pool scene and all the interactions.
 * TODO: Split this off into smaller components?
 */
public class Pool extends GameScreen {
    private static final Vector3 CAMERA_POSITION = new Vector3(0f, 100f, 0f);

    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;
    private transient Game poolGame;
    private transient GameUI gameUI;
    private transient boolean loaded;

    /**
     * Creates an instance of the Pool game screen,
     * and handles all of the initialization for the
     * game.
     */
    public Pool(MainGame game) {
        super(game);

        initializeAssetLoader();
        // Initialize model batch for rendering
        modelBatch = new ModelBatch();

        // Initialize viewport to the relevant width & height
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

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
                Gdx.input, resolution, CAMERA_POSITION);

        // Create the players
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(this.game.getPlayer().getUserID()));
        players.add(new Player(this.game.getOpponent().getUserID()));

        // Instantiate the game & retrieve the scene from the game
        this.poolGame = gameInitializer.createGame(players);
        this.scene = this.poolGame.getScene();

        // Update the camera of the scene to point to the right location
        this.scene.getCamera().update();

        // Make the Pool game observe the Game loop class to receive events.
        this.poolGame.addObserver(this);

        // Start the game.
        this.poolGame.startGame();

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
        if (!loaded) {
            return;
        }

        // Advance the game loop of the game & render scene
        this.poolGame.advanceGameLoop(deltaTime);
        this.scene.render();
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
            this.initializeScene();
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
            gameUI.updatePlayerTurnLabel(this.poolGame);
            gameUI.updateBallTypeLabels(this.poolGame);
        }

        gameUI.render();
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();
    }

    @Override
    public void show() {
        gameUI = new GameUI();
        gameUI.createUI();
        gameUI.render();
    }

    @Override
    public void onGameEnded(Player winnerPlayer, List<Player> players) {
        // Store the result.
        UserController userController = new UserController(Database.getInstance());
        ResultController resultController = new ResultController(Database.getInstance());

        User winner = userController.getUser(winnerPlayer.getId());
        User loser = winnerPlayer.getId() == this.game.getPlayer().getUserID()
                ? this.game.getOpponent()
                : this.game.getPlayer();
        this.game.setWinner(winner);

        resultController.createResult(winner, loser);

        // Go to the leaderboard screen.
        this.game.setScreen(new Leaderboard(this.game));
    }

    @Override
    public void pause() {
        System.out.println("Pausing game...");
    }

    @Override
    public void resume() {
        System.out.println("Resuming game");
    }
}