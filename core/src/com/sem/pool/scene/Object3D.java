package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Abstract class used to generalize all 3D Objects used in the game.
 */
public abstract class Object3D {
    
    protected transient ModelInstance model;

    /**
     * Creates a default 3D Object with no internal parameters
     * adjusted. To be used only in private/protected contexts
     * for further extensions.
     */
    protected Object3D() {
        
    }

    /**
     * Constructs a new Object3D instance.
     * @param model The model that is to be used.
     */
    public Object3D(ModelInstance model) {
        this.model = model;
    }
    
    public ModelInstance getModel() {
        return model;
    }

    /**
     * Returns the current coordinates of the ball.
     * @return The coordinates of the ball.
     */
    public Vector3 getCoordinates() {
        return new Vector3(this.model.transform.getTranslation(new Vector3()));
    }
}
