package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
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
    }

    /**
     * Test case to ensure that the ID setter for the Ball3D
     * class sets the ID accordingly.
     */
    @Test
    public void testIdSetter() {
        final int initId = 0;
        final int id = 2;
        Ball3D ball = new Ball3D(initId, null);

        ball.setId(id);

        assertEquals(id, ball.getId());
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
        final ModelInstance model = null;

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
        final ModelInstance model = null;

        Ball3D ball1 = new Ball3D(id1, model);
        Ball3D ball2 = new Ball3D(id2, model);

        int hashCode1 = ball1.hashCode();
        int hashCode2 = ball2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetCoordinates() {
        ModelInstance mockModelInstance = Mockito.mock(ModelInstance.class);
        mockModelInstance.transform = new Matrix4();
        Ball3D ball = new Ball3D(0, mockModelInstance);
        Vector3 coordinates = new Vector3(new float[3]);
        assertEquals(coordinates, ball.getCoordinates());
    }


    @Test
    public void testMove() {
        // DOES NOT CURRENTLY VERIFY THAT NEW POSITION IS CORRECT ACCORDING TO TRANSLATION
        ModelInstance mockModelInstance = Mockito.mock(ModelInstance.class);
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        mockModelInstance.transform = mockMatrix;
        Ball3D ball = new Ball3D(0, mockModelInstance);
        Vector3 translation = new Vector3(1f,0,0);
        ball.move(translation);
        Mockito.verify(mockMatrix, Mockito.times(1)).translate(translation);
    }

    @Test
    public void testApplyForce() {
        // DOES NOT CURRENTLY VERIFY THAT NEW POSITION IS CORRECT ACCORDING TO TRANSLATION
        ModelInstance mockModelInstance = Mockito.mock(ModelInstance.class);
        mockModelInstance.transform = Mockito.mock(Matrix4.class);
        Ball3D ball = new Ball3D(0, mockModelInstance);
        Vector3 translation = new Vector3(1f,0,0);
        float scalar = 10;
        Ball3D spyBall = Mockito.spy(ball);
        spyBall.applyForce(scalar, translation);
        Mockito.verify(spyBall, Mockito.times(1)).move(new Vector3(10f, 0, 0));
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
}