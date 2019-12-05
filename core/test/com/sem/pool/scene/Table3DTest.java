package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;



/**
 * Test class containing unit tests for the Table3D class.
 */
class Table3DTest {
    /**
     * Test case to verify that the constructor of Table3D
     * properly sets the model of the Table.
     */
    @Test
    public void testConstructor() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        Table3D board = new Table3D(model);

        assertEquals(model, board.getModel());
    }

    /**
     * Tests the getters and setters of the fields.
     */
    @Test
    public void testFields() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        Table3D board = new Table3D(model);

        ArrayList<HitBox> hitBoxes = new ArrayList<>();
        assertEquals(board.getHitBoxes(), hitBoxes);

        HitBox mockHitBox = Mockito.mock(HitBox.class);
        hitBoxes.add(mockHitBox);
        board.addHitBox(mockHitBox);
        assertEquals(board.getHitBoxes(), hitBoxes);

        ArrayList potHitBoxes = new ArrayList();
        assertEquals(board.getPotHitBoxes(), potHitBoxes);
        HitBox mockedPot = Mockito.mock(HitBox.class);
        potHitBoxes.add(mockedPot);
        board.addPotHitBox(mockedPot);
        assertEquals(board.getPotHitBoxes(), potHitBoxes);

        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        board.setCollisionHandler(mockedHandler);
        assertEquals(mockedHandler, board.getCollisionHandler());
    }

    /**
     * Test if the handler is called by the table on collision check.
     */
    @Test
    public void testCollisions() {
        Ball3D mockedBall = Mockito.mock(Ball3D.class);

        ModelInstance model = Mockito.mock(ModelInstance.class);
        Table3D board = new Table3D(model);
        HitBox mockedHitBox = Mockito.mock(HitBox.class);
        board.addHitBox(mockedHitBox);
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        Mockito.when(mockedHandler.checkHitBoxCollision(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        Vector3 normal = new Vector3(0,1,0);
        Vector3 direction = new Vector3(1,0,0);
        Mockito.when(mockedHitBox.getNormal()).thenReturn(normal);
        Mockito.when(mockedBall.getDirection()).thenReturn(direction);
        board.setCollisionHandler(mockedHandler);
        assertTrue(board.checkCollision(mockedBall));
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));
        Vector3 reflectedVector = direction.add(normal.scl(-2 * direction.dot(normal)));
        Mockito.verify(mockedBall, Mockito.times(1)).setDirection(reflectedVector);
    }

    /**
     * Test if the handler is called by the table on collision check.
     */
    @Test
    public void testPotCollisions() {
        Ball3D mockedBall = Mockito.mock(Ball3D.class);

        ModelInstance model = Mockito.mock(ModelInstance.class);
        Table3D board = new Table3D(model);
        HitBox mockedPot = Mockito.mock(HitBox.class);
        board.addPotHitBox(mockedPot);
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        board.setCollisionHandler(mockedHandler);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        board.checkIfPot(mockedBall);
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));
    }
}