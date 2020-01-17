package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Ball3D with id 0 which represents the cue (white) ball.
 */
public class CueBall3D extends Ball3D {
    /**
     * Constructs a new 3D Pool CueBall instance with
     * the specified id and model.
     *
     * @param id    ID of the ball
     * @param model Model object of the ball
     */
    public CueBall3D(int id, ModelInstance model) {
        super(id, model);
    }

    /**
     * Pot method for a ball.
     * This overrides the default behavior, so the cue resets if potted.
     */
    @Override
    public void pot() {
        this.setSpeed(0);
        this.setDirection(new Vector3());
    }
}
