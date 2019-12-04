package com.sem.pool.game;

import com.badlogic.gdx.Input;

import com.sem.pool.scene.Scene3D;

/**
 * Class that handles everything related to the pool game.
 * TODO: This is currently only a template, no functionality has been implemented as of yet.
 */
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

    public Scene3D getScene() {
        return scene;
    }

    public Input getInput() {
        return input;
    }

    public GameState getState() {
        return state;
    }

    /**
     * Moves the balls according to our physics implementation. This method will be called
     * every frame in the render() method.
     */
    public void moveBalls() {
        // TODO: Implement logic that handles all ball movement every frame
    }

    /**
     * Method to handle any input by the player(s), should ignore input if invalid.
     */
    public void respondToInput() {

    }
    

}
