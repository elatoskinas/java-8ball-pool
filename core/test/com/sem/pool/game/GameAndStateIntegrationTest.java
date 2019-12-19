package com.sem.pool.game;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Scene3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Game & Game State integration testing.
 */
class GameAndStateIntegrationTest {
    transient Scene3D scene;
    transient Input input;
    transient GameState gameState;
    transient Game game;

    @BeforeEach
    void setUp() {
        scene = Mockito.mock(Scene3D.class);
        input = Mockito.mock(Input.class);

        List<Player> players = new ArrayList<>();
        players.add(Mockito.mock(Player.class));
        players.add(Mockito.mock(Player.class));

        List<Ball3D> poolBalls = new ArrayList<>();
        gameState = Mockito.mock(GameState.class);
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
        gameState.setInMotion();

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

        gameState.startGame();
        gameState.setInMotion();

        final float deltaTime = 42f;
        game.advanceGameLoop(deltaTime);

        Mockito.verify(scene).triggerCollisions();
    }

    // Seems like an FP indicating a DU caused by the loop
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void setupScenePoolBallsHelper(boolean... motion) {
        List<Ball3D> balls = new ArrayList<>();

        for (boolean b : motion) {
            Ball3D ball = Mockito.mock(Ball3D.class);
            Mockito.when(ball.isInMotion()).thenReturn(b);
            balls.add(ball);
        }

        Mockito.when(scene.getPoolBalls()).thenReturn(balls);
    }
}