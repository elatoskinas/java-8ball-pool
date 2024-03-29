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
    
    // Attribute to help make turn advancement easy
    private boolean pottedCorrectBall;

    /**
     * Creates a new Player with the specified id.
     * @param id  ID to assign to the player
     */
    public Player(int id) {
        this.id = id;
        pottedBalls = new HashSet<>();
        this.ballType = RegularBall3D.Type.UNASSIGNED;
        this.pottedCorrectBall = false;
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

    public boolean getPottedCorrectBall() {
        return pottedCorrectBall;
    }

    public void setPottedCorrectBall(boolean b) {
        this.pottedCorrectBall = b;
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
            pottedCorrectBall = true;
        }
    }

    /**
     * Checks whether the Player has potted all of their balls, given
     * a Set of balls that are not yet potted.
     * @param unpotted - Set of unpotted balls
     * @return  True iff Player has ball type assigned & all balls were potted.
     */
    // UR anomaly false positive triggered by foreach loop (ball variable)
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean allBallsPotted(Set<Ball3D> unpotted) {
        if (ballType == RegularBall3D.Type.UNASSIGNED) {
            return false;
        }

        for (Ball3D ball : unpotted) {
            if (ball instanceof RegularBall3D
                    && ((RegularBall3D)ball).getType() == ballType) {
                return false;
            }
        }

        return true;
    }
}
