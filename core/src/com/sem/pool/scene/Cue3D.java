package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Class representing a 3D Cue.
 * TODO: Rotation of the cue.
 * TODO: Set position according to the force.
 */
public class Cue3D {

    // The distance between the cueBall and the tip of the cue.
    protected static final float CUE_OFFSET = 0.05f;

    // The Y of the cue can't be 0 because it will end up in the bumpers.
    protected static final float Y_COORDINATE = 1f;

    private transient ModelInstance model;

    /**
     * Constructs a new 3D Cue instance.
     */
    public Cue3D(ModelInstance model) {
        this.model = model;
    }

    public ModelInstance getModel() {
        return model;
    }

    /**
     * Returns the current coordinates of the cue.
     * @return The coordinates of the cue.
     */
    public Vector3 getCoordinates() {
        return this.model.transform.getTranslation(new Vector3());
    }

    /**
     * Given the mouse position, determines the direction of the cue
     * shot for the cueball.
     *
     * @param mousePosition  Position of the mouse as a 3D Vector
     * @param cueBall  Ball3D to get the position of the cueBall
     * @return  Direction of the cue shot
     */
    public Vector3 getCueShotDirection(Vector3 mousePosition, Ball3D cueBall) {
        Vector3 ballPosition = cueBall.getCoordinates();

        // The direction is the center of the ball (ball position)
        // from which the mouse position is subtracted.
        // We normalize this vector to reduce ambiguity with direction,
        // and work on unit length vectors.
        Vector3 direction = new Vector3();
        direction.add(ballPosition).sub(mousePosition);
        direction.y = 0; // Set y direction 0 because we never move up
        direction.nor();
        return direction;
    }

    /**
     * Set the cue to the begin position when there is no mouse input yet.
     * @param cueBall cue ball to determine cue position.
     */
    public void toBeginPosition(Ball3D cueBall) {

        Vector3 position = cueBall.getCoordinates();
        // Sets the cue left from the cue ball
        float x = position.x - CUE_OFFSET - cueBall.getRadius();

        model.transform.translate(x,  Y_COORDINATE, position.z);
    }

    /**
     * Shoots the cueball based on the mouse-postion and the mouse-position when the drag started.
     * @param mousePosition the mouse coordinates
     * @param cueBall the cueball
     */
    public void shoot(Vector3 mousePosition, Ball3D cueBall) {
        // Calculates the force based on the distance
        // TODO: Add force control
        float force = 0.3f;
        Vector3 direction = getCueShotDirection(mousePosition, cueBall);

        // Apply the force in the shoot direction
        cueBall.setDirection(direction);
        cueBall.setSpeed(force);
    }

}
