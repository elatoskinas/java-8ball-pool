package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for Game & Game State integration testing.
 */
class GameAndStateIntegrationTest extends GameBaseTest {
    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        gameState = new GameState(players, poolBalls);
        game = new Game(scene, input, gameState);
    }

    /**
     * Test case to verify the game advances when there are no balls
     * in motion and the game is currently in the in motion state.
     * The players should switch turns.
     */
    @Test
    void testAdvanceGameLoopCallRunningState() {
        setupScenePoolBallsHelper(false, false, false);
        game.startGame();
        assertFalse(game.determineIsInMotion());
        gameState.onMotion();

        final float deltaTime = 42f;
        game.advanceGameLoop(deltaTime);

        assertTrue(gameState.isStarted());
        assertFalse(gameState.isInMotion());
        assertTrue(gameState.isIdle());

    }

    /**
     * Test that test the transition from a new (stopped) game to a started game.
     * Verifies the interaction with game state and ensures that the game
     * is now marked as started.
     */
    @Test
    void testStartGame() {
        // Ensure game is not started
        assertFalse(gameState.isStarted());

        // Start the game
        game.startGame();

        assertTrue(gameState.isStarted());
    }

    /**
     * Test case to verify that a running game with moving balls
     * will move the balls and trigger collisions when the advance game loop is called.
     */
    @Test
    void testAdvanceGameLoopMoveBalls() {

        setupScenePoolBallsHelper(true, false);

        gameState.onGameStarted();
        gameState.onMotion();

        final float deltaTime = 42f;
        game.advanceGameLoop(deltaTime);

        Mockito.verify(scene).triggerCollisions();
    }
}