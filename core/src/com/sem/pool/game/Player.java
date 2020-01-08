package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private transient int id;
    private transient RegularBall3D.Type ballType;
    private transient Set<RegularBall3D> pottedBalls;
    private transient int ballsLeft;

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
        if (ball.getType() == ballType) {
            pottedBalls.add(ball);
            ballsLeft--;
        }
    }

    /**
     * Checks whether the Player has potted all of their balls, given
     * a Set of balls that are not yet potted
     * @param unpotted - Set of unpotted balls
     * @return  True iff Player has ball type assigned & all balls were potted.
     */
    public boolean allBallsPotted(Set<Ball3D> unpotted) {
        return false;
    }
}
