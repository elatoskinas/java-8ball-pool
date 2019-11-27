package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class CueFactory extends Base3DFactory {

    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.CUE;

    private Texture texture;


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


    public Cue3D createCue() {
        ModelInstance cueInstance = assetLoader.loadModel(MODEL_TYPE);

        // TODO: Set texture accordingly
        Cue3D cue = new Cue3D(cueInstance);
        cue.getModel().transform.scale(0.1f,0.1f,0.1f);
        cue.getModel().transform.translate(0,1.4f,0);
        return cue;
    }
}
