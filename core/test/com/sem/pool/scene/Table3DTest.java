package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
}