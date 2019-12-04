package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Class representing a 3D Cue.
 * TODO: Rotation of the cue.
 * TODO: Set position according to the force.
 */
public class Cue3D {

    // The distance between the cueBall and the tip of the cue.
    private static final float cueOffset = 0.05f;

    // The Y of the cue can't be 0 because it will end up in the bumpers.
    private static final float yCoordinate = 1f;
    private static final float forceCap = 1f;

    // Used for the dragging animation and force calculation
    private boolean dragging;
    private Vector3 shootDirection;
    private Vector3 dragOriginCue;
    private Vector3 dragOriginMouse;

    private transient ModelInstance model;

    /**
     * Constructs a new 3D Cue instance.
     */
    public Cue3D(ModelInstance model) {
        this.model = model;
        this.dragging = false;
        this.shootDirection = new Vector3();
        this.dragOriginCue = new Vector3();
        this.dragOriginMouse = new Vector3();
    }

    public ModelInstance getModel() {
        return model;
    }

    /**
     * Returns the current coordinates of the cue.
     * @return The coordinates of the cue.
     */
    public Vector3 getCoordinates() {
        return this.model.transform.getTranslation(new Vector3());
    }


    /**
     * Given the mouse position, determines the direction of the cue
     * shot for the cueball.
     *
     * @param mousePosition  Position of the mouse as a 3D Vector
     * @param cueBall  Ball3D to get the position of the cueBall
     * @return  Direction of the cue shot
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
     * @param cueBall cue ball to determine cue position.
     */
    public void toBeginPosition(Ball3D cueBall) {
        Vector3 position = cueBall.getModel().transform.getTranslation(new Vector3());

        // Sets the cue left from the cue ball
        float x = position.x - cueOffset - cueBall.getRadius();
        model.transform.translate(x,  yCoordinate, position.z);
    }


    /**
     * Set the cue to its position and rotation.
     * @param mousePosition mouse coordinates
     * @param cueBall cue ball
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
        float dist = cueBall.getRadius() + cueOffset;

        // Scale the direction with the radius of the circle where the cue needs to be
        Vector3 onRadius = new Vector3(direction.scl(dist));
        Vector3 res = onRadius.add(ballPosition);
        model.transform.setToTranslation(res.x, yCoordinate, res.z);

        // Rotation
        Vector3 newCuePosition = getCoordinates().sub(ballPosition);
        newCuePosition.y = 0;
        newCuePosition.nor();
        double newAngle = MathUtils.atan2(newCuePosition.z, newCuePosition.x * -1);
        model.transform.rotateRad(Vector3.Y, (float) newAngle);
    }

    /**
     * TODO: Move this to a input processor.
     * Process the input mouse input for the cue
     * @param scene the scene object, to get the camera and cueballs.
     */
    public void processInput(Scene3D scene) {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        scene.getCamera().unproject(mousePosition);
        Ball3D cueBall = scene.getPoolBalls().get(0);

        // Enter dragging
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !dragging) {
            dragging = true;
            shootDirection = getCueShotDirection(mousePosition, cueBall);
            dragOriginCue = getCoordinates();
            dragOriginMouse = mousePosition;
        }
        // Update drag position
        else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && dragging) {
            toDragPosition(mousePosition, cueBall);
        }
        // Left click released -> shoot
        else if (dragging) {
            dragging = false;
            shoot(mousePosition, cueBall);
        }
        // Not draging -> update position
        else {
            toPosition(mousePosition, cueBall);
        }
    }

    /**
     * Sets the cue to the position in the drag phase.
     * @param mousePosition mouse coordinates
     * @param cueBall cue ball
     */
    public void toDragPosition(Vector3 mousePosition, Ball3D cueBall) {
        
        // Get the direction of the cue drag
        Vector3 direction = new Vector3(dragOriginMouse).sub(dragOriginCue);

        // Normalize vector because it is the direction
        direction.y = 0;
        direction.nor();

        // The distance from the current mouse position and the first left-click mouse position.
        float distance = calculateDistanceToOrigin(new Vector3(mousePosition).sub(dragOriginMouse));

        // Cap the max distance that the cue can move away
        if (distance > forceCap) {
            distance = forceCap;
        }

        // Scale the direction with the distance
        direction.scl(distance);

        // Add the direction to the cue position of the first left-click
        direction.add(dragOriginCue);

        // Do the translation
        model.transform.setToTranslation(direction.x, yCoordinate, direction.z);

        // Calculate and set the rotation of the cue (setToTranslation resets the rotation matrix)
        Vector3 cuePosition = getCoordinates().sub(cueBall.getCoordinates()).nor();
        double angle = MathUtils.atan2(cuePosition.z, cuePosition.x * -1);
        model.transform.rotateRad(Vector3.Y, (float) angle);
    }

    /**
     * Calculates the distance to the origin.
     * @param vector The vector
     * @return float distance to origin
     */
    public float calculateDistanceToOrigin(Vector3 vector) {
        Vector3 v = new Vector3(vector);
        return (float) Math.sqrt(Math.pow(v.x,2) + Math.pow(v.z,2));
    }

    /**
     * Shoots the cueball based on the mouse-postion and the mouse-position when the drag started.
     * @param mousePosition the current mouse position
     * @param cueBall the cueball
     */
    public void shoot(Vector3 mousePosition, Ball3D cueBall) {
        // Calculates the force based on the distance
        float force = calculateDistanceToOrigin(new Vector3(mousePosition).sub(dragOriginMouse));

        // Caps the force
        if (force > forceCap) {
            force = forceCap;
        }

        // Apply the force in the shoot direction
        cueBall.applyForce(force, shootDirection);
    }

}
