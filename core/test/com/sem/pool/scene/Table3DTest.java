package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
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

    @Test
    public void testSetupBoundingBoxes() {
        Bullet.init();

        ModelInstance model = Mockito.mock(ModelInstance.class);
        Table3D board = new Table3D(model);
        model.transform = Mockito.mock(Matrix4.class);
        board.setUpBoundingBorders();
        Mockito.verify(model.transform.translate(Mockito.any()));
    }
}