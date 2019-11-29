package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Class representing a 3D Cue.
 * TODO: Rotation of the cue.
 * TODO: Set position according to the force.
 */
public class Cue3D {

    /**
     * The distance between the center of the cueBall and the tip of the cue.
     */
    private static final float cueRadius = 0.2f;

    private transient ModelInstance model;

    public Cue3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }


    /**
     * Set the cue left from the cue ball.
     * @param cueBall Ball3D cue ball to determine position.
     */
    public void toShotPosition(Ball3D cueBall) {
        Vector3 position = cueBall.getModel().transform.getTranslation(new Vector3());
        model.transform.translate(position.x - cueRadius, position.y + 0.1f, position.z);
    }
}
