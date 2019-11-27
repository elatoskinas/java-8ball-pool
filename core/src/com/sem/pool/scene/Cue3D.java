package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Cue3D {

    private transient ModelInstance model;

    public Cue3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }
}
