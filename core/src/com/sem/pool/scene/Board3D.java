package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Board3D {
    private transient ModelInstance model;

    public Board3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }
}
