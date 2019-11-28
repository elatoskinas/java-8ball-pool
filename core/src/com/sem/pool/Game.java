package com.sem.pool;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Scene3D;

public class Game {
    Scene3D scene;
    Input input;
    
    public Game(Scene3D scene, Input input) {
        this.scene = scene;
        this.input = input;
    }

    public Scene3D getScene() {
        return scene;
    }

    public Input getInput() {
        return input;
    }
}
