package com.sem.pool.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.sem.pool.scene.Cue3D;

public class CueFactory extends Base3DFactory {

    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.CUE;
    
    private Texture texture;

    /**
     * Creates a new Cue Factory instance with the specified
     * texture.
     *
     * @param texture  Texture to use for the cue
     */
    public CueFactory(Texture texture, AssetLoader assetLoader) {
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
     * Creates a Cue3D object instance.
     * @return  New Cue3D object instance
     */
    @Override
    public Cue3D createObject() {

        // TODO: Set texture for personalization

        ModelInstance cueInstance = assetLoader.loadModel(MODEL_TYPE);

        return new Cue3D(cueInstance);
    }
}
