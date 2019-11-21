package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.sem.pool.scene.AssetLoader;
import com.sem.pool.scene.Scene3D;

public class Pool extends ApplicationAdapter {
    private transient AssetLoader assetLoader;

    private transient Scene3D scene;

    @Override
    public void create() {
        initializeAssetLoader();

        // Initialize model batch for rendering
        ModelBatch batch = new ModelBatch();

        // Create scene
        scene = new Scene3D(assetLoader, batch);
    }

    private void initializeAssetLoader() {
        // Initialize objects to handle asset loading
        AssetManager manager = new AssetManager();
        assetLoader = new AssetLoader(manager);

        // Initialize models by queueing them for loading
        assetLoader.initializeModels();
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Scene3D getScene() {
        return scene;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {

    }
}