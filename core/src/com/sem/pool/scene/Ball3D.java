package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.Objects;

/**
 * Class representing a 3D Pool Ball while also
 * associating the specific Ball with a specified ID.
 */
public class Ball3D {
    private int id;
    private transient ModelInstance model;

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

    public Vector3 getCoordinates() {
        return this.model.transform.getTranslation(new Vector3());
    }

    public void move(Vector3 translation){ // might need deltaTime
        this.model.transform.translate(translation);
    }

    public void applyForce(float force, Vector3 direction){
        this.move(direction.scl(force));
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
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }


}
