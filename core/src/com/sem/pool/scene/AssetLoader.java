package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class AssetLoader {
    public static final String BALL_MODEL_PATH = "models/ball.obj";
    public static final String BOARD_MODEL_PATH = "models/board.obj";

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

    private AssetManager assetManager;

    public AssetLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void initializeModels() {
        for (ModelType type : ModelType.values()) {
            assetManager.load(type.path, Model.class);
        }
    }

    public ModelInstance loadModel(ModelType type) {
        Model model = assetManager.get(type.path, Model.class);

        return new ModelInstance(model);
    }
}
