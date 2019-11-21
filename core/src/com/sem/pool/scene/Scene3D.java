package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Scene representation of a single Pool game.
 */
public class Scene3D {
    // TODO: Change value to support all 16 balls
    private static final int ballCount = 16;

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
    // ballFactory variable gets tagged as a DU anomaly, even
    // though it is initialized and used to create the pool
    // balls properly. Seems like a false positive here.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void instantiate() {
        // TODO: Move this to it's own Environment factory or method.
        environment =  new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -1f, 0f));

        // TODO: Move this to it's own CameraFactory class (or separate method)
        // TODO: For now, this is only a placeholder to be able to minimally system test.
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 5f, 0f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        models = new ArrayList<>();
        poolBalls = new ArrayList<>();

        ArrayList<Texture> ballTexures = new ArrayList<Texture>();

        BallFactory ballFactory = new BallFactory(ballTexures, assetLoader);

        for (int i = 0; i < ballCount; ++i) {
            Ball3D ball = ballFactory.createBall(i);
            models.add(ball.getModel());

            // TODO: Temporary code to randomly spread out the
            // TODO: Initialized balls. To be replaced with
            // TODO: proper positioning of the balls later on.
            // TODO: Here, we also move the cue ball further away
            // TODO: with the intention of easier integration testing if needed.
            float xtranslate = i * (float) Math.random() * 0.2f;
            float ztranslate = i * (float) Math.random() * 0.1f;

            if (i == 0) {
                xtranslate = -1f;
                ztranslate = 0f;
            }

            ball.getModel().transform.translate(xtranslate, 0, ztranslate);
        }

        Texture tableTexture = null;
        TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);
        table = tableFactory.createBoard();

        models.add(table.getModel());
    }

    /**
     * Renders the scene with the scene's models, environment
     * and camera. Should be called on every game loop iteration.
     */
    public void render() {
        modelBatch.begin(camera);
        modelBatch.render(models, environment);
        modelBatch.end();
    }

    /** Disposes & completely cleans up the scene of models.
     * To be used when the lifecycle of the game making use
     * of the 3D scene ends.
     */
    public void dispose() {
        modelBatch.dispose();
        models.clear();
        assetLoader.dispose();
    }
}
