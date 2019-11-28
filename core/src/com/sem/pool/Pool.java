package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.scene.AssetLoader;
import com.sem.pool.scene.BallFactory;

import com.sem.pool.scene.CameraFactory;
import com.sem.pool.scene.Scene3D;
import com.sem.pool.scene.SceneFactory;
import com.sem.pool.scene.TableFactory;

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
    static final Vector3 cameraPosition = new Vector3(0f,100f,0f);
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


            List<Texture> ballTextures = assetLoader.getBallTextures();
            BallFactory ballFactory = new BallFactory(ballTextures, assetLoader);

            Texture tableTexture = null;
            TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);

            SceneFactory sceneFactory = new SceneFactory(tableFactory,
                    ballFactory, cameraFactory, modelBatch);

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
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();
    }
}