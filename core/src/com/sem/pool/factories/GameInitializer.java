package com.sem.pool.factories;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.game.Game;
import com.sem.pool.scene.Scene3D;
import com.sem.pool.screens.MainGame;

/**
 * Class that abstracts the functionality of initializing the
 * Game and providing an interface to creating new factories.
 */
public class GameInitializer {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Input input;
    private transient Vector2 resolution;
    private transient Vector3 cameraPosition;
    private transient CameraCreator cameraCreator;
    private transient MainGame mainGame;

    /**
     * Creates a new Game Initializer object with the specified
     * parameters for loading assets and rendering models.
     * @param assetLoader     AssetLoader for loading assets
     * @param modelBatch      ModelBatch for rendering 3D objects
     * @param resolution      Resolution of the game as a 2D vector
     * @param cameraPosition  Position of the camera as a 3D vector
     */
    public GameInitializer(AssetLoader assetLoader, ModelBatch modelBatch,
                           Input input, Vector2 resolution, Vector3 cameraPosition, MainGame mainGame) {
        this.assetLoader = assetLoader;
        this.modelBatch = modelBatch;
        this.input = input;
        this.resolution = resolution;
        this.cameraPosition = cameraPosition;
        this.cameraCreator = new CameraCreator();
        this.mainGame = mainGame;
    }

    public void setCameraCreator(CameraCreator creator) {
        this.cameraCreator = creator;
    }

    /**
     * Creates a new Camera Factory instance.
     */
    public CameraFactory createCameraFactory() {
        CameraFactory cameraFactory = new CameraFactory(this.resolution.x,
                this.resolution.y, this.cameraPosition);
        cameraFactory.setCameraCreator(cameraCreator);
        return cameraFactory;
    }

    /**
     * Creates a new instance of a Ball Factory object used for instantiating balls.
     * Uses the internal asset loader.
     * @return  new Ball Factory object instance
     */
    public BallFactory createBallFactory() {
        return new BallFactory(this.assetLoader.getBallTextures(), this.assetLoader);
    }

    /**
     * Creates a new instance of a Cue Factory object used for instantiating cues.
     * Uses the internal asset loader.
     * @return  new Cue Factory object instance
     */
    public CueFactory createCueFactory() {
        return new CueFactory(this.assetLoader.getCueTexture(), this.assetLoader);
    }

    /**
     * Creates a new instance of a Table Factory object used for instantiating tables.
     * Uses the internal asset loader.
     * @return  new Table Factory object instance
     */
    public TableFactory createTableFactory() {
        return new TableFactory(this.assetLoader.getTableTexture(), this.assetLoader);
    }

    /**
     * Creates a new instnace of a Scene Factory object used for instantiating scenes.
     * Uses the internal asset loader.
     * @return  new Scene Factory object instance
     */
    public SceneFactory createSceneFactory() {
        return new SceneFactory(createTableFactory(), createBallFactory(),
                                createCameraFactory(), createCueFactory(), this.modelBatch);
    }

    /**
     * Creates a new Game with its own Scene.
     * @return  object of new Game.
     */
    public Game createGame() {
        // Creates new scene
        Scene3D scene = createSceneFactory().createScene();

        // Create & return new game
        return Game.createNewGame(scene, input, mainGame);
    }
}