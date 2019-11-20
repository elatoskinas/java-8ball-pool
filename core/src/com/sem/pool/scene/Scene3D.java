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
    private ModelBatch modelBatch;
    private Environment environment;
    private Camera camera;
    private AssetLoader assetLoader;
    private List<ModelInstance> models;
}
