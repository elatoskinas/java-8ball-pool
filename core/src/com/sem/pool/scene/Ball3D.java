package com.sem.pool.scene;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

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
    // The ratio of the model to the hitbox's radius
    static final float hitBoxRatio = 0.95f;


    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }

    /**
     * Creates a default Ball object with no internal parameters
     * adjusted. To be used only in private/protected contexts
     * for further extensions.
     */
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
        btSphereShape ballShape = new btSphereShape(this.getRadius() * hitBoxRatio);
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
        // if we're outside of the bounds of the table, move back.
        if (!checkWithinBounds()) {
            translate(new Vector3(translation).scl(-1));
        }

    }

    /**
     * Method called to move the ball in a direction.
     * @param translation direction of movement.
     */
    public void translate(Vector3 translation) {
        // move the visual model of the ball, we update the value as well.
        this.model.transform = this.model.transform.trn(translation);
        // hit box needs to be moved too to make sure hit box
        // and visual model are at the same position
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

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Method that checks if this and another ball collide.
     * If they do, both ball change directions and speed.
     * If a ball with no speed or direction is hit, they get
     * a new one. This is to respond to the cue ball hitting a ball that was just placed.
     * @param other Other ball.
     * @return whether the ball collided with the other ball.
     */
    // PMD gives a few DD warnings because the values of variables such as phi
    // are sometimes changed several times (because of conditions) before being used.
    // If it is not all changed in one line PMD gives a DD warning.
    // However since sometimes the conditions don't apply we cannot do it in one line.
    // Thus we suppress the warning.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean checkCollision(Ball3D other) {
        // balls placed below the table (when potted) should not collide.
        if (this.getCoordinates().y < 0 || other.getCoordinates().y < 0) {
            return false;
        }
        if (getCollisionHandler().checkHitBoxCollision(getHitBox(), other.getHitBox())) {
            // Create vector from ball to other
            Vector3 directionToOther = new Vector3(other.getCoordinates())
                    .sub(new Vector3(getCoordinates()));

            // Calculate phi, the angle of the direction of collision.
            double phi = acos(directionToOther.nor().x);
            
            // Calculate theta1 and theta2, the angle of the respective ball's direction.
            double theta1 = acos(this.getDirection().x);
            
            double theta2 = acos(other.getDirection().x);
            if (other.getDirection().z < 0) {
                theta2 = theta2 * -1;
            }
            // Whenever the z direction of the ball is negative, invert the angle, 
            // as the domain of the acos function does not cover negative angles
            if (directionToOther.nor().z < 0) {
                phi = phi * -1;
            }
            if (this.getDirection().z < 0) {
                theta1 = theta1 * -1;
            }
            if (this.getDirection().z < 0) {
                theta1 = theta1 * -1;
            }
            
            // Declaration of the speed of both balls before the collision for calculation purposes
            double v1 = this.getSpeed();
            double v2 = other.getSpeed();

            // Calculate and set the speed and direction of ball 1
            // We do this using two methods now,
            // but keep the old code here in case something went wrong.
            double v1x = calculateVx(v1, v2, theta1, theta2, phi);
            //v2 * cos(theta2 - phi) * cos(phi) + v1 * sin(theta1 - phi)
            //* cos(phi + (PI / 2));
            double v1z = calculateVz(v1, v2, theta1, theta2, phi);
            // v2* cos(theta2 - phi) * sin(phi) + v1 * sin(theta1 - phi)
            //* sin(phi + (PI / 2));

            this.setSpeed((float) Math.sqrt(v1x * v1x + v1z * v1z));
            Vector3 newDirection = new Vector3(getDirection()).sub(new Vector3(directionToOther));
            setDirection(newDirection);

            // Calculate and set the speed and direction of ball 2
            double v2x = calculateVx(v2, v1, theta2, theta1, phi);
            //v1 * cos(theta1 - phi) * cos(phi) + v2 * sin(theta2 - phi)
            //* cos(phi + (PI / 2));
            double v2z = calculateVz(v2, v1, theta2, theta1, phi);
            //v1 * cos(theta1 - phi) * sin(phi) + v2 * sin(theta2 - phi)
            //* sin(phi + (PI / 2));

            other.setSpeed((float) Math.sqrt(v2x * v2x + v2z * v2z));
            other.setDirection(directionToOther);
            return true;
        }
        return false;
    }

    /**
     * Calculates the speed in the x direction.
     * @param v1 Our speed.
     * @param v2 Speed of other object.
     * @param theta1 Angle of our direction.
     * @param theta2 Angle of direction of other object.
     * @param phi Angle of collision between objects.
     * @return Speed in the x direction after collision.
     */
    private double calculateVx(double v1, double v2, double theta1, double theta2, double phi) {
        return v2 * cos(theta2 - phi) * cos(phi) + v1 * sin(theta1 - phi)
                * cos(phi + (PI / 2));
    }

    /**
     * Calculates the speed in the z direction.
     * @param v1 Our speed.
     * @param v2 Speed of other object.
     * @param theta1 Angle of our direction.
     * @param theta2 Angle of direction of other object.
     * @param phi Angle of collision between objects.
     * @return Speed in the z direction after collision.
     */
    private double calculateVz(double v1, double v2, double theta1, double theta2, double phi) {
        return v2 * cos(theta2 - phi) * sin(phi) + v1 * sin(theta1 - phi)
                * sin(phi + (PI / 2));
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

    /**
     * Returns whether the ball is within the bounds of the table.
     * @return whether the ball is within the bounds of the table.
     */
    public boolean checkWithinBounds() {
        return Math.abs(getCoordinates().x) < Table3D.xBound
                && Math.abs(getCoordinates().z) < Table3D.zBound;
    }


    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }
}
