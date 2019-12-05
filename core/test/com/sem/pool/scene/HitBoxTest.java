package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    void testConstructor() {
        btCollisionShape mockShape = Mockito.mock(btCollisionShape.class);
        btCollisionObject mockObject = Mockito.mock(btCollisionObject.class);
        HitBox hitBox = new HitBox(mockShape, mockObject);
        assertEquals(hitBox.getShape(), mockShape);
        assertEquals(hitBox.getObject(), mockObject);
    }

    @Test
    void testEquals() {
        btCollisionShape mockShape = Mockito.mock(btCollisionShape.class);
        btCollisionObject mockObject = Mockito.mock(btCollisionObject.class);
        final HitBox hitBox = new HitBox(mockShape, mockObject);
        final HitBox hitBox1 = new HitBox(mockShape, mockObject);
        final HitBox hitBox2 = new HitBox(mockShape, Mockito.mock(btCollisionObject.class));

        assertEquals(hitBox.getShape(), mockShape);
        assertEquals(hitBox.getObject(), mockObject);
        assertEquals(hitBox, hitBox1);
        assertFalse(hitBox.equals(hitBox2));
        assertFalse(hitBox.equals("test"));
    }


    @Test
    void testHashCode() {
        btCollisionShape mockShape = Mockito.mock(btCollisionShape.class);
        btCollisionObject mockObject = Mockito.mock(btCollisionObject.class);
        HitBox hitBox = new HitBox(mockShape, mockObject);
        HitBox hitBox1 = new HitBox(mockShape, mockObject);
        HitBox hitBox2 = new HitBox(mockShape, Mockito.mock(btCollisionObject.class));

        assertEquals(hitBox.hashCode(), hitBox.hashCode());
        assertNotEquals(hitBox.hashCode(), hitBox1.hashCode());
        assertNotEquals(hitBox.hashCode(), hitBox2.hashCode());
    }
}
