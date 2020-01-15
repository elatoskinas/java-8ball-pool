package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Cue3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.HitBox;
import com.sem.pool.scene.Scene3D;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class GameTest extends GameBaseTest {
    @BeforeEach
    void setUp() {
        super.setUp();
        gameState = Mockito.mock(GameState.class);
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
        final float deltaTime = 3.14159f;
        game.moveBalls(deltaTime);

        Mockito.verify(ball).move(deltaTime);
        Mockito.verify(ball2).move(deltaTime);
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
     * Test case to verify that a stopped game does not trigger
     * any functionality. This is done by making sure that there
     * are no interactions with the scene or the input objects
     * of the Game.
     */
    @Test
    void testLoopNotStarted() {
        final float deltaTime = 42f;
        game.advanceGameLoop(deltaTime);

        Mockito.verifyNoInteractions(scene);
        Mockito.verifyNoInteractions(input);
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

    /**
     * Test case to verify that a running game
     * with no moving balls advances turns.
     */
    @Test
    void testAdvanceGameLoopAdvanceTurn() {

        scene = Mockito.mock(Scene3D.class);
        input = Mockito.mock(Input.class);
        gameState = Mockito.mock(GameState.class);
        game = new Game(scene, input, gameState);
        setupScenePoolBallsHelper(false, false);

        Mockito.when(gameState.isStarted()).thenReturn(true);
        Mockito.when(gameState.isInMotion()).thenReturn(true);

        final float deltaTime = 42f;
        game.advanceGameLoop(deltaTime);
        Mockito.verify(gameState).advanceTurn();
    }

    /**
     * Test case to verify that a running game
     * with moving balls doesn't advance turns.
     */
    @Test
    void testAdvanceGameLoopNotAdvanceTurn() {

        scene = Mockito.mock(Scene3D.class);
        input = Mockito.mock(Input.class);
        gameState = Mockito.mock(GameState.class);
        game = new Game(scene, input, gameState);
        setupScenePoolBallsHelper(true, false);

        Mockito.when(gameState.isStarted()).thenReturn(true);
        Mockito.when(gameState.isInMotion()).thenReturn(true);

        final float deltaTime = 42f;
        game.advanceGameLoop(deltaTime);
        Mockito.verify(gameState, never()).advanceTurn();
    }

    /**
     * Test case to ensure that when balls are moved and potted,
     * the actions are propagated to the Game State.
     */
    @Test
    void testMoveBallsPotted() {
        final float deltaTime = 0;

        // Create 2 mock balls and add them to the expected Ball list
        // resulting from triggering collisions
        List<Ball3D> ballResult = new ArrayList<>();
        Ball3D ball1 = Mockito.mock(Ball3D.class);
        Ball3D ball2 = Mockito.mock(Ball3D.class);
        ballResult.add(ball1);
        ballResult.add(ball2);

        // Set the resut to be returned after triggering collisions
        Mockito.when(scene.triggerCollisions()).thenReturn(ballResult);

        // Start the game & move the balls
        game.startGame();
        game.moveBalls(deltaTime);

        // Ensure that the balls are potted (since we set them
        // to be after triggering collisions)
        Mockito.verify(gameState).onBallPotted(ball1);
        Mockito.verify(gameState).onBallPotted(ball2);
    }

    /**
     * Test case to ensure that when the balls are moved
     * and not potted, no interactions are done with the Game State
     * with regards to potting.
     */
    @Test
    void testMoveBallsNotPotted() {
        final float deltaTime = 0;

        // Set the scene to not pot any balls
        List<Ball3D> ballResult = new ArrayList<>();
        Mockito.when(scene.triggerCollisions()).thenReturn(ballResult);

        // Start game & move balls
        game.startGame();
        game.moveBalls(deltaTime);

        // Verify ball potting is never called on the game state,
        // since no balls were potted
        Mockito.verify(gameState, Mockito.times(0))
                .onBallPotted(Mockito.any(Ball3D.class));
    }

    /**
     * Test case to verify that upon potting a ball, the proper
     * interactions are made between the ball itself, the Game State
     * and the Game class.
     */
    @Test
    void testPotBall() {
        Ball3D ball = Mockito.mock(Ball3D.class);
        game.startGame();
        game.potBall(ball);

        // Verify ball is potted
        Mockito.verify(ball).pot();

        // Verify ball potted in Game State
        Mockito.verify(gameState).onBallPotted(ball);
    }

    /**
     * Test if the resetCue method is called when potting the cue ball.
     */
    @Test
    void testPotCue() {
        Ball3D ball = Mockito.mock(CueBall3D.class);
        ModelInstance model = Mockito.mock(ModelInstance.class);
        Matrix4 transform = Mockito.mock(Matrix4.class);
        Matrix4 newTransform = Mockito.mock(Matrix4.class);
        final HitBox hitBox = Mockito.mock(HitBox.class);
        model.transform = transform;
        Mockito.when(ball.getModel()).thenReturn(model);
        Mockito.when(transform.set(Mockito.any(float[].class))).thenReturn(newTransform);
        Mockito.when(ball.getHitBox()).thenReturn(hitBox);
        Mockito.doNothing().when(hitBox).updateLocation(Mockito.any(Matrix4.class));
        ArrayList<Ball3D> balls = new ArrayList<>();
        balls.add(ball);
        Mockito.when(this.scene.getPoolBalls()).thenReturn(balls);

        game.startGame();
        game.potBall(ball);

        // Verify ball is potted
        Mockito.verify(ball).pot();
    }
}
