package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

import java.util.List;

/**
 * Factory class which allows the instantiation
 * of Ball3D objects from the specified texture set.
 */
public class BallFactory {
    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.BALL;

    private List<Texture> textures;
    private AssetLoader assetLoader;

    /**
     * Creates a new Ball Factory instance with the specified
     * textures. The indices of the provided List directly
     * correspond to the Ball IDs.
     * E.g. 0 - cue ball, 1 - solid 1, 9 - striped 1, etc.
     *
     * @param textures  List of textures for the balls
     */
    public BallFactory(List<Texture> textures, AssetLoader assetLoader) {
        this.textures = textures;
        this.assetLoader = assetLoader;
    }

    public Ball3D createBall(int id) {
        ModelInstance ballInstance = assetLoader.loadModel(MODEL_TYPE);

        // TODO: Set texture according to id

        return new Ball3D(id, ballInstance);
    }
}
