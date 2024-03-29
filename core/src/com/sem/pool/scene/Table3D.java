package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Class representing a 3D Board of a single game.
 */
public class Table3D extends Object3D {
    private transient ArrayList<HitBox> hitBoxes;
    private transient CollisionHandler collisionHandler;

    public static ArrayList<HitBox> potHitBoxes;

    // bounding borders for the board
    public static final float xBound = 3.05f;
    public static final float zBound = 1.45f;

    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Table3D(ModelInstance model) {
        super(model);
        this.hitBoxes = new ArrayList<>();
    }

    public ArrayList<HitBox> getHitBoxes() {
        return hitBoxes;
    }

    public void addHitBox(HitBox hitBox) {
        hitBoxes.add(hitBox);
    }

    /**
     * Method to add a pot hit box to the table.
     * These will not be checked for regular collisions
     * but only for when potting is being checked.
     * @param pot HitBox of the pot.
     */
    public void addPotHitBox(HitBox pot) {
        potHitBoxes.add(pot);
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }

    public ArrayList<HitBox> getPotHitBoxes() {
        return potHitBoxes;
    }

    /**
     * Checks whether a ball has collided with any of the hit boxes of the table.
     * @param ball Ball that we check collisions with.
     * @return whether the ball collided with the table.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // Suppressed as PMD flags hit box
    // as a UR anomaly / being undefined
    // Checking for UR anomalies has been removed in updated versions of PMD: https://pmd.github.io/2019/10/31/PMD-6.19.0/
    public boolean checkCollision(Ball3D ball) {
        for (HitBox hitBox: hitBoxes) {
            if (collisionHandler.checkHitBoxCollision(ball.getHitBox(), hitBox)) {
                Vector3 newDirection = PhysicsUtils.reflectVector(
                        new Vector3(ball.getDirection()),
                        new Vector3(hitBox.getNormal()));
                ball.setDirection(newDirection);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a ball is currently colliding with one of the pots.
     * If this is the case the ball will be potted.
     * @param ball Ball that we check collisions with.
     * @return whether or not a ball was potted.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // Suppressed as PMD flags pot
    // as a UR anomaly / being undefined
    // Checking for UR anomalies has been removed in updated versions of PMD: https://pmd.github.io/2019/10/31/PMD-6.19.0/
    public boolean checkIfPot(Ball3D ball) {
        for (HitBox pot: Table3D.potHitBoxes) {
            if (collisionHandler.checkHitBoxCollision(ball.getHitBox(), pot)) {
                return true;
            }
        }
        return false;
    }
}
