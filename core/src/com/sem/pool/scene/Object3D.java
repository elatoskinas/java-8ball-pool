package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Abstract class used to generalize all 3D Objects used in the game
 */
public abstract class Object3D {
    
    protected transient ModelInstance model;
    
    public Object3D(ModelInstance model) {
        this.model = model;
    }
    
    public ModelInstance getModel(){
        return model;
    }
}
