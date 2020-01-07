package com.sem.pool.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.game.GameState;

/**
 * Class representing a 3D Cue.
 * TODO: Set position according to the force.
 */
public class Cue3D {

    // The distance between the cueBall and the tip of the cue.
    protected static final float CUE_OFFSET = 0.05f;

    // The Y of the cue can't be 0 because it will end up in the bumpers.
    protected static final float Y_COORDINATE = 1f;

    // The max force that can be applied
    private static final float FORCE_CAP = 0.3f;

    private transient ModelInstance model;
    private transient BlendingAttribute blendingAttribute;
    private transient State state;

    // Used for the dragging animation and force calculation
    private transient Vector3 dragOriginCue;
    private transient Vector3 dragOriginMouse;

    enum State {
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

        blendingAttribute = new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        model.getMaterial("CueMaterial").set(blendingAttribute);
        blendingAttribute.opacity = 0;
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
        float x = position.x - CUE_OFFSET - cueBall.getRadius();
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
     * Set the cue to its position and rotation.
     *
     * @param mousePosition mouse coordinates
     * @param cueBall       cue ball
     */
    public void toPosition(Vector3 mousePosition, Ball3D cueBall) {

        Vector3 ballPosition = cueBall.getCoordinates();

        // If the mouseposition and cue ball position are the same -> set the cue to the left.
        if (mousePosition.x == ballPosition.x && mousePosition.z == ballPosition.z) {
            mousePosition.x -= cueBall.getRadius();
        }

        // Get the mouse direction with respect to the ball
        Vector3 direction = new Vector3();
        direction.add(mousePosition).sub(ballPosition);

        // Set y direction 0 because we never move up
        direction.y = 0;
        direction.nor();

        // Positioning
        // Distance from the center of the cue ball
        float dist = cueBall.getRadius() + CUE_OFFSET;

        // Scale the direction with the radius of the circle where the cue needs to be
        Vector3 onRadius = new Vector3(direction.scl(dist));
        Vector3 res = onRadius.add(ballPosition);
        model.transform.setToTranslation(res.x, Y_COORDINATE, res.z);

        // Rotation
        rotateCue(cueBall);
    }

    /**
     * TODO: Move this to a input processor.
     * Process the input mouse input for the cue
     *
     * @param scene the scene object, to get the camera and cueballs.
     */
    public void processInput(Input input, Scene3D scene, GameState gameState) {
        Vector3 mousePosition = scene.getUnprojectedMousePosition();
        Ball3D cueBall = scene.getPoolBalls().get(0);

        if (state == State.Hidden) {
            state = State.Rotating;
            blendingAttribute.opacity = 1;
        }

        // Enter dragging
        if (input.isButtonPressed(Input.Buttons.LEFT)) {
            // Enter dragging
            if (state != State.Dragging) {
                state = State.Dragging;
                setDragOriginCue(getCoordinates());
                setDragOriginMouse(mousePosition);
            }
            toDragPosition(mousePosition, cueBall);

        } else if (state == State.Dragging) {
            gameState.setInMotion();
            shoot(mousePosition, cueBall);
            hideCue();

        } else {
            toPosition(mousePosition, cueBall);
        }
    }

    /*
     * Hides the cue
     */
    public void hideCue() {
        state = State.Hidden;
        blendingAttribute.opacity = 0;
    }

    /**
     * Sets the cue to the position in the drag phase.
     *
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
        float distance = mousePosition.dst(dragOriginMouse);

        // Maximum distance that the cue can be dragged
        if (distance > FORCE_CAP) {
            distance = FORCE_CAP;
        }

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
     *
     * @param cueBall cue ball
     */
    public void rotateCue(Ball3D cueBall) {
        Vector3 cuePosition = getCoordinates().sub(cueBall.getCoordinates());
        double angle = MathUtils.atan2(cuePosition.z, cuePosition.x * -1);
        model.transform.rotateRad(Vector3.Y, (float) angle);
    }

    /**
     * Shoots the cueball based on the mouse-postion and the mouse-position when the drag started.
     *
     * @param mousePosition the current mouse position
     * @param cueBall       the cueball
     */
    public void shoot(Vector3 mousePosition, Ball3D cueBall) {
        // Calculates the force based on the distance
        float force = mousePosition.dst(dragOriginMouse);
        Vector3 direction = getCueShotDirection(dragOriginMouse, cueBall);

        // Caps the force
        if (force > FORCE_CAP) {
            force = FORCE_CAP;
        }

        System.out.println(direction);

        // Apply the force in the shoot direction
        cueBall.setDirection(direction);
        cueBall.setSpeed(force);
    }
}
