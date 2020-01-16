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
import com.sem.pool.game.GameConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the Ball3D class.
 */
abstract class Ball3DTest {

    /**
     * Returns an instance of Ball3D.
     * @param id id of the ball.
     * @param model model of the ball.
     * @return Instance of Ball3D.
     */
    protected abstract Ball3D getBall(int id, ModelInstance model);

    /**
     * Force every ball test to test its constructor.
     */
    @Test
    public abstract void testConstructor();


    /**
     * Test case to ensure that the ID setter for the Ball3D
     * class sets the ID accordingly.
     */
    @Test
    public void testIdSetter() {
        final int initId = 0;
        final int id = 2;
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        Ball3D ball = getBall(initId, mockModel);

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
        Ball3D ball = getBall(0, mockModel);

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
        Ball3D ball = getBall(0, mockModel);

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

        Ball3D ball1 = getBall(id, model);
        Ball3D ball2 = getBall(id, model);

        assertEquals(ball1, ball2);
        assertNotEquals(ball1, "test");
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

        Ball3D ball1 = getBall(id1, model);
        Ball3D ball2 = getBall(id2, model);

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

        Ball3D ball1 = getBall(id, model1);
        Ball3D ball2 = getBall(id, model2);

        assertNotEquals(ball1, ball2);
    }

    /**
     * Test case to ensure that comparing equality between a Ball3D
     * object with an object of a different type returns false.
     */
    @Test
    public void testNotEqualsDifferentObjectType() {
        final int id = 3;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball = getBall(id, model);
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

        Ball3D ball1 = getBall(id, model);
        Ball3D ball2 = getBall(id, model);

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

        Ball3D ball1 = getBall(id1, model);
        Ball3D ball2 = getBall(id2, model);

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
        Ball3D ball = getBall(0, mockModelInstance);
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
        Ball3D ball = getBall(0, mockModelInstance);
        Vector3 translation = new Vector3(1f, 0, 0);
        ball.translate(translation);
        Mockito.verify(mockMatrix, Mockito.times(1)).trn(translation);
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

        Ball3D ball = getBall(id1, model);

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

        Ball3D ball = getBall(id1, model);

        float radius = ball.getRadius(); // Get radius

        assertEquals(expectedRadius, radius);
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
        Ball3D ball = getBall(0, model);
        final Vector3 translation = new Vector3(1f, 0, 0);
        ball.setDirection(new Vector3(1,0,0));
        ball.setSpeed(1f);
        // set our translation scaled to what we expect the ball to have
        translation.scl((ball.getSpeed() - GameConstants.DRAG_COEFFICIENT));
        ball.move(1);
        Mockito.verify(mockMatrix, Mockito.times(1)).trn(translation);
    }

