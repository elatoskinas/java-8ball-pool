package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.Model;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public class Ball3D {
    private int id;
    private Model model;

    /**
     * Constructs a new 3D Pool Ball instance with
     * the specified id and model.
     * @param id  ID of the ball
     * @param model  Model object of the ball
     */
    public Ball3D(int id, Model model) {
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
    public Model getModel() {
        return model;
    }
}
