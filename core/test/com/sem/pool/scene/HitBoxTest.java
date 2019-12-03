package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the HitBox class.
 */
public class HitBoxTest {

    /**
     * Test method to verify that the HitBox instance is constructed properly.
     */
    @Test
    void testConstructor(){
        btCollisionShape mockShape = Mockito.mock(btCollisionShape.class);
        btCollisionObject mockObject = Mockito.mock(btCollisionObject.class);
        HitBox hitBox = new HitBox(mockShape, mockObject);
        assertEquals(hitBox.getShape(), mockShape);
        assertEquals(hitBox.getObject(), mockObject);
    }
}
