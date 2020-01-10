package com.sem.pool.game;

/**
 * Stateless class that holds global game constants
 * for Pool games.
 */
public final class GameConstants {
    // Fixed count of pool balls in one Pool game:
    // Cue ball + 8-ball + 7 striped + 7 solid balls
    public static final int BALL_COUNT = 16;

    // Regular ball count per type is 7 -> 7 striped and 7 solid balls
    public static final int REGULAR_BALL_COUNT = 7;
    public static final int CUEBALL_ID = 0;
    public static final int EIGHTBALL_ID = 8;

    // Set to 0.03f after experimenting, should be increased if ball stops too slowly.
    public static final float DRAG_COEFFICIENT = 0.03f;
    // Minimal speed for a ball before its speed it set to 0.
    public static final float MIN_SPEED = 0.0001f;


    // CUE
    // Cue max shooting force
    public static final float MAX_CUE_FORCE = 0.3f;

    // Cue max distance when dragging
    public static final float MAX_DRAG_DISTANCE = 1f;

    // The distance between the cueBall and the tip of the cue.
    public static final float CUE_OFFSET = 0.05f;


}
