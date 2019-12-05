package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the Ball3D class.
 */
class Ball3DTest {

    /**
     * Test method to verify that the Ball3D object instance
     * is constructed properly (the right values for the id and
     * model are set).
     */
    @Test
    public void testConstructor() {
        final int id = 3;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball = new Ball3D(id, model);

        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
        assertEquals(new Vector3(0, 0, 0), ball.getDirection());
        assertEquals(0f, ball.getSpeed());
    }

    /**
     * Test case to ensure that the ID setter for the Ball3D
     * class sets the ID accordingly.
     */
    @Test
    public void testIdSetter() {
        final int initId = 0;
        final int id = 2;
        final ModelInstance model = Mockito.mock(ModelInstance.class);
        Ball3D ball = new Ball3D(initId, model);

        ball.setId(id);

        assertEquals(id, ball.getId());
    }

    /**
     * Test case to ensure that the direction setter for the Ball3D
     * class sets the direction accordingly.
     */
    @Test
    void testDirectionSetter() {
        Vector3 direction = new Vector3(3f, 4f, 0f);
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        Ball3D ball = new Ball3D(0, mockModel);

        ball.setDirection(direction);

        assertEquals(new Vector3(3f / 5f, 4f / 5f, 0f), ball.getDirection());
    }

    /**
     * Test case to ensure that the speed setter for the Ball3D
     * class sets the speed accordingly.
     */
    @Test
    void testSpeedSetter() {
        float speed = 5.1f;
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        Ball3D ball = new Ball3D(0, mockModel);

        ball.setSpeed(speed);

        assertEquals(speed, ball.getSpeed());
    }

    /**
     * Test case to ensure that the equals method on two
     * different objects with the same values returns true
     * upon calling equals.
     */
    @Test
    public void testEquals() {
        final int id = 1;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id, model);
        Ball3D ball2 = new Ball3D(id, model);

