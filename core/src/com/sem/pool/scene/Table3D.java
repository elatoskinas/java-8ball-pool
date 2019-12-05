package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Class representing a 3D Board of a single game.
 */
public class Table3D {
    private final transient ModelInstance model;

    private transient ArrayList<HitBox> hitBoxes;
    private transient ArrayList<ModelInstance> modelInstances;
    private transient CollisionHandler collisionHandler;

    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Table3D(ModelInstance model) {
        this.model = model;
        this.hitBoxes = new ArrayList<>();
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

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
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
                Vector3 normal = new Vector3(hitBox.getNormal().nor());
                Vector3 reflectedVector = ball.getDirection().add(normal
                        .scl(-2 * ball.getDirection().dot(normal)));
                ball.setDirection(reflectedVector);
                return true;
            }
        }
        return false;
    }
}
