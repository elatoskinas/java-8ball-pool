package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import java.util.Objects;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public class Ball3D {
    private int id;
    private transient ModelInstance model;
    private transient BoundingBox boundingBox;
    private transient HitBox hitBox;
    private transient boolean setUp = false;
    private transient Vector3 direction;
    private transient float speed;

    /**
     * Constructs a new 3D Pool Ball instance with
     * the specified id and model.
     * @param id  ID of the ball
     * @param model  Model object of the ball
     */
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public Ball3D(int id, ModelInstance model) {
        this.id = id;
        this.model = model;
        this.direction = new Vector3(0,0,0);
        boundingBox = new BoundingBox();
        boundingBox = model.calculateBoundingBox(boundingBox);
    }

    public boolean isSetUp() {
        return setUp;
    }

    /**
     * Sets up the bounding box and hitboxes after the game is loaded.
     * This should be called when a ball is loaded into the scene.
     */
    public void setUpBoxes() {
        btSphereShape ballShape = new btSphereShape(0.5f * this.getRadius());
        btCollisionObject ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(this.model.transform);
        hitBox = new HitBox(ballShape, ballObject);
        this.setUp = true;
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

    public HitBox getHitBox() {
        return hitBox;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction.nor();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Returns the current coordinates of the ball.
     * @return The coordinates of the ball.
     */
    public Vector3 getCoordinates() {
        return this.model.transform.getTranslation(new Vector3());
    }

    /**
     * Moves the ball in a direction with speed.
     */
    public void move() {
        Vector3 translation = new Vector3(getDirection()).scl(speed);
        translate(translation);
    }

    /**
     * Method called to move the ball in a direction.
     * @param translation direction of movement.
     */
    public void translate(Vector3 translation) {
        this.model.transform.translate(translation);
        if (setUp) {
            this.hitBox.getObject().setWorldTransform(this.model.transform);
        }
    }

    /**
     * Returns the radius of the 3D Ball as a scalar.
     * @return  Radius of the 3D ball
     */
    public float getRadius() {
        // Calculate the radius; One axis is enough to determine the radius,
        // as we assume we have a perfect sphere.
        return boundingBox.max.x - boundingBox.getCenterX();
    }

    /**
     * Returns whether another Object is equal to this ball.
     * @param other other Object.
     * @return whether they are equal.
     */
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


    /**
     * Pot method for a ball. Should probably be overwritten for every ball.
     */
    public void pot() {
        translate(getCoordinates().scl(-1));
        translate(new Vector3(0, -100, 0));
    }
}
