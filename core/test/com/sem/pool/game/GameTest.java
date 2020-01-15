package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.Scene3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        //Mockito.verify(gameState).addObserver(game2);
        // TODO: Verify game state is added as an observer
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
        Mockito.verify(gameState).onMotionStop(null);
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
     * Test if the recenterCue method is called when potting the cue ball.
     */
    @Test
    void testPotCue() {
        CueBall3D ball = Mockito.mock(CueBall3D.class);

        game.startGame();
        game.potBall(ball);

        // Verify ball is potted
        Mockito.verify(ball).pot();
    }

    /**
     * Test case to verify that the game state is an observer of the Game
     * class after constructing the Game class.
     */
    @Test
    public void testGameStateIsObserver() {
        assertTrue(game.getObservers().contains(gameState));
    }

    /**
     * Test case to verify that retrieving the observers of the
     * instantiated Game object returns a non-empty Set
     * containing the Game State, thus verifying that the Game
     * State is added as an observer, and that the proper collection is returned.
     */
    @Test
    public void testGameGetObservers() {
        final Set<GameObserver> expected = new HashSet<>();
        expected.add(gameState);

        assertEquals(expected, game.getObservers());
    }

    /**
     * Test case to verify that adding an observer to the Game object
     * properly adds it to the underlying collection.
     */
    @Test
    public void testGameAddObserver() {
        GameObserver observer = Mockito.mock(GameObserver.class);
        assertFalse(game.getObservers().contains(observer));

        game.addObserver(observer);
        assertTrue(game.getObservers().contains(observer));
    }

    /**
     * Test case to verify that removing an observer from the Game
     * object will properly remove it from the underlying collection
     * in the case that it exists.
     */
    @Test
    public void testGameRemoveObserver() {
        GameObserver observer = Mockito.mock(GameObserver.class);

        game.addObserver(observer);
        assertTrue(game.getObservers().contains(observer));

        game.removeObserver(observer);
        assertFalse(game.getObservers().contains(observer));
    }

    /**
     * Test case to verify that removing an observer which does not
     * exist in the Game's observers does not remove anything.
     */
    @Test
    public void testGameRemoveObserverNonExistent() {
        int size = game.getObservers().size();

        GameObserver observer = Mockito.mock(GameObserver.class);
        assertFalse(game.getObservers().contains(observer));

        game.removeObserver(observer);
        assertEquals(size, game.getObservers().size());
    }

    /**
     * Test case to verify that upon starting the game, all observers
     * are notified of the start of the game.
     */
    @Test
    public void testGameStartObservers() {
        final int observerCount = 5;
        final List<GameObserver> observers = setUpObservers(observerCount);

        game.startGame();

        for (int i = 0; i < observers.size(); ++i) {
            GameObserver o = observers.get(i);
            Mockito.verify(o).onGameStarted();
        }
    }

    /**
     * Test case to verify that upon ending the game, all observers
     * are notified of the end of the game.
     */
    @Test
    public void testGameEndObservers() {
        final int observerCount = 3;
        final List<GameObserver> observers = setUpObservers(observerCount);

        game.endGame();

        for (int i = 0; i < observers.size(); ++i) {
            GameObserver o = observers.get(i);
            Mockito.verify(o).onGameEnded();
        }
    }

    /**
     * Test case to verify that upon stopping the motion of the game,
     * all the observers are notified of the change.
     */
    @Test
    public void testGameMotionStopObservers() {
        final int observerCount = 4;
        final List<GameObserver> observers = setUpObservers(observerCount);

        Ball3D touched = Mockito.mock(Ball3D.class);
        game.stopMotion(touched);

        for (int i = 0; i < observers.size(); ++i) {
            GameObserver o = observers.get(i);
            Mockito.verify(o).onMotionStop(touched);
        }
    }

    /**
     * Test case to verify that upon starting the motion of the game,
     * all the observers are notified of the change.
     */
    @Test
    public void testGameMotionStartObservers() {
        final int observerCount = 4;
        final List<GameObserver> observers = setUpObservers(observerCount);

        game.startMotion();

        for (int i = 0; i < observers.size(); ++i) {
            GameObserver o = observers.get(i);
            Mockito.verify(o).onMotion();
        }
    }

    /**
     * Test case to verify that upon potting a ball in the Game object,
     * all observers are notified of the pot.
     */
    @Test
    public void testGamePotObservers() {
        final int observerCount = 5;
        final List<GameObserver> observers = setUpObservers(observerCount);

        Ball3D ball = Mockito.mock(Ball3D.class);
        game.potBall(ball);

        for (int i = 0; i < observers.size(); ++i) {
            GameObserver o = observers.get(i);
            Mockito.verify(o).onBallPotted(ball);
        }
    }

    /**
     * Test case to verify that when the game is determined to
     * be no longer in motion, the observers are notified with
     * the scene's touched ball that the motion stopped.
     */
    @Test
    public void testDetermineNotInMotionTouchedBall() {
        final int observerCount = 4;
        final List<GameObserver> observers = setUpObservers(observerCount);

        Ball3D touched = Mockito.mock(Ball3D.class);
        Mockito.when(scene.getFirstTouched()).thenReturn(touched);

        // Set in motion & determine new in motion state
        Mockito.when(gameState.isInMotion()).thenReturn(true);
        boolean motion = game.determineIsInMotion();

        // Verify interraction on observers
        for (int i = 0; i < observers.size(); ++i) {
            GameObserver o = observers.get(i);
            Mockito.verify(o).onMotionStop(touched);
        }

        // Assert that game is no longer in motion, and that the
        // first touched ball is cleared from the scene.
        assertFalse(motion);
        Mockito.verify(scene).clearFirstTouched();
    }

    /**
     * Helper method to set up the specified number of mock observers.
     * @param count  Number of observers to create
     * @return       List of mock observers
     */
    private List<GameObserver> setUpObservers(int count) {
        List<GameObserver> result = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            GameObserver observer = Mockito.mock(GameObserver.class);
            game.addObserver(observer);
            result.add(observer);
        }

        return result;
    }
}
