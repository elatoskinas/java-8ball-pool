package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Class representing a 3D Board of a single game.
 */
public class Board3D {
    private transient ModelInstance model;

    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Board3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }
}
