package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;

/**
 * Class used for loading assets to the scene.
 * Simplifies the usage of the AssetManager for loading scenes
 * by providing a simpler to use interface.
 */
public class AssetLoader {
    // Paths to models for loading
    protected static final String BALL_MODEL_PATH = "models/ball.obj";
    protected static final String TABLE_MODEL_PATH = "models/table.obj";

    // Parameters for loading 3D models
    private transient ObjLoader.ObjLoaderParameters objectLoaderParameters;

    // Underlying AssetManager (LibGDX dependency) to use
    // for loading assets
    private transient AssetManager assetManager;

    /**
     * Enum to represent loadable model types.
     * Simplifies the specification of paths for models to load.
     */
    public enum ModelType {
        BALL(BALL_MODEL_PATH),
        TABLE(TABLE_MODEL_PATH);

        private String path;

        private ModelType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * Creates a new Asset Loader from the specified Asset Manager
     * object instance.
     * @param assetManager  Asset Manager to use to load assets (LibGDX dependency)
     */
    public AssetLoader(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Initialize object loader parameters to flip the texture coordinates.
        // Otherwise, the Blender object textures will appear wrong due to
        // the textures being flipped by default in LibGDX.
        objectLoaderParameters = new ObjLoader.ObjLoaderParameters(true);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ObjLoader.ObjLoaderParameters getObjectLoaderParameters() {
        return objectLoaderParameters;
    }

    /**
     * Initializes all of the models of the AssetLoader.
     * To be called during the create() stage of the game lifecycle.
     */
    // type.getPath() in the loop seems to cause an UR anomaly.
    // Using a value other than the ModelType's getPath seems
    // to not cause the issue. Thus, we suppress it here.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void initializeModels() {
        for (ModelType type : ModelType.values()) {
            assetManager.load(type.getPath(), Model.class, objectLoaderParameters);
        }
    }

    /**
     * Creates a model of the specified type.
     * @param type  Type of the model to load
     * @return  new Model Instance corresponding to the specified type.
     */
    public ModelInstance loadModel(ModelType type) {
        Model model = assetManager.get(type.getPath(), Model.class);

        return new ModelInstance(model);
    }

    /**
     * Disposes the asset loader. To be used when the lifecycle of
     * an application using the asset loader ends.
     */
    public void dispose() {
        assetManager.dispose();
    }
}
