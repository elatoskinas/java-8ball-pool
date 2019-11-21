package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.sem.pool.scene.AssetLoader;
import com.sem.pool.scene.BallFactory;
import com.sem.pool.scene.Scene3D;
import com.sem.pool.scene.SceneFactory;
import com.sem.pool.scene.TableFactory;

import java.util.ArrayList;

public class Pool extends ApplicationAdapter {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;

    private transient boolean loaded;

    @Override
    public void create() {
        initializeAssetLoader();

        // Initialize model batch for rendering
        modelBatch = new ModelBatch();

        // Create scene
        //scene = new Scene3D(assetLoader, batch);
    }

    private void initializeAssetLoader() {
        // Initialize objects to handle asset loading
        AssetManager manager = new AssetManager();
        assetLoader = new AssetLoader(manager);

        // Initialize models by queueing them for loading
        assetLoader.initializeModels();

        // Initialize viewport to the relevant width & height
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

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
    }
}