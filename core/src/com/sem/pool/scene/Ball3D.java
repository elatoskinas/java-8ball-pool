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
public abstract class Ball3D {
    private int id;
    private transient ModelInstance model;
    private transient BoundingBox boundingBox;
    private transient HitBox hitBox;
    private transient Vector3 direction;
    private transient float speed;
    private transient CollisionHandler collisionHandler;

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }

    /**
     * Constructs a new 3D Pool Ball instance with
     * the specified id and model.
     * @param id  ID of the ball
     * @param model  Model object of the ball
     */
    public Ball3D(int id, ModelInstance model) {
        this.id = id;
        this.model = model;
        this.direction = new Vector3(0,0,0);
        boundingBox = new BoundingBox();
        boundingBox = model.calculateBoundingBox(boundingBox);
    }

    /**
     * Sets up the bounding box and hit boxes after the game is loaded.
     * This should be called when a ball is loaded into the scene.
     */
    public void setUpBoxes() {
        btSphereShape ballShape = new btSphereShape(0.5f * this.getRadius());
        btCollisionObject ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(this.model.transform);
        hitBox = new HitBox(ballShape, ballObject);
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
     * Returns true if the ball is in motion, and false otherwise.
     * @return  True if the ball is in motion.
     */
    public boolean isInMotion() {
        return speed != 0;
    }
    
    /**
     * Moves the ball with current direction and speed.
     */
    public void move() {
        Vector3 translation = new Vector3(getDirection()).scl(speed);
//        if (speed > 0) {
//            System.out.println(translation);
//        }
        translate(translation);
    }

    /**
     * Method called to move the ball in a direction.
     * @param translation direction of movement.
     */
    public void translate(Vector3 translation) {
        // move the visual model of the ball
        this.model.transform.translate(translation);
        //this.model.transform.translate(new Vector3(0.000000001f,0,0));
        // hit box needs to be moved too to make sure hit box
        // and visual model are at the same position
        // TODO: refactor code to fix this issue with tests
        if (hitBox != null) {
            this.hitBox.updateLocation(this.model.transform);
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

    public boolean checkCollision(Ball3D other) {
        if (getCollisionHandler().checkHitBoxCollision(getHitBox(), other.getHitBox())) {
            System.out.println("Collision with ball: " + getId() + " and " + other.getId());

            Vector3 ourDirection = new Vector3(getDirection()).nor();
            Vector3 directionToOther = new Vector3(getCoordinates()).sub(other.getCoordinates()).nor();
//
            Vector3 otherDirection = new Vector3(getDirection()).nor();
            Vector3 directionToMe = new Vector3(other.getCoordinates().sub(other.getCoordinates())).nor();
//
            Vector3 newDirection = collisionHandler.reflectVector(directionToOther, ourDirection);
            Vector3 otherNewDirection = collisionHandler.reflectVector(directionToMe, otherDirection);

//            setDirection(collisionHandler.reflectVector(directionToOther, ourDirection));
//            other.setDirection(collisionHandler.reflectVector(directionToMe, otherDirection));
            setDirection(ourDirection.scl(-1));
            other.setDirection(otherDirection.scl(-1));

//            translate(new Vector3(newDirection).scl(0.1f));
//            other.translate(new Vector3(otherNewDirection).scl(0.1f));

            if (other.getSpeed() <= 0) {
                other.setSpeed(getSpeed() * 0.5f);
            }
            if (other.getDirection().equals(new Vector3())){
                other.setDirection(new Vector3(getDirection()));
            }
            return true;
        }
        return false;
    }

}
