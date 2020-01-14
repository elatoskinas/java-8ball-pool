package com.sem.pool.factories;

import com.sem.pool.scene.Object3D;

/**
 * Factory class intended for instantiation of
 * 3D objects of the Pool game.
 */
public abstract class Base3DFactory {
    
    protected static AssetLoader.ModelType MODEL_TYPE = null;

    protected final transient AssetLoader assetLoader;

    /**
     * Creates a Factory object instance used to instantiate models.
     * Uses the provided asset loader for loading models.
     * @param assetLoader  Asset Loader to use for model loading.
     */
    protected Base3DFactory(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    /**
     * Abstract factory method that is to implemented by child classes.
     * @return The crated Object3D.
     */
    public abstract Object3D createObject();
}
