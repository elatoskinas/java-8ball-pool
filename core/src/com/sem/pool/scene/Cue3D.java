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
    private static final float cueOffset = 0.05f;

    // The Y of the cue can't be 0 because it will end up in the bumpers.
    private static final float yCoordinate = 0.1f;
    private transient ModelInstance model;

    /**
     * Constructs a new 3D Cue instance.
     */
    public Cue3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }


    /**
     * Set the cue near the cue ball.
     * @param cueBall Ball3D cue ball to determine position.
     */
    public void toShotPosition(Ball3D cueBall) {
        Vector3 position = cueBall.getModel().transform.getTranslation(new Vector3());

        float x = position.x - cueOffset + cueBall.getRadius();
        model.transform.translate(x, position.y + yCoordinate, position.z);
    }
}
