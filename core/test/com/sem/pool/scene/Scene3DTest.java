package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.sem.pool.game.GameConstants;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



/**
 * Test class containing unit tests for the Scene3D class.
 */
class Scene3DTest {
    transient Scene3D scene;

    transient ModelBatch batch;
    transient Camera camera;
    transient Environment environment;

    transient List<Ball3D> poolBalls;
    transient Table3D table;
    transient Cue3D cue;

    /**
     * Handles setting up the test fixture by
     * creating mocks of all the required dependencies
     * for creating a Scene object instance.
     */
    @BeforeEach
    public void setUp() {
        batch = Mockito.mock(ModelBatch.class);
        camera = Mockito.mock(Camera.class);
        environment = Mockito.mock(Environment.class);

        table = Mockito.mock(Table3D.class);
        poolBalls = new ArrayList<>();
        cue = Mockito.mock(Cue3D.class);

        scene = new Scene3D(environment, camera, poolBalls, table, cue, batch);
    }

    /**
     * Test to verify that the correct objects are set to the
     * scene's parameters upon constructing a new Scene.
     */
    @Test
    public void testConstructor() {
        assertEquals(camera, scene.getCamera());
        assertEquals(environment, scene.getEnvironment());
        assertEquals(poolBalls, scene.getPoolBalls());
        assertEquals(table, scene.getTable());
    }

    /**
     * Test to verify that the correct number of models
     * are added to the scene's model List upon constructing
     * a Scene object.
     */
    @Test
    public void testConstructorModelsSize() {
        // Poolballs + Table + Cue
        int expectedSize = poolBalls.size() + 2;
        int actualSize = scene.getModels().size();
        assertEquals(expectedSize, actualSize);
    }

    /**
     * Test to verify that the correct number of models
     * are added to the scene's model list upon specifying
     * a non-empty List of pool balls as one of the parameters
     * for the Scene to instantiate.
     */
    @Test
    public void testConstructorModelsSizeBallsPresent() {
        final int ballCount = 6;

        List<Ball3D> poolBalls2 = new ArrayList<>();

        for (int i = 0; i < ballCount; ++i) {
            poolBalls2.add(Mockito.mock(Ball3D.class));
        }

        scene = new Scene3D(environment, camera, poolBalls2, table, cue, batch);

        // Poolballs + Table + Cue
        int expectedSize = poolBalls2.size() + 2;
        int actualSize = scene.getModels().size();
        assertEquals(expectedSize, actualSize);
    }

    /**
     * Test case to verify that the necessary render-related
     * calls are made with the scene's elements.
     */
    @Test
    public void testRenderModels() {
        scene.render();

        // Get the elements of the scene that should be used
        // in the rendering process
        Camera camera = scene.getCamera();
        List<ModelInstance> models = scene.getModels();
        Environment environment = scene.getEnvironment();

        // Verify that all the necessary render calls have been made
        Mockito.verify(batch).begin(camera);
        Mockito.verify(batch).render(models, environment);
        Mockito.verify(batch).end();
    }

    /**
     * Test case to ensure that after calling the dispose method
     * on the scene, all the necessary calls are made to clean
     * up the scene & models.
     */
    @Test
    public void testDispose() {
        scene.dispose();

        Mockito.verify(batch).dispose();
        assertEquals(0, scene.getModels().size());
    }

    /**
     * Test case to verify that the collisions are checked between
     * all the balls and the table of the game upon triggering
     * collisions for the scene.
     */
    @Test
    public void testTriggerCollisionsTableBallInteractions() {
        // Create 2 mock pool balls, and add them to the scene
        Ball3D ball1 = Mockito.mock(Ball3D.class);
        Ball3D ball2 = Mockito.mock(Ball3D.class);

        scene.getPoolBalls().add(ball1);
        scene.getPoolBalls().add(ball2);

        // Trigger collisions for the scene
        scene.triggerCollisions();

        // Verify that the collisions are checked between
        // the balls and the table.
        Mockito.verify(table).checkCollision(ball1);
        Mockito.verify(table).checkCollision(ball2);
    }

    /**
     * Test case to verify that when a ball is potted after
     * triggering collisions for the scene, the List
     * contains the potted ball in the returned List.
     */
    @Test
    public void testTriggerCollisionsBallPotted() {
        Ball3D ball = Mockito.mock(Ball3D.class);
        scene.getPoolBalls().add(ball);
        ball.setSpeed(GameConstants.MIN_SPEED + 1);
        // Set ball to be potted
        Mockito.when(table.checkIfPot(ball)).thenReturn(true);

        // Geth the potted balls after triggering collisions
        List<Ball3D> potted = scene.triggerCollisions();

        // Ensure that the ball was potted
        assertEquals(1, potted.size());
        assertEquals(ball, potted.get(0));
    }

    /**
     * Test case to verify that when all the balls are
     * potted after triggering collisions for the scene, the List
     * contains all the Balls present in the scene.
     */
    @Test
    public void testTriggerCollisionsMultipleBallsPotted() {
        final int ballCount = 3;

        // Create specified number of balls & set ball to be potted
        for (int i = 0; i < ballCount; ++i) {
            Ball3D ball = Mockito.mock(Ball3D.class);
            Mockito.when(table.checkIfPot(ball)).thenReturn(true);
            scene.getPoolBalls().add(ball);
        }

        // Geth the potted balls after triggering collisions
        List<Ball3D> potted = scene.triggerCollisions();

        // Ensure that the list of potted balls is equal to
        // the list of the scene's balls
        assertEquals(scene.getPoolBalls(), potted);
    }

    /**
     * Test case to verify that when the collisions are
     * triggered for a scene, and no ball is potted, then
     * the returned List of potted balls is empty.
     */
    @Test
    public void testTriggerCollisionsNoBallPotted() {
        Ball3D ball = Mockito.mock(Ball3D.class);
        scene.getPoolBalls().add(ball);
        ball.setSpeed(GameConstants.MIN_SPEED + 1);
        // Set ball to be potted
        Mockito.when(table.checkIfPot(ball)).thenReturn(false);

        // Geth the potted balls after triggering collisions
        List<Ball3D> potted = scene.triggerCollisions();

        // Ensure that no ball was potted
        assertEquals(0, potted.size());
    }

    /**
     * Tests if the sounds are played when collisions are triggered.
     */
    @Test
    public void testTriggerCollisionsSounds() {
        Ball3D ball = Mockito.mock(Ball3D.class);
        scene.getPoolBalls().add(ball);
        ball.setSpeed(GameConstants.MIN_SPEED + 1);
        // Set ball to be potted
        Mockito.when(table.checkIfPot(ball)).thenReturn(true);
        Mockito.when(table.checkCollision(ball)).thenReturn(true);
        Mockito.when(ball.checkCollision(Mockito.any())).thenReturn(true);
        // Get the potted balls after triggering collisions
        List<Ball3D> potted = scene.triggerCollisions();
        // Ensure that no ball was potted
        assertEquals(1, potted.size());
    }

    /**
     * Tests whether the playSound method calls the play() method when it should.
     */
    @Test
    public void testPlaySound() {
        Music mockedMusic = Mockito.mock(Music.class);
        Mockito.when(mockedMusic.isPlaying()).thenReturn(true);
        scene.playSound(mockedMusic);
        Mockito.verify(mockedMusic, Mockito.times(0)).play();
        Mockito.when(mockedMusic.isPlaying()).thenReturn(false);
        scene.playSound(mockedMusic);
        Mockito.verify(mockedMusic, Mockito.times(1)).play();
    }
}