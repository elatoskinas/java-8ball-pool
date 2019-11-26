package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.scene.*;

import java.util.ArrayList;

/**
 * Main Pool Game application class that handles
 * the 3D pool scene and all the interactions.
 * TODO: Split this off into smaller components?
 */
public class Pool extends ApplicationAdapter {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;

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
        assetLoader.initializeModels();
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
            // TODO: Move this to it's own CameraFactory class (or separate method)
            // TODO: For now, this is only a placeholder to be able to minimally system test.
            Camera camera = new PerspectiveCamera(67,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.position.set(0f, 5f, 0f);
            camera.lookAt(0,0,0);
            camera.near = 1f;
            camera.far = 300f;

            ArrayList<Texture> ballTexures = new ArrayList<Texture>();
            BallFactory ballFactory = new BallFactory(ballTexures, assetLoader);

            Texture tableTexture = null;
            TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);

            SceneFactory sceneFactory = new SceneFactory(tableFactory,
                    ballFactory, camera, modelBatch);

            // Instantiate the scene
            scene = sceneFactory.createScene();

            // Update the camera of the scene to point to the right location
            scene.getCamera().update();

            // The assets of the game are now fully loaded
            loaded = true;
        }
    }

    /**
     * Renders the scene only if the scene has finished loading.
     */
    private void renderScene() {
        // Render the scene only if the game is loaded
        if (loaded) {
            scene.render();
         Ball3D ball = getScene().getPoolBalls().get(0);
         ball.move(new Vector3(0.005f,0,0));
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
        // Initialize scene if uninitialized
        initializeScene();

        // Clear depth buffer & color buffer masks
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render the scene if initialized
        renderScene();
        //float[] coordinates = getScene().getPoolBalls().get(0).getModel().transform.getValues();
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();
    }
}