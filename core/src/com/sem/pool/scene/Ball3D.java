package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.sem.pool.game.GameConstants;

import java.util.Objects;

import static java.lang.Math.*;

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
     * @param deltaTime deltaTime, time between current and last frame.
     */
    public void move(float deltaTime) {
        setSpeed(getSpeed() - (deltaTime * GameConstants.DRAG_COEFFICIENT));
        if (getSpeed() <= GameConstants.MIN_SPEED) {
            setSpeed(0);
            setDirection(new Vector3());
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
            
            if(other.getDirection().equals(new Vector3(0, 0, 0))) {
                other.setDirection(directionToOther);
            }
            
            // Calculate phi
            
            double phi = acos(directionToOther.nor().x);
            
            // Calculate theta1 and theta2
            double theta1 = acos(this.getDirection().x);
            
            double theta2 = acos(other.getDirection().x);
            if(other.getDirection().z < 0) {
                theta2 = theta2 * -1;
            }
            // Whenever the z direction of the ball is negative, invert the angle, 
            // as the domain of the acos function does not cover negative angles
            if(directionToOther.nor().z < 0) {
                phi = phi * -1;
            }
            if(this.getDirection().z < 0) {
                theta1 = theta1 * -1;
            }
            if(this.getDirection().z < 0) {
                theta1 = theta1 * -1;
            }
            
            // Declaration of the speed of both balls before the collision for calculation purposes
            double v1 = this.getSpeed();
            double v2 = other.getSpeed();

            // Calculate and set the speed and direction of ball 1
            double v1x = v2*cos(theta2 - phi)*cos(phi) + v1*sin(theta1 - phi)*cos(phi + (PI/2));
            double v1z = v2*cos(theta2 - phi)*sin(phi) + v1*sin(theta1 - phi)*sin(phi + (PI/2));

            this.setSpeed((float) Math.sqrt(v1x*v1x + v1z*v1z));
            this.setDirection(new Vector3((float) v1x, 0, (float) v1z));

            // Calculate and set the speed and direction of ball 2
            double v2x = v1*cos(theta1 - phi)*cos(phi) + v2*sin(theta2 - phi)*cos(phi + (PI/2));
            double v2z = v1*cos(theta1 - phi)*sin(phi) + v2*sin(theta2 - phi)*sin(phi + (PI/2));

            other.setSpeed((float) Math.sqrt(v2x*v2x + v2z*v2z));
            other.setDirection(new Vector3((float) v2x, 0, (float) v2z));
            
            // Move both balls a little bit to combat clipping
            move(0.01f);
            other.move(0.01f);
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
