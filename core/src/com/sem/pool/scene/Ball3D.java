package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.sem.pool.game.GameConstants;

import java.util.Objects;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public abstract class Ball3D extends Object3D {
    private int id;
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

    protected Ball3D() {

    }

    /**
     * Constructs a new 3D Pool Ball instance with
     * the specified id and model.
     * @param id  ID of the ball
     * @param model  Model object of the ball
     */
    public Ball3D(int id, ModelInstance model) {
        super(model);
        this.id = id;
        this.direction = new Vector3(0,0,0);
        boundingBox = new BoundingBox();
        boundingBox = model.calculateBoundingBox(boundingBox);
    }

    /**
     * Sets up the bounding box and hit boxes after the game is loaded.
     * This should be called when a ball is loaded into the scene.
     */
    public void setUpBoxes() {
        btSphereShape ballShape = new btSphereShape(this.getRadius());
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
     * Returns true if the ball is in motion, and false otherwise.
     * @return  True if the ball is in motion.
     */
    public boolean isInMotion() {
        return speed != 0;
    }
    
    /**
     * Moves the ball with current direction and speed.
     * @param deltaTime deltaTime, time between current and last frame.
     */
    public void move(float deltaTime) {
        setSpeed(getSpeed() - (deltaTime * GameConstants.DRAG_COEFFICIENT));
        if (getSpeed() <= GameConstants.MIN_SPEED) {
            setSpeed(0);
        }
        Vector3 translation = new Vector3(getDirection()).scl(speed);
        translate(translation);

    }

    /**
     * Method called to move the ball in a direction.
     * @param translation direction of movement.
     */
    public void translate(Vector3 translation) {
        // move the visual model of the ball
        this.model.transform.translate(translation);
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

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    /**
     * Method that checks if this and another ball collide.
     * If they do, both ball change directions and speed.
     * If a ball with no speed or direction is hit, they get
     * a new one. This is to respond to the cue ball hitting a ball that was just placed.
     * @param other Other ball.
     * @return whether the ball collided with the other ball.
     */
    public boolean checkCollision(Ball3D other) {
        if (getCollisionHandler().checkHitBoxCollision(getHitBox(), other.getHitBox())) {
            // Create vector from ball to other
            Vector3 directionToOther = new Vector3(other.getCoordinates())
                    .sub(new Vector3(getCoordinates()));
            // Create vector from other to ball.
            Vector3 directionToMe = new Vector3(getCoordinates())
                    .sub(new Vector3(other.getCoordinates()));

            // set directions of balls to opposite of their direction to the other.
            setDirection(directionToOther.scl(-1));
            other.setDirection(directionToMe.scl(-1));

            // halve our speed on collision (implementation will be improved later)
            setSpeed(getSpeed() / 2);

            // if we hit a ball that is not moving or has no direction, give it speed/direction.
            if (other.getSpeed() <= 0) {
                other.setSpeed(getSpeed());
            } else { // else, give it more speed if the similar direction, slow down if not.
                other.setSpeed(other.getSpeed() - getDirection()
                        .dot(other.getDirection()) / 100);
            }
            if (other.getDirection().equals(new Vector3())) {
                other.setDirection(new Vector3(getDirection()));
            }
            return true;
        }
        return false;
    }

    /**
     * Pot method for a ball.
     * Could be overwritten by subclasses in order to specify behaviour.
     * Default behaviour is to move the ball far below the center of the table.
     */
    public void pot() {
        // move ball back to origin
        translate(getCoordinates().scl(-1));
        // set ball below the table.
        translate(new Vector3(0, -100, 0));
        setSpeed(0);
        setDirection(new Vector3());
    }
}
