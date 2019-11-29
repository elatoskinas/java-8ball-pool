package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Class representing a 3D Cue.
 * TODO: Rotation of the cue.
 * TODO: Set position according to the force.
 */
public class Cue3D {

    /**
     * The distance between the center of the cueBall and the tip of the cue.
     */
    private static final float cueOffset = 0.05f;

    // The Y of the cue can't be 0 because it will end up in the bumpers.
    private static final float yCoordinate = 0.1f;
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
     * Set the cue near the cue ball.
     * @param cueBall Ball3D cue ball to determine position.
     */
    public void toBeginPosition(Ball3D cueBall) {
        Vector3 position = cueBall.getModel().transform.getTranslation(new Vector3());

        float x = position.x - cueOffset - cueBall.getRadius();
        model.transform.translate(x, position.y + yCoordinate, position.z);
    }


    public void toMousePosition(Vector3 mousePosition, Ball3D cueBall) {

        Vector3 ballPosition = cueBall.getCoordinates();

        Vector3 oldCuePosition = getCoordinates().sub(ballPosition).nor();
        toPosition(mousePosition, cueBall);
        Vector3 newCuePosition = getCoordinates().sub(ballPosition).nor();

        double newAngle = MathUtils.atan2(newCuePosition.z, newCuePosition.x);
        double oldAngle = MathUtils.atan2(oldCuePosition.z, oldCuePosition.x);

        model.transform.rotate(new Vector3(0,1, 0), (float)Math.toDegrees(oldAngle - newAngle));
    }

    void toPosition(Vector3 mousePosition, Ball3D cueBall){
        Vector3 cuePosition = getCoordinates();
        Vector3 ballPosition = cueBall.getCoordinates();

        Vector3 direction = new Vector3();
        direction.add(mousePosition).sub(ballPosition);
        direction.y = 0; // Set y direction 0 because we never move up
        direction.nor();

        float dist = cueBall.getRadius() + cueOffset;
        Vector3 onRadius = new Vector3(direction.x * dist, 0 , direction.z * dist);
        Vector3 diff = cuePosition.sub(ballPosition);
        Vector3 res = onRadius.sub(diff);
        model.transform.translate(res.x, 0, res.z);
    }
}
