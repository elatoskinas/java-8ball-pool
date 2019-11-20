package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;

/**
 * Factory class which allows the instantiation
 * of Board3D objects from the specified texture.
 */
public class BoardFactory {
    private Texture texture;
    private transient AssetLoader assetLoader;

    /**
     * Creates a new Board Factory instance with the specified
     * texture.
     *
     * @param texture  Texture to use for the board
     */
    public BoardFactory(Texture texture, AssetLoader assetLoader) {
        this.texture = texture;
        this.assetLoader = assetLoader;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
