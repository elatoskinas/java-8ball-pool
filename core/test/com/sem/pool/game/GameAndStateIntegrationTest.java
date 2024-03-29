package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.scene.Cue3D;
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
        this.gameState = new GameState(players, poolBalls);

        Database.setTestMode();

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
        Cue3D cue = Mockito.mock(Cue3D.class);
        Mockito.when(scene.getCue()).thenReturn(cue);

        game = new Game(scene, input, gameState);

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

    @Test
    void testEndGameOnWin() {
        final float deltaTime = 1f;

        game = new Game(scene, input, gameState);

        // Start game
        game.startGame();

        // Create observer to verify game end call
        GameObserver observer = Mockito.mock(GameObserver.class);
        game.addObserver(observer);

        // Win game & advance to next game loop iteration
        gameState.winGame(false);
        game.advanceGameLoop(deltaTime);

        // Ensure end game event is sent
        Mockito.verify(observer).onGameEnded(Mockito.any(Player.class), Mockito.any());

        assertTrue(gameState.isStopped());
    }
}