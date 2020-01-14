package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.sem.pool.game.GameConstants;

/**
 * Ball3D with id between 1 - 7 and 9 - 15 which represents either a full or striped ball.
 */
public class RegularBall3D extends Ball3D {

    public enum Type {
        STRIPED,
        FULL,
        UNASSIGNED
    }

    private final transient Type type;

    /**
     * Constructs a new 3D Pool Full Ball instance with
     * the specified id and model.
     *
     * @param id    ID of the ball
     * @param model Model object of the ball
     */
    public RegularBall3D(int id, ModelInstance model) {
        super(id, model);
        if (id < GameConstants.EIGHTBALL_ID) {
            this.type = Type.FULL;
        } else {
            this.type = Type.STRIPED;
        }
    }

    /**
     * Returns the type of the ball.
     *
     * @return Type, the type of the ball.
     */
    public Type getType() {
        return this.type;
    }
}
