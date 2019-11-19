package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

/**
 * Factory class which allows the instantiation
 * of Ball3D objects from the specified texture set.
 */
public class BallFactory {
    private List<Texture> textures;

    /**
     * Creates a new Ball Factory instance with the specified
     * textures. The indices of the provided List directly
     * correspond to the Ball IDs.
     * E.g. 0 - cue ball, 1 - solid 1, 9 - striped 1, etc.
     *
     * @param textures  List of textures for the balls
     */
    public BallFactory(List<Texture> textures) {
        this.textures = textures;
    }
}
