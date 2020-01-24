package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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
    transient SoundPlayer soundPlayer;

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
        soundPlayer = Mockito.mock(SoundPlayer.class);

        GameElements gameElements = new GameElements(poolBalls, table, cue);
        SceneElements sceneElements = new SceneElements(environment, camera, soundPlayer);

        scene = new Scene3D(batch, gameElements, sceneElements);
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

        poolBalls.clear();

        for (int i = 0; i < ballCount; ++i) {
            poolBalls.add(Mockito.mock(Ball3D.class));
        }

        GameElements newGameElements = new GameElements(poolBalls, table, cue);
        SceneElements newSceneElements = new SceneElements(environment, camera, soundPlayer);
        scene = new Scene3D(batch, newGameElements, newSceneElements);

        // Poolballs + Table + Cue
        int expectedSize = poolBalls.size() + 2;
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
     * Test case to verify that upon triggering collisions
     * with the cue ball not colliding with anything,
     * the first touched ball is null.
     */
    @Test
    public void testTriggerCollisionsOtherBallsTouch() {
        // Create 2 mock pool balls, and add them to the scene
        Ball3D ball1 = Mockito.mock(Ball3D.class);
        Ball3D ball2 = Mockito.mock(Ball3D.class);

        scene.getPoolBalls().add(ball1);
        scene.getPoolBalls().add(ball2);

        // Make the two balls collide
        Mockito.when(ball1.checkCollision(ball2)).thenReturn(true);

        // Trigger collisions for the scene
        scene.triggerCollisions();

        // Assert that cue ball did not touch any ball
        assertTrue(scene.getFirstTouched() instanceof NullBall);
    }

    /**
     * Test case to verify that upon triggering collisions
     * with the cue ball colliding with another ball,
     * the last touched ball is updated to that ball.
     */
    @Test
    public void testTriggerCollisionsCueTouch() {
        assertTrue(scene.getFirstTouched() instanceof NullBall);

        // Create 2 mock pool balls, and add them to the scene
        Ball3D ball1 = Mockito.mock(CueBall3D.class);
        Ball3D ball2 = Mockito.mock(Ball3D.class);

        scene.getPoolBalls().add(ball1);
        scene.getPoolBalls().add(ball2);

        // Make the two balls collide
        Mockito.when(ball1.checkCollision(ball2)).thenReturn(true);

        // Trigger collisions for the scene
        scene.triggerCollisions();

        // Assert that cue ball touched the ball it collided with
        assertEquals(ball2, scene.getFirstTouched());
    }

    /**
     * Test case to verify that upon triggering collisions
     * with the cue ball colliding with another ball,
     * the last touched ball is updated to that ball.
     */
    @Test
    public void testTriggerCollisionsCueTouchMultiple() {
        // Create 3 mock pool balls, and add them to the scene
        Ball3D ball1 = Mockito.mock(CueBall3D.class);
        Ball3D ball2 = Mockito.mock(Ball3D.class);
        Ball3D ball3 = Mockito.mock(Ball3D.class);

        scene.getPoolBalls().add(ball1);
        scene.getPoolBalls().add(ball2);
        scene.getPoolBalls().add(ball3);

        // Make the two balls collide
        Mockito.when(ball1.checkCollision(ball2)).thenReturn(true);
        Mockito.when(ball1.checkCollision(ball3)).thenReturn(true);

        // Trigger collisions for the scene
        scene.triggerCollisions();

        // Assert that cue ball touched the ball it collided with
        assertEquals(ball2, scene.getFirstTouched());
    }

    /**
     * Test case to verify that clearing touched ball
     * successfully removes the ball that was stored
     * as first touched.
     */
    @Test
    public void testTriggerCollisionsClearTouched() {
        // Call previous test to setup into Cue Ball having touched
        // a ball
        testTriggerCollisionsCueTouch();

        // Clear pool balls of scene to prevent collision re-checking
        scene.getPoolBalls().clear();

        // Trigger collisions again
        scene.triggerCollisions();

        // Verify a touched ball still exists (i.e. not reset)
        assertNotNull(scene.getFirstTouched());

        // Clear the first touched ball & assert that it is now
        // null when checked in the scnee.
        scene.clearFirstTouched();
        assertTrue(scene.getFirstTouched() instanceof NullBall);
    }

    /**
     * Test if getting the cue ball will actually return the cue ball.
     */
    @Test
    public void testGetCueBall() {
        Ball3D ball = Mockito.mock(CueBall3D.class);
        scene.getPoolBalls().add(ball);

        assertEquals(ball, scene.getCueBall());
    }

    /**
     * Test to verify whether all correct calls are made when the cue ball
     * is placed correctly.
     */
    @Test
    void testValidCueBallPlacement() {
        // Set up the spy object
        Scene3D spyScene = Mockito.spy(scene);
        Mockito.doReturn(new Vector3(1, 21, 0)).when(spyScene).getUnprojectedMousePosition();

        // Set up the mock cue ball object, and handle its methods
        CueBall3D cueBall = Mockito.mock(CueBall3D.class);
        Mockito.doReturn(true).when(cueBall).checkWithinBounds();
        Mockito.doReturn(cueBall).when(spyScene).getCueBall();

        ModelInstance modelInstance = Mockito.mock(ModelInstance.class);
        Matrix4 transform = Mockito.mock(Matrix4.class);
        modelInstance.transform = transform;
        Mockito.doReturn(modelInstance).when(cueBall).getModel();

        HitBox hitBox = Mockito.mock(HitBox.class);
        Mockito.doReturn(hitBox).when(cueBall).getHitBox();

        // Set up mock object for another ball
        Ball3D otherBall = Mockito.mock(Ball3D.class);
        HitBox otherHitBox = Mockito.mock(HitBox.class);
        Mockito.doReturn(otherHitBox).when(otherBall).getHitBox();

        // Have the other ball be returned when the method 
        // looks for the remaining balls on the board
        List<Ball3D> list = new ArrayList<>();
        list.add(cueBall);
        list.add(otherBall);
        Mockito.doReturn(list).when(spyScene).getPoolBalls();

        // Set up mock object for the collision handler
        CollisionHandler handler = Mockito.mock(CollisionHandler.class);
        Mockito.doReturn(false).when(handler).checkHitBoxCollision(hitBox, otherHitBox);
        Mockito.doReturn(handler).when(cueBall).getCollisionHandler();

        // Set up the mock input object
        Input input = Mockito.mock(Input.class);
        Mockito.doReturn(true).when(input).isButtonPressed(Input.Buttons.LEFT);

        assertTrue(spyScene.placeCueBall(input));

        Mockito.verify(transform).setTranslation(new Vector3(1, 0.28f, 0));
        Mockito.verify(hitBox).updateLocation(transform);
    }

    /**
     * Test to verify whether all correct calls are made when the cue ball
     * is placed out of bounds.
     */
    @Test
    void testOutOfBoundsCueBallPlacement() {
        // Set up the spy object
        Scene3D spyScene = Mockito.spy(scene);
        Mockito.doReturn(new Vector3(0, 5, 3)).when(spyScene).getUnprojectedMousePosition();

        // Set up the mock cue ball object, and handle its methods
        CueBall3D cueBall = Mockito.mock(CueBall3D.class);
        Mockito.doReturn(false).when(cueBall).checkWithinBounds();
        Mockito.doReturn(cueBall).when(spyScene).getCueBall();

        ModelInstance modelInstance = Mockito.mock(ModelInstance.class);
        Matrix4 transform = Mockito.mock(Matrix4.class);
        modelInstance.transform = transform;
        Mockito.doReturn(modelInstance).when(cueBall).getModel();

        HitBox hitBox = Mockito.mock(HitBox.class);
        Mockito.doReturn(hitBox).when(cueBall).getHitBox();

        // Set up the mock input object
        Input input = Mockito.mock(Input.class);
        Mockito.doReturn(true).when(input).isButtonPressed(Input.Buttons.LEFT);

        assertFalse(spyScene.placeCueBall(input));

        Mockito.verify(transform).setTranslation(new Vector3(0, 0.28f, 3));
        Mockito.verify(hitBox).updateLocation(transform);
    }

    /**
     * Test case to verify that the ball is not placed successfully when
     * it collides with another ball on the table.
     */
    @Test
    void testCollidingCueBallPlacement() {
        // Set up the spy object
        Scene3D spyScene = Mockito.spy(scene);
        Mockito.doReturn(new Vector3(2, 20, 0)).when(spyScene).getUnprojectedMousePosition();

        // Set up the mock cue ball object, and handle its methods
        CueBall3D cueBall = Mockito.mock(CueBall3D.class);
        Mockito.doReturn(true).when(cueBall).checkWithinBounds();
        Mockito.doReturn(cueBall).when(spyScene).getCueBall();

        ModelInstance modelInstance = Mockito.mock(ModelInstance.class);
        Matrix4 transform = Mockito.mock(Matrix4.class);
        modelInstance.transform = transform;
        Mockito.doReturn(modelInstance).when(cueBall).getModel();

        HitBox hitBox = Mockito.mock(HitBox.class);
        Mockito.doReturn(hitBox).when(cueBall).getHitBox();
        
        // Set up mock object for another ball
        Ball3D otherBall = Mockito.mock(Ball3D.class);
        HitBox otherHitBox = Mockito.mock(HitBox.class);
        Mockito.doReturn(otherHitBox).when(otherBall).getHitBox();
        
        // Have the other ball be returned when the method 
        // looks for the remaining balls on the board
        List<Ball3D> list = new ArrayList<>();
        list.add(cueBall);
        list.add(otherBall);
        Mockito.doReturn(list).when(spyScene).getPoolBalls();
        
        // Set up mock object for the collision handler
        CollisionHandler handler = Mockito.mock(CollisionHandler.class);
        Mockito.doReturn(true).when(handler).checkHitBoxCollision(hitBox, otherHitBox);
        Mockito.doReturn(handler).when(cueBall).getCollisionHandler();
        
        // Set up the mock input object
        Input input = Mockito.mock(Input.class);
        Mockito.doReturn(true).when(input).isButtonPressed(Input.Buttons.LEFT);

        assertFalse(spyScene.placeCueBall(input));

        Mockito.verify(transform).setTranslation(new Vector3(2, 0.28f, 0));
        Mockito.verify(hitBox).updateLocation(transform);
    }
}