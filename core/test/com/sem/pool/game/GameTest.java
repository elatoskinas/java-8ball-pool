package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Scene3D;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameTest {
    transient Scene3D scene;
    transient Input input;
    transient GameState gameState;
    transient Game game;

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

    /**
     * Test case to verify that the move balls method moves all
     * the balls present in the scene upon calling the move balls
     * method.
     */
    @Test
    void testMoveBallsMethod() {
        // Set up mock balls to pass into the mock scene
        Ball3D ball = Mockito.mock(Ball3D.class);
        Ball3D ball2 = Mockito.mock(Ball3D.class);

        List<Ball3D> poolBalls = new ArrayList<>();
        poolBalls.add(ball);
        poolBalls.add(ball2);

        Mockito.when(scene.getPoolBalls()).thenReturn(poolBalls);

        // Start the game and attempt to move the balls
        game.startGame();
        game.moveBalls();

        Mockito.verify(ball).move();
        Mockito.verify(ball2).move();
    }

    /**
     * Test case to verify that when all the balls are in motion,
     * the Game determines that it is in motion for the current iteration.
     */
    @Test
    void testBallsInMotionAll() {
        setupScenePoolBallsHelper(true, true, true);

        game.startGame();

        assertTrue(game.determineIsInMotion());
    }

    /**
     * Test case to verify that when a single ball is in motion,
     * the Game determines that it is also in motion.
     */
    @Test
    void testBallsInMotionSingle() {
        setupScenePoolBallsHelper(false, false, true);

        game.startGame();

        assertTrue(game.determineIsInMotion());
    }

    /**
     * Test case to verify that when no balls are in motion,
     * the Game determines that it is also not in motion.
     */
    @Test
    void testBallsInMotionNone() {
        setupScenePoolBallsHelper(false, false, false);

        game.startGame();

        assertFalse(game.determineIsInMotion());
    }

    /**
     * Test case to verify the state transition from a game that has
     * not yet been started, to a started game that is not in motion,
     * and then to a game that is in motion because at least one
     * ball is in motion.
     */
    @Test
    void testLoopToMotion() {
        setupScenePoolBallsHelper(false, true);

        game.startGame();
        assertTrue(game.isStarted());

        assertFalse(game.isInMotion());

        game.advanceGameLoop();

        assertTrue(game.isInMotion());
    }

    /**
     * Test case to verify that a stopped game does not trigger
     * any functionality. This is done by making sure that there
     * are no interactions with the scene or the input objects
     * of the Game.
     */
    @Test
    void testLoopNotStarted() {
        game.advanceGameLoop();

        Mockito.verifyNoInteractions(scene);
        Mockito.verifyNoInteractions(input);
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
