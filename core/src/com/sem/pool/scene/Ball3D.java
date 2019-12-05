package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Objects;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public abstract class Ball3D {
    private int id;
    private final transient ModelInstance model;
    private transient BoundingBox boundingBox;

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
     * Returns true if the ball is in motion, and false otherwise.
     * TODO: This is to be integrated in collisions
     * @return  True if the ball is in motion.
     */
    public boolean isInMotion() {
        return false;
    }

    /**
     * Translates the ball according to the provided vector.
     * @param translation The direction and distance wherein the ball should be moved.
     */
    public void move(Vector3 translation) {
        this.model.transform.translate(translation);
    }

    /**
     * Moves the ball based on it's current speed & direction.
     * TODO: This is to be integrated in collisions;
     */
    public void move() {
        // TODO: ...
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

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

}
