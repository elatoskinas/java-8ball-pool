package com.sem.pool;

import com.badlogic.gdx.graphics.g3d.Model;

public class Ball3D {
    private int id;
    private Model model;

    public Ball3D(int id, Model model) {
        this.id = id;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public Model getModel() {
        return model;
    }
}
