package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Cue3D {

    private transient ModelInstance model;

    public Cue3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }

    public void toShotPosition(Ball3D cueBall){
        Vector3 position = cueBall.getModel().transform.getTranslation(new Vector3());
        model.transform.translate(position);
    }
}
