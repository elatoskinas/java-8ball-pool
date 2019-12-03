package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Objects;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public class Ball3D {
    private int id;
    private transient ModelInstance model;
    private transient BoundingBox boundingBox;
    private HitBox hitBox;

    /**
     * Constructs a new 3D Pool Ball instance with
     * the specified id and model.
     * @param id  ID of the ball
     * @param model  Model object of the ball
     */
    public Ball3D(int id, ModelInstance model) {
        this.id = id;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelInstance getModel() {
        return model;
    }

    /**
     * Returns the current coordinates of the ball.
     * @return The coordinates of the ball.
     */
    public Vector3 getCoordinates() {
        return this.model.transform.getTranslation(new Vector3());
    }

    /**
     * Translates the ball according to the provided vector.
     * @param translation The direction and distance wherein the ball should be moved.
     */
    public void move(Vector3 translation) {
        this.model.transform.translate(translation);
    }

    /**
     * Applies the provided directional force to the ball, resulting in movement.
     * @param force Scalar by which the direction vector will be multiplied.
     * @param direction The direction of the force that is to be applied to the ball.
     */
    public void applyForce(float force, Vector3 direction) {
        this.move(direction.scl(force));
    }

    /**
     * Returns the radius of the 3D Ball as a scalar.
     * @return  Radius of the 3D ball
     */
    public float getRadius() {
        // Construct a bounding box around the model (if
        // the box has not yet been created)
        if (boundingBox == null) {
            boundingBox = new BoundingBox();
            boundingBox = model.calculateBoundingBox(boundingBox);
        }

        // Calculate the radius; One axis is enough to determine the radius,
        // as we assume we have a perfect sphere.
        return boundingBox.max.x - boundingBox.getCenterX();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Ball3D) {
            Ball3D otherBall = (Ball3D) other;

            return this.id == otherBall.id
                    && this.model.equals(otherBall.model);
        }

        return false;
    }

    /**
     * Given the mouse position, determines the direction of the cue
     * shot for the current ball.
     * TODO: This should be in the CueBall extended class, as it is not
     * TODO: relevant for other pool balls.
     *
     * @param mousePosition  Position of the mouse as a 3D Vector
     * @return  Direction of the cue shot
     */
    public Vector3 getCueShotDirection(Vector3 mousePosition) {
        Vector3 ballPosition = getCoordinates();

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

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

}
