package com.sem.pool.factories;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Class that abstracts the functionality of initializing the
 * Game and providing an interface to creating new factories.
 */
public class GameInitializer {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Vector2 resolution;
    private transient Vector3 cameraPosition;

    /**
     * Creates a new Game Initializer object with the specified
     * parameters for loading assets and rendering models.
     * @param assetLoader     AssetLoader for loading assets
     * @param modelBatch      ModelBatch for rendering 3D objects
     * @param resolution      Resolution of the game as a 2D vector
     * @param cameraPosition  Position of the camera as a 3D vector
     */
    public GameInitializer(AssetLoader assetLoader, ModelBatch modelBatch,
                           Vector2 resolution, Vector3 cameraPosition) {
        this.assetLoader = assetLoader;
        this.modelBatch = modelBatch;
        this.resolution = resolution;
        this.cameraPosition = cameraPosition;
    }

    /**
     * Creates a new Camera Factory instance.
     */
    public CameraFactory createCameraFactory() {
        return new CameraFactory(this.resolution.x, this.resolution.y, this.cameraPosition);
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
}