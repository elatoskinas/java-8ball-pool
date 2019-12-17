package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Cue3D;
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

        List<Player> players = new ArrayList<>();
        players.add(Mockito.mock(Player.class));
        players.add(Mockito.mock(Player.class));

        List<Ball3D> poolBalls = new ArrayList<>();
        gameState = new GameState(players, poolBalls);
        game = new Game(scene, input, gameState);
    }

    /**
     * Test method to verify that the Game object is instantiated correctly.
     * (Scene and input are assigned without error)
     */
    @Test
    void testConstructor() {
        gameState = Mockito.mock(GameState.class);
        Game game2 = new Game(scene, input, gameState);

        assertEquals(scene, game2.getScene());
        assertEquals(input, game2.getInput());
        assertEquals(gameState, game2.getState());

        // Verify that game is added as an observer
        Mockito.verify(gameState).addObserver(game2);
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
     * Test that verifies that upon immediately starting the game,
     * the pool balls are not initially in motion.
     */
    @Test
    void testStartGameInMotion() {
        game.startGame();
        assertFalse(game.determineIsInMotion());
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
     * Test case to verify the game advances when there are no balls
     * in motion and the game is currently in the run state.
     * The players should switch turns.
     */
    @Test
    void testAdvanceGameLoopCallRunningState() {

        setupScenePoolBallsHelper(false, false, false);
        game.startGame();
        assertFalse(game.determineIsInMotion());
        gameState.setToRunning();
        game.advanceGameLoop();
        assertTrue(gameState.isIdle());
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

    /**
     * Test case to verify that shoot is called when the left mouse button is clicked.
     */
    @Test
    void testLeftClickShot() {
        Cue3D cue = Mockito.mock(Cue3D.class);
        Mockito.when(scene.getCue()).thenReturn(cue);
        Mockito.when(scene.getUnprojectedMousePosition()).thenReturn(new Vector3(0,0,0));
        Mockito.when(input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);
        setupScenePoolBallsHelper(false);

        game.respondToInput();

        Mockito.verify(cue).shoot(Mockito.any(Vector3.class), Mockito.any(Ball3D.class));
    }
}