        assertEquals(ball1, ball2);
        assertFalse(ball1.equals("test"));
    }

    /**
     * Test case to ensure that comparing equality between
     * two different objects with differing ids returns false.
     */
    @Test
    public void testNotEqualId() {
        final int id1 = 1;
        final int id2 = 2;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id1, model);
        Ball3D ball2 = new Ball3D(id2, model);

        assertNotEquals(ball1, ball2);
    }

    /**
     * Test case to ensure that comparing equality between
     * two different objects with different models returns false.
     */
    @Test
    public void testNotEqualsModelInstance() {
        final int id = 1;
        final ModelInstance model1 = Mockito.mock(ModelInstance.class);
        final ModelInstance model2 = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id, model1);
        Ball3D ball2 = new Ball3D(id, model2);

        assertNotEquals(ball1, ball2);
    }

    /**
     * Test case to ensure that comparing equality between a Ball3D
     * object with an object of a differnt type returns false.
     */
    @Test
    public void testNotEqualsDifferentObjectType() {
        final int id = 3;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball = new Ball3D(id, model);
        Integer x = 3;
        assertNotEquals(x, ball);
    }

    /**
     * Test case to ensure that two different objects with
     * the same values return the same hashcode.
     */
    @Test
    public void testHashCodeEqual() {
        final int id = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id, model);
        Ball3D ball2 = new Ball3D(id, model);

        int hashCode1 = ball1.hashCode();
        int hashCode2 = ball2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test case to ensure that two different objects
     * with differing values return a hashcode that
     * is not equal (this may not always be the case,
     * but it is in the case of our test).
     */
    @Test
    public void testHashCodeNotEqual() {
        final int id1 = 0;
        final int id2 = 5;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id1, model);
        Ball3D ball2 = new Ball3D(id2, model);

        int hashCode1 = ball1.hashCode();
        int hashCode2 = ball2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    /**
     * Tests getCoordinates returns {0,0,0} after a new ball Object is created.
     */
    @Test
    public void testGetCoordinates() {
        ModelInstance mockModelInstance = Mockito.mock(ModelInstance.class);
        mockModelInstance.transform = new Matrix4();
        Ball3D ball = new Ball3D(0, mockModelInstance);
        Vector3 coordinates = new Vector3(new float[3]);
        assertEquals(coordinates, ball.getCoordinates());
    }


    /**
     * Tests if the matrix translation is called after the translate method is called,
     * and if the translation method has the same argument as the move method.
     */
    @Test
    public void testTranslate() {
        ModelInstance mockModelInstance = Mockito.mock(ModelInstance.class);
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        mockModelInstance.transform = mockMatrix;
        Ball3D ball = new Ball3D(0, mockModelInstance);
        Vector3 translation = new Vector3(1f, 0, 0);
        ball.translate(translation);
        Mockito.verify(mockMatrix, Mockito.times(1)).translate(translation);
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
     *
     * @param ballPosition      Position of the ball
     * @param mousePosition     Passed in position of the mouse
     * @param expectedDirection Expected direction of the cue shot
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

        Ball3D ball = new Ball3D(id, model);

        // Get the direction given the mouse position
        Vector3 direction = ball.getCueShotDirection(mousePosition);

        // Assert expected direction equal to actual direction
        assertEquals(expectedDirection, direction);
    }

    /**
     * Test case to verify that the correct radius is returned
     * upon calling getRadius for the Ball3D when a BoundingBox
     * has not yet been initialized.
     */
    @Test
    public void testGetRadiusBoundingBoxInitiallyNull() {
        final int id1 = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);
        final float expectedRadius = 1.f;

        // Setup expected bounding box of size 2 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(2, 2, 2);

        // Make the mock model's calculate bounding box method return
        // the constructed box
        Mockito.when(model.calculateBoundingBox(Mockito.any(BoundingBox.class)))
                .thenReturn(box);

        Ball3D ball = new Ball3D(id1, model);
        float radius = ball.getRadius();

        assertEquals(expectedRadius, radius);
    }

    /**
     * Test case to verify that the correct radius is returned
     * upon calling getRadius for the Ball3D when a BoundingBox
     * has already been initialized.
     */
    @Test
    public void testGetRadiusBoundingBoxInitialized() {
        final int id1 = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);
        final float expectedRadius = 2.f;

        // Setup expected bounding box of size 4 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(4, 4, 4);

        // Make the mock model's calculate bounding box method return
        // the constructed box
        Mockito.when(model.calculateBoundingBox(Mockito.any(BoundingBox.class)))
                .thenReturn(box);

        Ball3D ball = new Ball3D(id1, model);

        ball.getRadius(); // Perform radius side effect to construct bounding box
        float radius = ball.getRadius(); // Get radius again

        assertEquals(expectedRadius, radius);
    }

    /**
     * Tests the setup boxes method.
     */
    @Test
    public void testSetupBoxes() {
        Bullet.init();
        final int id1 = 0;
        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = new Matrix4();

        // Setup expected bounding box of size 4 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(4, 4, 4);

        // Make the mock model's calculate bounding box method return
        // the constructed box
        Mockito.when(model.calculateBoundingBox(Mockito.any(BoundingBox.class)))
                .thenReturn(box);

        Ball3D ball = new Ball3D(id1, model);

        ball.getRadius(); // Perform radius side effect to construct bounding box
        ball.setUpBoxes();
        assertEquals(ball.getModel(), model);
        assertTrue(ball.isSetUp());
    }

    /**
     * Tests that the move method calls the translate method for the matrix.
     */
    @Test
    public void testMove() {
        Bullet.init();
        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = new Matrix4();

        // Setup expected bounding box of size 4 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(4, 4, 4);

        // Make the mock model's calculate bounding box method return
        // the constructed box
        Mockito.when(model.calculateBoundingBox(Mockito.any(BoundingBox.class)))
                .thenReturn(box);

        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        model.transform = mockMatrix;
        Ball3D ball = new Ball3D(0, model);
        final Vector3 translation = new Vector3(1f, 0, 0);
        ball.setDirection(new Vector3(1,0,0));
        ball.setSpeed(1f);
        ball.move();
        Mockito.verify(mockMatrix, Mockito.times(1)).translate(translation);
    }
}

