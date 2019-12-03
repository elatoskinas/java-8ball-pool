package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.sem.pool.game.GameConstants;

public class RegularBall3D extends Ball3D {
    transient boolean isFull;

    /**
     * Constructs a new 3D Pool Full Ball instance with
     * the specified id and model.
     *
     * @param id    ID of the ball
     * @param model Model object of the ball
     */
    public RegularBall3D(int id, ModelInstance model) {
        super(id, model);
        isFull = id < GameConstants.EIGHTBALL_ID;
    }

    /**
     * Returns whether the ball is a Full Ball or a Striped Ball.
     * @return isFull   Boolean that holds if the ball is full or striped.
     */
    public boolean getIsFull() {
        return isFull;
    }
}
