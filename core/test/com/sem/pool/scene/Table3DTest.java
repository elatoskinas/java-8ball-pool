package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

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
        board.setCollisionHandler(mockedHandler);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        board.checkCollision(mockedBall);
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));
    }
}