package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
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

    public void setModels(List<ModelInstance> models) {
        this.models = models;
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
    }
}
