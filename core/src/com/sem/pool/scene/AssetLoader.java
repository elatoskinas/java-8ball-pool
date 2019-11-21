package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;

public class AssetLoader {
    public static final String BALL_MODEL_PATH = "models/ball.obj";
    public static final String TABLE_MODEL_PATH = "models/table.obj";

    private transient ObjLoader.ObjLoaderParameters objectLoaderParameters;

    /**
     * Enum to represent loadable model types.
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

    private transient AssetManager assetManager;

    public AssetLoader(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Iniitalize object loader parameters to flip the texture coordinates
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
            assetManager.load(type.getPath(), Model.class, new ObjLoader.ObjLoaderParameters(true));
        }
    }

    /**
     * Loads the model of the specified type.
     * @param type  Type of the model to load
     * @return  Model Instance of the specified type.
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
