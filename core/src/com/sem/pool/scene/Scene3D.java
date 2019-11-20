package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Scene representation of a single Pool game.
 */
public class Scene3D {
    // TODO: Change value to support all 16 balls
    private static final int ballCount = 2;

    private AssetLoader assetLoader;
    private transient ModelBatch modelBatch;

    private transient Environment environment;
    private transient Camera camera;
    private transient List<ModelInstance> models;

    private transient List<Ball3D> poolBalls;
    private transient Table3D table;

    /**
     * Creates an instance of a 3D Pool Game scene.
     * The models for the scene will be loaded via the specified asset loader,
     * and rendered on the specified ModelBatch.
     * @param assetLoader  Asset Loader to use for loading assets
     * @param batch  Model Batch to use for rendering
     */
    public Scene3D(AssetLoader assetLoader, ModelBatch batch) {
        this.assetLoader = assetLoader;
        this.modelBatch = batch;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public void setAssetLoader(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Camera getCamera() {
        return camera;
    }

    public List<ModelInstance> getModels() {
        return models;
    }

    public List<Ball3D> getPoolBalls() {
        return poolBalls;
    }

    public Table3D getTable() {
        return table;
    }

    /**
     * Instantiates the 3D scene by setting up the environment, camera
     * and models. The method instantiates all the necessary models,
     * positions them in the necessary locations and sets the camera
     * in its right location.
     */
    public void instantiate() {
        environment =  new Environment();
        camera = new PerspectiveCamera();
        models = new ArrayList<>();
        poolBalls = new ArrayList<>();

        ArrayList<Texture> ballTexures = new ArrayList<Texture>();
        BallFactory ballFactory = new BallFactory(ballTexures, assetLoader);

        for (int i = 0; i < ballCount; ++i) {
            Ball3D ball = ballFactory.createBall(i);
            models.add(ball.getModel());
        }

        Texture tableTexture = null;
        TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);
        table = tableFactory.createBoard();

        models.add(table.getModel());
    }
}
