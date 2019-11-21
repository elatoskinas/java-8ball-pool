package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class SceneFactory {
    private static final int ballCount = 16;

    private transient TableFactory tableFactory;
    private transient BallFactory ballFactory;
    private transient Camera camera;
    private transient ModelBatch modelBatch;

    public SceneFactory(TableFactory tableFactory, BallFactory ballFactory,
                        Camera camera, ModelBatch modelBatch) {

    }
}
