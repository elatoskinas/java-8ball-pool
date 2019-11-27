package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Factory class which allows the instantiation
 * of Table3D objects from the specified texture.
 */
public class TableFactory extends Base3DFactory {
    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.TABLE;

    private Texture texture;

    /**
     * Creates a new Board Factory instance with the specified
     * texture.
     *
     * @param texture  Texture to use for the board
     */
    public TableFactory(Texture texture, AssetLoader assetLoader) {
        super(assetLoader);
        this.texture = texture;
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
     * internal texture of the TableFactory class.
     * @return  New Table3D object instance corresponding to the specified id
     */
    public Table3D createTable() {
        ModelInstance boardInstance = assetLoader.loadModel(MODEL_TYPE);
        // TODO: Set texture accordingly
        boardInstance.transform.translate(0, -1f, 0);

        return new Table3D(boardInstance);
    }
}
