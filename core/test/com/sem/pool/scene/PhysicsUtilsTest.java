package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PhysicsUtilsTest {
    /**
     * Asserts that the calculateSpeed methods
     * returns the root of the summed squares.
     */
    @Test
    public void testCalculateSpeed() {
        final float Vx = 3;
        final float Vz = 4;
        final double speed = 5;
        assertEquals(PhysicsUtils.calculateBallSpeed(Vx, Vz), speed);
    }
}