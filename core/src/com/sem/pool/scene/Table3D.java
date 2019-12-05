package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Class representing a 3D Board of a single game.
 */
public class Table3D {
    private transient ModelInstance model;

    private transient ArrayList<HitBox> hitBoxes;
    private transient ArrayList<ModelInstance> modelInstances;
    private transient CollisionHandler collisionHandler;
    private transient ArrayList<HitBox> potHitBoxes;

    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Table3D(ModelInstance model) {
        this.model = model;
        this.hitBoxes = new ArrayList<>();
        this.potHitBoxes = new ArrayList<>();
        this.modelInstances = new ArrayList<>();
    }

    public ArrayList<HitBox> getHitBoxes() {
        return hitBoxes;
    }

    public ModelInstance getModel() {
        return model;
    }

    public void addHitBox(HitBox hitBox) {
        hitBoxes.add(hitBox);
    }

    public void addPotHitBox(HitBox pot) { potHitBoxes.add(pot); }

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
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // Might be unsuppressed later.
    public boolean checkCollision(Ball3D ball) {
        for (HitBox hitBox: hitBoxes) {
            if (collisionHandler.checkHitBoxCollision(ball.getHitBox(), hitBox)) {
                Vector3 normal = new Vector3(hitBox.getNormal().nor());
                ball.setDirection(ball.getDirection().add(
                        // reflected vector
                        normal.scl(-2 * ball.getDirection().dot(normal))));
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
    public boolean checkIfPot(Ball3D ball) {
        for (HitBox pot: potHitBoxes) {
            if (collisionHandler.checkHitBoxCollision(ball.getHitBox(), pot)){
                System.out.println("POTTED");
                ball.pot();
                return true;
            }
        }
        return false;
    }
}
