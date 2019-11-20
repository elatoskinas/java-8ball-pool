package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.List;

/**
 * 3D Scene representation of a single Pool game.
 */
public class Scene3D {
    private AssetLoader assetLoader;
    private transient ModelBatch modelBatch;

    private Environment environment;
    private Camera camera;
    private List<ModelInstance> models;

    public Scene3D(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
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

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<ModelInstance> getModels() {
        return models;
    }
}
