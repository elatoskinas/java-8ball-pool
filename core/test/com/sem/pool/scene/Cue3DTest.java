package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.sem.pool.game.GameConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
        cue = new Cue3D(model);
    }

    /**
     * Test case to verify that the constructor of Cue3D
     * properly sets the model of the cue.
     */
    @Test
    public void testConstructor() {

        ModelInstance model = Mockito.mock(ModelInstance.class);
        Cue3D cue = new Cue3D(model);
        assertEquals(model, cue.getModel());
    }

    /**
     * Test case to verify that the cue is positioned near the cue ball
     */
    @Test
    public void testCueToBeginPosition() {
        ModelInstance cueModel = Mockito.mock(ModelInstance.class);
        Matrix4 ballMockMatrix = Mockito.mock(Matrix4.class);
        cueModel.transform = ballMockMatrix;
        Cue3D cue = new Cue3D(cueModel);

        ModelInstance ballModel = Mockito.mock(ModelInstance.class);
        ballModel.transform = new Matrix4();
        Ball3D ball = new CueBall3D(0, ballModel);


        final float expectedRadius = 2.f;

        // Setup expected bounding box of size 4 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(4, 4, 4);

        ball.setBoundingBox(box);

        cue.toBeginPosition(ball);
        Mockito.verify(ballMockMatrix, Mockito.times(1))
                .translate(-expectedRadius - Cue3D.CUE_OFFSET, Cue3D.Y_COORDINATE, 0);
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
