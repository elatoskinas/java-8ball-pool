package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Class representing a 3D Cue.
 */
public class Cue3D {

    /**
     * The distance between the center of the cueBall and the tip of the cue.
     */
    private static final float cueRadius = 0.3f;

    private transient ModelInstance model;

    public Cue3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }

    public void toShotPosition(Ball3D cueBall) {
        Vector3 position = cueBall.getModel().transform.getTranslation(new Vector3());
        model.transform.translate(position.x - cueRadius, position.y, position.z);
    }
}
