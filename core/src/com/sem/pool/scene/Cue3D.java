package com.sem.pool.scene;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.game.GameConstants;

/**
 * Class representing a 3D Cue.
 */
public class Cue3D {

    // The Y of the cue can't be 0 because it will end up in the bumpers.
    protected static final float Y_COORDINATE = 1f;

    protected static final String MATERIAL_NAME = "CueMaterial";

    private transient ModelInstance model;

    // Add blending attribute to hide the cue
    public transient BlendingAttribute blendingAttribute;
    private transient State state;

    // Used for the dragging animation and force calculation
    private transient Vector3 dragOriginCue;
    private transient Vector3 dragOriginMouse;
    private transient float currentForce;

    public enum State {
        Rotating,
        Dragging,
        Hidden
    }

    /**
     * Constructs a new 3D Cue instance.
     */
    public Cue3D(ModelInstance model) {
        this.model = model;

        this.state = State.Hidden;
        this.dragOriginCue = new Vector3();
        this.dragOriginMouse = new Vector3();
        this.currentForce = 0;
        blendingAttribute = new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        model.getMaterial(MATERIAL_NAME).set(blendingAttribute);
        blendingAttribute.opacity = 0;
    }

    public void setCurrentForce(float currentForce) {
        this.currentForce = currentForce;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ModelInstance getModel() {
        return model;
    }

    public void setDragOriginMouse(Vector3 dragOriginMouse) {
        this.dragOriginMouse = dragOriginMouse;
    }

    public void setDragOriginCue(Vector3 dragOriginCue) {
        this.dragOriginCue = dragOriginCue;
    }

    public BlendingAttribute getBlendingAttribute() {
        return blendingAttribute;
    }

    /**
     * Given the mouse position, determines the direction of the cue
     * shot for the cueball.
     *
     * @param mousePosition Position of the mouse as a 3D Vector
     * @param cueBall       Ball3D to get the position of the cueBall
     * @return Direction of the cue shot
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
     *
     * @param cueBall cue ball to determine cue position.
     */
    public void toBeginPosition(Ball3D cueBall) {

        Vector3 position = cueBall.getCoordinates();

        // Sets the cue left from the cue ball
        float x = position.x - GameConstants.CUE_OFFSET - cueBall.getRadius();
        model.transform.translate(x, Y_COORDINATE, position.z);
    }

    /**
     * Returns the current coordinates of the cue.
     *
     * @return The coordinates of the cue.
     */
    public Vector3 getCoordinates() {
        return this.model.transform.getTranslation(new Vector3());
    }

    /**
     * Sets the mouseposition to the left of the cueball
     * when the mouseposition and ballposition are the same.
     * @param mousePosition Vector3 mouse position
     * @param ballPosition Vector3 ball position
     */
    void verifyMousePosition(Vector3 mousePosition, Vector3 ballPosition) {
        // If the mouseposition and cue ball position are the same -> set the cue to the left.
        if (mousePosition.x == ballPosition.x && mousePosition.z == ballPosition.z) {
            mousePosition.x -= 1;
        }
    }

    /**
     * Set the cue to its position and rotation.
     *
     * @param mousePosition mouse coordinates
     * @param cueBall       cue ball
     */
    public void toPosition(Vector3 mousePosition, Ball3D cueBall) {
        Vector3 ballPosition = cueBall.getCoordinates();

        verifyMousePosition(mousePosition, ballPosition);

        // Get the mouse direction with respect to the ball
        Vector3 direction = new Vector3();
        direction.add(mousePosition).sub(ballPosition);

        // Set y direction 0 because we never move up
        direction.y = 0;
        direction.nor();

        // Positioning
        // Distance from the center of the cue ball
        float dist = cueBall.getRadius() + GameConstants.CUE_OFFSET;

        // Scale the direction with the radius of the circle where the cue needs to be
        Vector3 onRadius = new Vector3(direction.scl(dist));
        Vector3 res = onRadius.add(ballPosition);
        model.transform.setToTranslation(res.x, Y_COORDINATE, res.z);

        // Rotation
        rotateCue(cueBall);
    }

    /**
     * Hides the cue and set force parameter to default.
     */
    public void hideCue() {
        setState(State.Hidden);
        setCurrentForce(0);
        blendingAttribute.opacity = 0;
    }

    /**
     * Shows the cue.
     */
    public void showCue() {
        setState(State.Rotating);
        blendingAttribute.opacity = 1;
    }

    /**
     * Sets the cue to the rotating state
     * and stores the point of the click which
     * is needed for the drag calculation
     * @param mousePosition Vector3 mouse position
     */
    public void setToRotating(Vector3 mousePosition) {
        setState(Cue3D.State.Dragging);
        setDragOriginCue(getCoordinates());
        setDragOriginMouse(mousePosition);
    }

    /**
     * Sets the cue to the position in the drag phase.

     * @param mousePosition mouse coordinates
     * @param cueBall       cue ball
     */
    public void toDragPosition(Vector3 mousePosition, Ball3D cueBall) {

        // Get the direction of the cue drag
        Vector3 direction = new Vector3(dragOriginMouse).sub(cueBall.getCoordinates());

        // Normalize vector because it is the direction
        direction.y = 0;
        direction.nor();

        // TODO: Different calculation for the distance/force of the cue
        // TODO: Now it just takes the distance to the first left-clicked point
        
        // The distance from the current mouse position and the first left-click mouse position.
        // capMaxForce prevents cue from going over max force
        // Calculate force based on the ratio of the
        // distance and the max distance that is allowed
        float tempDistance = mousePosition.dst(dragOriginMouse);
        float distance = Math.min(GameConstants.MAX_DRAG_DISTANCE, tempDistance);
        float distanceRatio = distance / GameConstants.MAX_DRAG_DISTANCE;
        currentForce = distanceRatio * GameConstants.MAX_CUE_FORCE;

        // Scale the direction with the distance
        direction.scl(distance);

        // Add the direction to the cue position of the first left-click
        direction.add(dragOriginCue);

        // Do the translation
        model.transform.setToTranslation(direction.x, Y_COORDINATE, direction.z);

        // Calculate and set the rotation of the cue (setToTranslation resets the rotation matrix)
        rotateCue(cueBall);
    }

    /**
     * Rotates the cue so that it points towards the center of the cue ball.
     * @param cueBall cue ball
     */
    public void rotateCue(Ball3D cueBall) {
        // Calculate the direction of the cue
        Vector3 cuePositionOrigin = getCoordinates().sub(cueBall.getCoordinates());

        // Calculate the angle the cue has to rotate
        double angle = MathUtils.atan2(cuePositionOrigin.z, cuePositionOrigin.x * -1);
        model.transform.rotateRad(Vector3.Y, (float) angle);
    }

    /**
     * Shoots the cue-ball based on the cue-ball-position
     * and the mouse-position when the drag started.
     *
     * @param cueBall the cue ball
     */
    public void shoot(Ball3D cueBall) {
        // Calculates the direction
        Vector3 direction = getCueShotDirection(dragOriginMouse, cueBall);

        // Apply the force in the shoot direction
        cueBall.setDirection(direction);
        cueBall.setSpeed(currentForce);

        hideCue();
    }

}
