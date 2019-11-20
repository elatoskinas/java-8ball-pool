package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class AssetLoader {
    public static final String BALL_MODEL_PATH = "models/ball.obj";
    public static final String BOARD_MODEL_PATH = "models/board.obj";

    /**
     * Enum to represent loadable model types.
     */
    public enum ModelType {
        BALL(BALL_MODEL_PATH),
        BOARD(BOARD_MODEL_PATH);

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
    }

    public AssetManager getAssetManager() {
        return assetManager;
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
            assetManager.load(type.getPath(), Model.class);
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
}
