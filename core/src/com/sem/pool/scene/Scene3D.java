package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Scene representation of a single Pool game.
 */
public class Scene3D {
    // ModelBatch LibGDX dependency that allows rendering
    private final transient ModelBatch modelBatch;

    // Scene elements
    private final transient SceneElements sceneElements;
    private final transient List<ModelInstance> models;

    // Game elements
    private final transient GameElements gameElements;

    // Represents the first ball touched on the last
    // check of trigger collisions.
    private transient Ball3D firstTouched = new NullBall();

    /**
     * Creates an instance of a 3D Pool Game scene from the specified
     * parameters of the scene.
     *
     * @param batch         Model Batch to use for rendering
     * @param gameElements  Game element collection (Balls, Table and Cue)
     * @param sceneElements Scene element collection (Camera, Environment, Sound Player)
     */
    public Scene3D(ModelBatch batch, GameElements gameElements, SceneElements sceneElements) {
        this.gameElements = gameElements;
        this.sceneElements = sceneElements;
        this.modelBatch = batch;

        // For all the pool balls and the table, add the models
        // of the entities to a single List for rendering.
        this.models = new ArrayList<>();
        models.add(gameElements.getTable().getModel());
        models.add(gameElements.getCue().getModel());

        for (Ball3D ball : gameElements.getPoolBalls()) {
            models.add(ball.getModel());
            ball.setUpBoxes();
        }
    }

    /**
     * Renders the scene with the scene's models, environment
     * and camera. Should be called on every game loop iteration.
     */
    public void render() {
        modelBatch.begin(sceneElements.getCamera());
        modelBatch.render(models, sceneElements.getEnvironment());
        modelBatch.end();
    }

    /**
     * Disposes & completely cleans up the scene of models.
     * To be used when the lifecycle of the game making use
     * of the 3D scene ends.
     */
    public void dispose() {
        modelBatch.dispose();
        models.clear();
    }

    /**
     * Checks collisions between the balls and the board,
     * and handles the reactions of the collisions.
     * TODO: Refactor this to it's own class, preferably
     *       This method should probably not even belong in the Scene class.
     *
     * @return the List of balls that have been potted immediately
     *         after the collision, or an empty List if no Ball has been potted.
     */
    // Suppress false positive for Dataflow Anomalies caused by the
    // defined loop in the method.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public List<Ball3D> triggerCollisions() {
        List<Ball3D> poolBalls = gameElements.getPoolBalls();
        List<Ball3D> potted = new ArrayList<>();

        for (int i = 0; i < poolBalls.size(); i++) {
            Ball3D ball = poolBalls.get(i);

            // Check collisions between the board and
            // every ball in the scene
            if (gameElements.getTable().checkCollision(ball)) {
                sceneElements.getSoundPlayer().playTableCollisionSound();
            }

            // Check if ball is potted
            boolean potResult = gameElements.getTable().checkIfPot(ball);
            if (potResult) {
                sceneElements.getSoundPlayer().playPotSound();
                potted.add(ball);
            }

            for (int j = i + 1; j < poolBalls.size(); j++) {
                Ball3D other = poolBalls.get(j);
                boolean collided = ball.checkCollision(other);
                if (collided) {
                    sceneElements.getSoundPlayer().playBallCollisionSound();
                }
                updateFirstTouched(ball, other, collided);
            }
        }

        return potted;
    }

    /**
     * Get the unprojected mouseposition.
     *
     * @return Vector3 mouseposition
     */
    public Vector3 getUnprojectedMousePosition() {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        sceneElements.getCamera().unproject(mousePosition);
        return mousePosition;
    }

    /**
     * Returns the first ball touched by the Cue Ball
     * on previous call of trigger collisions.
     *
     * @return Ball object of first ball touched, or null if does not apply.
     */
    public Ball3D getFirstTouched() {
        return firstTouched;
    }

    /**
     * Clears the first ball that is tracked as touched by the Cue Ball.
     * To be called at the end of a turn.
     */
    public void clearFirstTouched() {
        firstTouched = new NullBall();
    }

    /**
     * Lets the player choose the location of the cue ball after it has been potted.
     * PMD errors are ignored, as this is a bug within PMD.
     * It gives an error of an undefined variable in the foreach,
     * but it's defined in the block.
     * @param input The input handler.
     * @return True iff the cue ball was placed correctly
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean placeCueBall(Input input) {
        CueBall3D ball = this.getCueBall();
        
        if (input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 mousePosition = this.getUnprojectedMousePosition();
            ball.getModel().transform
                    .setTranslation(new Vector3(mousePosition.x, 0.28f, mousePosition.z));
            ball.getHitBox().updateLocation(ball.getModel().transform);

            // Move the cue ball to the desired spot and check if it collides with any balls
            boolean doesCollide = existsCollidingBall(ball);

            // If the cue ball does not collide with any balls already on the table,
            // and it is within bounds of the table, return true
            if (!doesCollide && ball.checkWithinBounds()) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Checks whether there exists a ball that collides with the
     * indicated ball.
     * PMD errors are ignored, as this is a bug within PMD.
     * It gives an error of an undefined variable in the foreach,
     * but it's defined in the block.
     * @param ball  Ball to check for collision
     * @return  True if there is a ball that collides with the specified ball.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private boolean existsCollidingBall(Ball3D ball) {
        for (Ball3D other : this.gameElements.getPoolBalls()) {
            if (ball.equals(other)) {
                continue;
            }

            CollisionHandler handler = ball.getCollisionHandler();
            if (handler.checkHitBoxCollision(ball.getHitBox(), other.getHitBox())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Updates the first ball touched variable in the Scene
     * based on the balls that collided. Is only effective
     * when one of the balls is a Cue Ball and when the balls
     * collide. Otherwise, the method does nothing.
     *
     * @param ball1    First ball that collided
     * @param ball2    Second ball that collided
     * @param collided True if the two balls collided, and false otherwise
     */
    private void updateFirstTouched(Ball3D ball1, Ball3D ball2, boolean collided) {
        if (collided && firstTouched instanceof NullBall) {
            firstTouched = distinguishCueBall(ball1, ball2);
        }
    }

    /**
     * Helper method to distinguish the Cue Ball between the two balls.
     * Returns the ball that is not the Cue Ball, or null if both of
     * the balls are non-cue balls.
     * NOTE: An assumption is made that at least
     * one of the balls is NOT a cue ball!
     *
     * @param ball1 First ball to check
     * @param ball2 Second ball to check
     * @return Null if none of the balls is a cue ball, and
     *              the non-cue ball if one of them is.
     */
    private Ball3D distinguishCueBall(Ball3D ball1, Ball3D ball2) {
        if (ball1 instanceof CueBall3D) {
            return ball2;
        } else if (ball2 instanceof CueBall3D) {
            return ball1;
        } else {
            return new NullBall();
        }
    }

    public Environment getEnvironment() {
        return sceneElements.getEnvironment();
    }

    public Camera getCamera() {
        return sceneElements.getCamera();
    }

    public List<ModelInstance> getModels() {
        return models;
    }

    public List<Ball3D> getPoolBalls() {
        return gameElements.getPoolBalls();
    }

    public Table3D getTable() {
        return gameElements.getTable();
    }

    public Cue3D getCue() {
        return gameElements.getCue();
    }

    public SoundPlayer getSoundPlayer() {
        return sceneElements.getSoundPlayer();
    }

    public CueBall3D getCueBall() {
        return gameElements.getCueBall();
    }
}
