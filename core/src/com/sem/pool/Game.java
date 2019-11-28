package com.sem.pool;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Scene3D;

public class Game {
    Scene3D scene;
    Input input;
    GameState state;
    
    public Game(Scene3D scene, Input input, GameState state) {
        this.scene = scene;
        this.input = input;
        this.state = state;
    }

    public void moveBalls() {
        // TODO: Implement logic that handles all ball movement every frame
    }
    
    public void respondToInput() {
        // TODO: Implement logic to respond to input every frame
    }
    
    public Scene3D getScene() {
        return scene;
    }

    public Input getInput() {
        return input;
    }

    public GameState getState() {
        return state;
    }
}
