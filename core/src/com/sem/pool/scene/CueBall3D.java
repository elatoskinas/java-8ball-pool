package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

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
}
