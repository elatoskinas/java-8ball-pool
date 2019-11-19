package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public class Ball3D {
    private int id;
    private ModelInstance model;

    /**
     * Constructs a new 3D Pool Ball instance with
     * the specified id and model.
     * @param id  ID of the ball
     * @param model  Model object of the ball
     */
    public Ball3D(int id, ModelInstance model) {
        this.id = id;
        this.model = model;
    }

    /**
     * @return  ID of the 3D Ball
     */
    public int getId() {
        return id;
    }

    /**
     * @return  3D Ball model object
     */
    public ModelInstance getModel() {
        return model;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Ball3D) {
            Ball3D otherBall = (Ball3D) other;

            return this.id == otherBall.id
                    && this.model.equals(otherBall.model);
        }

        return false;
    }
}
