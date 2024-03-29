package com.sem.pool.scene;

import com.sem.pool.game.GameConstants;

import java.util.List;

/**
 * Collection of 3D Scene elements that are part
 * of a Game of Pool.
 * This class groups the objects together in one cohesive unit.
 */
public final class GameElements {
    private final List<Ball3D> poolBalls;
    private final Table3D table;
    private final Cue3D cue;

    /**
     * Creates a new group of Game elements.
     * @param poolBalls  List of pool balls
     * @param table      3D Table object
     * @param cue        3D Cue object
     */
    public GameElements(List<Ball3D> poolBalls, Table3D table, Cue3D cue) {
        this.poolBalls = poolBalls;
        this.table = table;
        this.cue = cue;
    }

    public List<Ball3D> getPoolBalls() {
        return poolBalls;
    }

    public Table3D getTable() {
        return table;
    }

    public Cue3D getCue() {
        return cue;
    }

    public CueBall3D getCueBall() {
        return (CueBall3D) poolBalls.get(GameConstants.CUEBALL_ID);
    }
}
