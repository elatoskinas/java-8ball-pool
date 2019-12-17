package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private int id;
    private RegularBall3D.Type ballType;
    Set<RegularBall3D> pottedBalls;

    /**
     * Creates a new Player with the specified id.
     * @param id  ID to assign to the player
     */
    public Player(int id) {
        this.id = id;
        pottedBalls = new HashSet<>();
        // TODO: Initialize ball type
    }

    public int getId() {
        return id;
    }

    public Set<RegularBall3D> getPottedBalls() {
        return pottedBalls;
    }

    /**
     * Assigns the specified ball type to the Player.
     * @param ballType  Ball Type to assign to the Player.
     */
    public void assignBallType(RegularBall3D.Type ballType) {
        // TODO: Implement ball type assignment
        // TODO: Take into account that the ball type should not
        //       be assigned during the break shot
        // TODO: Do not assign ball type when cue ball is potted
    }

    public void potBall(RegularBall3D ball) {
        // TODO: Implement ball potting logic
        pottedBalls.add(ball);
    }
}
