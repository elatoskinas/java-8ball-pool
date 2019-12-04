package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Scene3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameTest {
    Scene3D scene;
    Input input;
    GameState gameState;
    Game game;

    @BeforeEach
    void setUp() {
        scene = Mockito.mock(Scene3D.class);
        input = Mockito.mock(Input.class);
        gameState = Mockito.mock(GameState.class);
        game = new Game(scene, input, gameState);
    }

    /**
     * Test method to verify that the Game object is instantiated correctly.
     * (Scene and input are assigned without error)
     */
    @Test
    void testConstructor() {
        Game game2 = new Game(scene, input, gameState);
        
        assertEquals(scene, game2.getScene());
        assertEquals(input, game2.getInput());
        assertEquals(gameState, game2.getState());
    }

    /**
     * Test that test the transition from a new (stopped) game to a started game.
     * Verifies the interaction with game state and ensures that the game
     * is now marked as started.
     */
    @Test
    void testStartGame() {
        // Ensure game is not started
        assertFalse(game.isStarted());

        // Start the game
        game.startGame();

        // Verify game state started & game is staretd
        Mockito.verify(gameState).startGame();
        assertTrue(game.isStarted());
    }

    /**
     * Test that verifies that upon immediately starting the game,
     * the pool balls are not initially in motion.
     */
    @Test
    void testStartGameInMotion() {
        game.startGame();
        assertFalse(game.isInMotion());
    }
}
