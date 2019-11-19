package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.List;

/**
 * Factory class which allows the instantiation
 * of Ball3D objects from the specified texture set.
 */
public class BallFactory {
    protected static final String MODEL_PATH = "models/ball.obj";

    private List<Texture> textures;
    private AssetManager assetManager;

    /**
     * Creates a new Ball Factory instance with the specified
     * textures. The indices of the provided List directly
     * correspond to the Ball IDs.
     * E.g. 0 - cue ball, 1 - solid 1, 9 - striped 1, etc.
     *
     * @param textures  List of textures for the balls
     */
    public BallFactory(List<Texture> textures, AssetManager assetManager) {
        this.textures = textures;
        this.assetManager = assetManager;
    }

    public Ball3D createBall(int id) {
        Model ballModel = assetManager.get(MODEL_PATH, Model.class);
        ModelInstance ballInstance = new ModelInstance(ballModel);

        Ball3D ball = new Ball3D(id, ballModel);

        return ball;
    }
}
