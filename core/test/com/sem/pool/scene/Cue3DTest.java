package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import com.sem.pool.game.GameConstants;
import com.sem.pool.game.GameState;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * TODO: Add more tests cases
 * Test class containing unit tests for the Cue3D class.
 */
public class Cue3DTest {

    transient Cue3D cue;


    /**
     * Handles setting up the test fixture by
     * creating mocks of all the required dependencies
     * for creating a Cue object instance.
     */
    @BeforeEach
    public void setUp() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = Mockito.mock(Matrix4.class);
        Material mat = new Material();
        Mockito.when(model.getMaterial("CueMaterial")).thenReturn(mat);

        cue = new Cue3D(model);
    }

    /**
     * Test case to verify that the constructor of Cue3D
     * properly sets the model of the cue.
     */
    @Test
    public void testConstructor() {

        ModelInstance model = Mockito.mock(ModelInstance.class);
        Material mat = new Material();
        Mockito.when(model.getMaterial("CueMaterial")).thenReturn(mat);

        Cue3D cue = new Cue3D(model);
        assertEquals(model, cue.getModel());
    }

    /**
     * Test case to verify that when the mouse is "behind" the ball
     * (relative to world coordinates, i.e. left of the ball),
     * then the cue shot direction goes forward from the mouse to the ball.
     */
    @Test
    public void testCueShotDirectionBehindBall() {
        Vector3 ballPosition = new Vector3(0, 0, 0);
        Vector3 mousePosition = new Vector3(-1, 0, 0);
        Vector3 expectedDirection = new Vector3(1, 0, 0);

        testCueShotDirectionHelper(ballPosition, mousePosition, expectedDirection);
    }

    /**
     * Test case to verify that differing y coordinates for the mouse position
     * and the ball has no effect on the cue shot direction. We verify
     * this by making sure the final direction has a y value of 0.
     */
    @Test
    public void testCueShotDirectionDifferentY() {
        Vector3 ballPosition = new Vector3(0, 3, 0);
        Vector3 mousePosition = new Vector3(-1, 0, 0);
        Vector3 expectedDirection = new Vector3(1, 0, 0);

        testCueShotDirectionHelper(ballPosition, mousePosition, expectedDirection);
    }

    /**
     * Test case to verify that when the mouse is "in front" of the ball
     * (relative to world coordinates, i.e. right of the ball),
     * then the cue shot direction goes forward from the mouse to the ball.
     */
    @Test
    public void testCueShotDirectionInFrontBall() {
        Vector3 ballPosition = new Vector3(0, 0, 0);
        Vector3 mousePosition = new Vector3(1, 0, 0);
        Vector3 expectedDirection = new Vector3(-1, 0, 0);

        testCueShotDirectionHelper(ballPosition, mousePosition, expectedDirection);
    }

    /**
     * Test case to verify that the return cue shot direction
     * is a normalized vector (unit length).
     */
    @Test
    public void testCueShotDirectionNormalization() {
        Vector3 ballPosition = new Vector3(0, 0, 0);
        Vector3 mousePosition = new Vector3(10, 0, 0);
        Vector3 expectedDirection = new Vector3(-1, 0, 0);

        testCueShotDirectionHelper(ballPosition, mousePosition, expectedDirection);
    }

    /**
     * Test case to verify that when the mouse is both
     * "above" and "in front" the ball (relative to world coordinates),
     * then the cue shot direction goes forward from the mouse to the ball
     * in the diagonal direction.
     */
    @Test
    public void testCueShotDirectionDiagonal() {
        Vector3 ballPosition = new Vector3(0, 0, 0);
        Vector3 mousePosition = new Vector3(1, 0, 1);
        float expectedXZ = -1f / (float) Math.sqrt(2f);
        Vector3 expectedDirection = new Vector3(expectedXZ, 0, expectedXZ);

        testCueShotDirectionHelper(ballPosition, mousePosition, expectedDirection);
    }

    /**
     * Test case to verify that cue is set to the right begin position
     * when there is no mouse input yet.
     */
    @Test
    public void testCueToBeginPosition() {

        final float expectedRadius = 2.f;

        Matrix4 cueMockMatrix = Mockito.mock(Matrix4.class);
        cue.getModel().transform = cueMockMatrix;

        cue.toBeginPosition(makeCueBall());

        Mockito.verify(cueMockMatrix, Mockito.times(1))
                .translate(-expectedRadius - Cue3D.CUE_OFFSET, Cue3D.Y_COORDINATE, 0);
    }

    /**
     * Test case to verify that the cue shoots the cueball in the right direction.
     * TODO: Test to check if the cueball has the right speed/force.
     */
    @Test
    public void testShoot() {

        final int id = 0;
        final ModelInstance cueBallModel = Mockito.mock(ModelInstance.class);
        final Matrix4 matrix = Mockito.mock(Matrix4.class);
        cueBallModel.transform = matrix;

        CueBall3D cueBall = new CueBall3D(id, cueBallModel);
        Mockito.when(matrix.getTranslation(Vector3.Zero)).thenReturn(new Vector3(0, 0, 0));

        Vector3 mouseposition = new Vector3(1, 0, 0);
        cue.setDragOriginMouse(new Vector3(0.5f, 0, 0));
        cue.shoot(mouseposition, cueBall);
        assertEquals(new Vector3(-1, 0, 0), cueBall.getDirection());

    }

    /**
     * Test case to verify that the cue shoots the cueball in the right direction.
     */
    @Test
    public void testCueForce() {

        CueBall3D cueBall = makeCueBall();
        Matrix4 matrix = Mockito.mock(Matrix4.class);
        cueBall.getModel().transform = matrix;

        Mockito.when(matrix.getTranslation(Vector3.Zero)).thenReturn(new Vector3(0, 0, 0));

        Vector3 mouseposition = new Vector3(1, 0, 0);
        cue.setDragOriginMouse(new Vector3(0.8f, 0, 0));
        cue.shoot(mouseposition, cueBall);
        assertEquals(0.2f, cueBall.getSpeed(), 0.0001f);
    }

    /**
     * Test case to verify that the cue shoots the cue ball
     * with the max cue force when the actual force
     * is higher than the max cue force.
     */
    @Test
    public void testMaxCueForce() {

        CueBall3D cueBall = makeCueBall();
        Matrix4 matrix = Mockito.mock(Matrix4.class);
        cueBall.getModel().transform = matrix;

        Mockito.when(matrix.getTranslation(Vector3.Zero)).thenReturn(new Vector3(0, 0, 0));

        Vector3 mouseposition = new Vector3(1000000, 0, 0);
        cue.setDragOriginMouse(new Vector3(0, 0, 0));
        cue.shoot(mouseposition, cueBall);

        assertEquals(Cue3D.FORCE_CAP, cueBall.getSpeed(), 0.0001f);
    }

    /**
     * Test case to verify that the cue opacity is set to zero when hideCue is called.
     */
    @Test
    public void testHideCue() {
        cue.getBlendingAttribute().opacity = 1;
        assertEquals(1, cue.getBlendingAttribute().opacity);
        cue.hideCue();
        assertEquals(0, cue.getBlendingAttribute().opacity);
    }

    /**
     * Test case to verify that the cue position is set to the left of the
     * cueball when the mouseposition is at the center of the cueball.
     */
    @Test
    public void testVerifyMousePosition() {
        Vector3 mousePosition = new Vector3(0, 0, 0);
        Vector3 ballPosition = new Vector3(0, 0, 0);
        cue.verifyMousePosition(mousePosition, ballPosition);

        assertEquals(new Vector3(-1, 0, 0), mousePosition);
    }


    /**
     * Test case to verify that the cue rotates when it is set to a new position.
     */
    @Test
    public void testRotateCueIsCalled() {

        CueBall3D ball = makeCueBall();
        Cue3D cueSpy = Mockito.spy(cue);
        Mockito.doNothing().when(cueSpy).rotateCue(ball);
        cueSpy.toPosition(new Vector3(1, 0, 0), ball);

        Mockito.verify(cueSpy, Mockito.times(1)).rotateCue(ball);

    }

    /**
     * Test case to verify that the cue rotates when it is set to a new position.
     */
    @Test
    public void testRotateCueIsCalledInDrag() {

        CueBall3D ball = makeCueBall();
        Cue3D cueSpy = Mockito.spy(cue);
        Mockito.doNothing().when(cueSpy).rotateCue(ball);
        cueSpy.toDragPosition(new Vector3(1, 0, 0), ball);

        Mockito.verify(cueSpy, Mockito.times(1)).rotateCue(ball);
    }

    /**
     * Test case to verify that the cue processes the input when it goes from Hidden to Rotating.
     */
    @Test
    public void testProcessInputWhenHidden() {

        Cue3D cueSpy = Mockito.spy(cue);
        Mockito.doNothing().when(cueSpy).toPosition(any(Vector3.class), any(CueBall3D.class));

        cueSpy.setState(Cue3D.State.Hidden);
        callProcessInput(cueSpy);
        assertEquals(Cue3D.State.Rotating, cueSpy.getState());

        Mockito.verify(cueSpy, Mockito.times(1)).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue processes the input when it goes from Dragging to Hidden.
     */
    @Test
    public void testProcessInputWhenDragging() {

        Cue3D cueSpy = Mockito.spy(cue);
        Mockito.doNothing().when(cueSpy).toPosition(any(Vector3.class), any(CueBall3D.class));

        cueSpy.setState(Cue3D.State.Dragging);
        callProcessInput(cueSpy);
        assertEquals(Cue3D.State.Hidden, cueSpy.getState());

        Mockito.verify(cueSpy, Mockito.times(1)).shoot(any(Vector3.class), any(CueBall3D.class));
        Mockito.verify(cueSpy, Mockito.never()).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue processes the input when it goes from Dragging to Hidden.
     */
    @Test
    public void testProcessInputOnLeftClick() {
        ArrayList<Ball3D> poolBalls = new ArrayList<>();
        poolBalls.add(makeCueBall());

        Scene3D scene = Mockito.mock(Scene3D.class);
        Mockito.when(scene.getPoolBalls()).thenReturn(poolBalls);
        Mockito.when(scene.getUnprojectedMousePosition()).thenReturn(new Vector3(0, 0,0));

        Input input = Mockito.mock(Input.class);
        Mockito.when(input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);
        GameState gameState = Mockito.mock(GameState.class);


        Cue3D cueSpy = Mockito.spy(cue);
        Mockito.doNothing().when(cueSpy).toDragPosition(any(Vector3.class), any(CueBall3D.class));

        cueSpy.setState(Cue3D.State.Rotating);
        cueSpy.processInput(input, scene, gameState);
        assertEquals(Cue3D.State.Dragging, cueSpy.getState());

        Mockito.verify(cueSpy, Mockito.times(1))
                .toDragPosition(any(Vector3.class), any(CueBall3D.class));
        Mockito.verify(cueSpy, Mockito.never()).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue doesn't go over the capped force.
     */
    @Test
    public void testMaxForce() {
        float overCap = Cue3D.FORCE_CAP + 1;
        assertEquals(Cue3D.FORCE_CAP, cue.capMaxForce(overCap));

        float underCap = Cue3D.FORCE_CAP - 0.1f;
        assertEquals(underCap, cue.capMaxForce(underCap));
    }

    /**
     * Test case to verify that the cue rotates when rotateCue is called.
     */
    @Test
    public void testRotateCue() {
        CueBall3D cueball = makeCueBall();
        Mockito.when(cue.getCoordinates()).thenReturn(new Vector3(0, 0,0));
        cue.rotateCue(cueball);
        Mockito.verify(cue.getModel().transform).rotateRad(any(Vector3.class), any(float.class));
    }


    /**
     * Helper method that makes a cue ball.
     * @return CueBall3D cue ball.
     */
    public CueBall3D makeCueBall() {
        ModelInstance ballModel = Mockito.mock(ModelInstance.class);
        ballModel.transform = new Matrix4();

        // Setup expected bounding box of size 4 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(4, 4, 4);

        // Make the mock model's calculate bounding box method return
        // the constructed box
        Mockito.when(ballModel.calculateBoundingBox(any(BoundingBox.class)))
                .thenReturn(box);

        return new CueBall3D(GameConstants.CUEBALL_ID, ballModel);
    }

    /**
     * Helper method that calls processInput on a cue.
     * @param cue Cue3D object
     */
    public void callProcessInput(Cue3D cue) {
        ArrayList<Ball3D> poolBalls = new ArrayList<>();
        poolBalls.add(makeCueBall());

        Scene3D scene = Mockito.mock(Scene3D.class);
        Mockito.when(scene.getPoolBalls()).thenReturn(poolBalls);
        Mockito.when(scene.getUnprojectedMousePosition()).thenReturn(new Vector3(0, 0,0));

        Input input = Mockito.mock(Input.class);
        GameState gameState = Mockito.mock(GameState.class);

        cue.processInput(input, scene, gameState);
    }

    /**
     * Helper method for testing Cue Shot direction given the
     * specified setup for the ball and the mouse position,
     * and the expected direction. The method handles assertions
     * for the specified setup.
     * @param ballPosition       Position of the ball
     * @param mousePosition      Passed in position of the mouse
     * @param expectedDirection  Expected direction of the cue shot
     */
    private void testCueShotDirectionHelper(Vector3 ballPosition,
                                            Vector3 mousePosition, Vector3 expectedDirection) {
        // Create mock Ball3D instance
        final int id = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);
        final Matrix4 matrix = Mockito.mock(Matrix4.class);

        // When we get the position of the model (by getting the translation of
        // a zero vector), then the ball position should be returned.
        Mockito.when(matrix.getTranslation(Vector3.Zero)).thenReturn(ballPosition);

        // Set the mock matrix to the model
        model.transform = matrix;

        Ball3D ball = new CueBall3D(id, model);

        // Get the direction given the mouse position
        Vector3 direction = cue.getCueShotDirection(mousePosition, ball);

        // Assert expected direction equal to actual direction
        assertEquals(expectedDirection, direction);
    }
}
