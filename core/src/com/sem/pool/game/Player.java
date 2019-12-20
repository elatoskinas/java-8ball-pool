package com.sem.pool.game;

import com.sem.pool.scene.RegularBall3D;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private transient int id;
    private transient RegularBall3D.Type ballType;
    private transient Set<RegularBall3D> pottedBalls;

    /**
     * Creates a new Player with the specified id.
     * @param id  ID to assign to the player
     */
    public Player(int id) {
        this.id = id;
        pottedBalls = new HashSet<>();
        this.ballType = RegularBall3D.Type.UNASSIGNED;
    }

    public int getId() {
        return id;
    }

    public RegularBall3D.Type getBallType() {
        return ballType;
    }

    public Set<RegularBall3D> getPottedBalls() {
        return pottedBalls;
    }

    /**
     * Assigns the specified ball type to the Player.
     * @param ballType  Ball Type to assign to the Player.
     */
    public void assignBallType(RegularBall3D.Type ballType) {
        this.ballType = ballType;
    }

    /**
     * Pots the ball for the Player. To be called when this
     * Player pots the specified ball.
     * @param ball  Ball that the player has potted
     */
    public void potBall(RegularBall3D ball) {
        pottedBalls.add(ball);
    }
}
