package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;

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

    public AssetLoader() {
        this.assetManager = new AssetManager();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
