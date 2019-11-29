package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

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
    public Cue3D createCue() {
        ModelInstance cueInstance = assetLoader.loadModel(MODEL_TYPE);

        Cue3D cue = new Cue3D(cueInstance);
        return cue;
    }
}