    /**
     * Tests whether the boundary for the move method that changes the speed works properly.
     */
    @Test
    public void testMoveBoundaryOutPoint() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = new Matrix4();
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        model.transform = mockMatrix;
        Ball3D ball = getBall(0, model);
        ball.setSpeed(-1);
        final float deltaTime = 1;
        ball.move(deltaTime);
        // tests if negative speed (below boundary) is set to 0
        assertEquals(ball.getSpeed(), 0);
        // test if speed on boundary is set to zero
        ball.setSpeed(GameConstants.MIN_SPEED);
        ball.move(deltaTime);
        assertEquals(ball.getSpeed(), 0);
        // test if speed above boundary is not set to zero
        // but decremented by the drag coefficient times delta time.
        ball.setSpeed(GameConstants.MIN_SPEED + 1);
        ball.move(deltaTime);
        assertEquals(ball.getSpeed(),
                GameConstants.MIN_SPEED + 1 - GameConstants.DRAG_COEFFICIENT * deltaTime);
    }

    /**
     * Tests whether when the speed is on the boundary the speed is set to 0.
     */
    @Test
    public void testBoundaryOn() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = new Matrix4();
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        model.transform = mockMatrix;
        Ball3D ball = getBall(0, model);
        final float deltaTime = 1;
        // test if speed on boundary is set to zero
        ball.setSpeed(GameConstants.MIN_SPEED);
        ball.move(deltaTime);
        assertEquals(ball.getSpeed(), 0);
    }

    /**
     * Tests whether when the speed is above the boundary the speed is not set to 0.
     */
    @Test
    public void testBoundaryInPoint() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = new Matrix4();
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        model.transform = mockMatrix;
        Ball3D ball = getBall(0, model);
        final float deltaTime = 1;
        // test if speed above boundary is not set to zero
        // but decremented by the drag coefficient times delta time.
        ball.setSpeed(GameConstants.MIN_SPEED + 1);
        ball.move(deltaTime);
        assertEquals(ball.getSpeed(),
                GameConstants.MIN_SPEED + 1 - GameConstants.DRAG_COEFFICIENT * deltaTime);
    }

    /**
     * Test case to test if the inMotion() method returns
     * the right value when the Ball is moving.
     */
    @Test
    void testInMotion() {
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        Ball3D ball = getBall(1, mockModel);
        
        ball.setSpeed(1);
        
        assertTrue(ball.isInMotion());
    }

    /**
     * Test case to test if the inMotion method returns
     * the right vlue when the ball is stationary.
     */
    @Test
    void testNotInMotion() {
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        Ball3D ball = getBall(1, mockModel);

        ball.setSpeed(0);

        assertFalse(ball.isInMotion());
    }

    /**
     * Test if the handler is called by the ball on collision check.
     */
    @Test
    public void testCollisions() {
        final int initId = 0;
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        final Ball3D mockedBall = Mockito.mock(Ball3D.class);
        Ball3D ball = getBall(initId, mockModel);
        ball.setSpeed(GameConstants.MIN_SPEED + 1);
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        ball.setCollisionHandler(mockedHandler);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        ball.checkCollision(mockedBall);
        assertFalse(ball.checkCollision(ball));
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(), Mockito.any(HitBox.class));
    }

    /**
     * Test if the handler is called by the ball on collision check.
     */
    @Test
    public void testIfCollision() {
        // mock modelinstance for ball
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        // mock matrix for ball transform
        Matrix4 mockedMatrix = Mockito.mock(Matrix4.class);

        // created mocked ball to collide with
        Ball3D other = getBall(0, mockModel);
        Vector3 position = new Vector3(0,0,0);
        Mockito.when(mockedMatrix.getTranslation(Mockito.any())).thenReturn(position);
        other.getModel().transform = mockedMatrix;
        // create ball we use to test
        Ball3D ball = getBall(0, mockModel);
        // set transform of model
        ball.getModel().transform = mockedMatrix;

        // mock handler, set handler to always return true and set handler for ball
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        Mockito.when(mockedHandler.checkHitBoxCollision(Mockito.any(),
                Mockito.any())).thenReturn(true);
        ball.setCollisionHandler(mockedHandler);

        // set ball speed to above minimum
        ball.setSpeed((GameConstants.MIN_SPEED + 1f));
        // assert that if the handler returns true, the checkCollision method returns true.
        assertTrue(ball.checkCollision(other));
        // verify that the handler is called once
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(), Mockito.any());
    }

    /**
     * Additional tests to test if the collisionCheck method returns no when it should.
     */
    @Test
    public void testCollisionCheck() {
        // mock modelinstance for ball
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        // mock matrix for ball transform
        Matrix4 mockedMatrix = Mockito.mock(Matrix4.class);

        // created mocked ball to collide with
        Ball3D other = getBall(0, mockModel);
        Vector3 position = new Vector3(0,0,0);
        Mockito.when(mockedMatrix.getTranslation(Mockito.any())).thenReturn(position);
        other.getModel().transform = mockedMatrix;
        // create ball we use to test
        Ball3D ball = getBall(0, mockModel);
        // set transform of model
        ball.getModel().transform = mockedMatrix;

        // mock handler, set handler to always return true and set handler for ball
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        Mockito.when(mockedHandler.checkHitBoxCollision(Mockito.any(),
                Mockito.any())).thenReturn(true);
        ball.setCollisionHandler(mockedHandler);
    }

    /**
     * Tests if the pot method works properly.
     */
    @Test
    public void testPot() {
        // mock model instance for ball
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);

        Mockito.when(mockMatrix.getTranslation(new Vector3()))
                .thenReturn(new Vector3(0, -100, 0).scl(-1));
        mockModel.transform = mockMatrix;
        Ball3D ball = getBall(0, mockModel);

        // created mocked ball to collide with
        ball.pot();
        assertEquals(ball.getCoordinates(), new Vector3(0, -100, 0));
        assertEquals(ball.getSpeed(), 0);
        assertEquals(ball.getDirection(), new Vector3());

    }
}