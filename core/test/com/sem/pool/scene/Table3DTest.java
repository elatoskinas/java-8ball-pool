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
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;


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
     * Tests the getters and setters of the fields
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
        // Simplified collision handler to test if the table returns what it shouldHitBox mockedBallHitBox = Mockito.mock(HitBox.class);
        board.checkCollision(mockedBall);
        Mockito.verify(mockedHandler, Mockito.times(1)).checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));
    }
}