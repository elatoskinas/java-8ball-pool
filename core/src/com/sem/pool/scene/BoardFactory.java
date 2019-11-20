package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Factory class which allows the instantiation
 * of Board3D objects from the specified texture.
 */
public class BoardFactory {
    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.BOARD;

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

    /**
     * Creates a 3D Board object instance.
     * The appearance of the board is set accordingly to the
     * internal texture of the BoardFactory class.
     * @return  New Board3D object instance corresponding to the specified id
     */
    public Board3D createBoard() {
        ModelInstance boardInstance = assetLoader.loadModel(MODEL_TYPE);

        // TODO: Set texture accordingly

        return new Board3D(boardInstance);
    }
}
