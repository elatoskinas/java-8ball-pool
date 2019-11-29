package com.sem.pool;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Scene3D;

public class Game {
    private transient Scene3D scene;
    private transient Input input;
    private transient GameState state;

    /**
     * Constructs a new Game object with the given scene, input, and state.
     * @param scene The scene of the game.
     * @param input The input the game should listen to.
     * @param state The state of the game.
     */
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
