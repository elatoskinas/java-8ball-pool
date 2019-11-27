package com.sem.pool;

import com.badlogic.gdx.math.Vector3;

/**
 * Stateless class that holds global game constants
 * for Pool games.
 */
public final class GameConstants {
    // Fixed count of pool balls in one Pool game:
    // Cue ball + 8-ball + 7 striped + 7 solid balls
    public static final int BALL_COUNT = 16;

    public static final Vector3 BALL_OFFSET = new Vector3(1f, 0.28f, 0f);
    public static final Vector3 CUE_BALL_OFFSET = new Vector3(-1.75f, 0.28f, 0f);
}
