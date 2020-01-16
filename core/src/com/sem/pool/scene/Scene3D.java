package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.game.GameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Scene representation of a single Pool game.
 */
public class Scene3D {
    // ModelBatch LibGDX dependency that allows rendering
    private final transient ModelBatch modelBatch;

    // Scene elements
    private final transient Environment environment;
    private final transient Camera camera;
    private final transient List<ModelInstance> models;

    // Game elements
    private final transient List<Ball3D> poolBalls;
    private final transient Table3D table;
    private final transient Cue3D cue;
    private transient SoundPlayer soundPlayer;

    // Represents the first ball touched on the last
    // check of trigger collisions.
    private transient Ball3D firstTouched = new NullBall();

    /**
     * Creates an instance of a 3D Pool Game scene from the specified
     * parameters of the scene.
     *
     * @param environment Environment settings of the scene (e.g. light)
     * @param camera      Camera used in the scene
     * @param poolBalls   List of pool balls part of the scene
     * @param table       The table to use for the scene
     * @param cue         The cue to use for the scene
     * @param batch       Model Batch to use for rendering
     */
    public Scene3D(Environment environment, Camera camera, List<Ball3D> poolBalls,
                   Table3D table, Cue3D cue, ModelBatch batch, SoundPlayer soundPlayer) {
        this.environment = environment;
        this.camera = camera;
        this.poolBalls = poolBalls;
        this.table = table;
        this.cue = cue;
        this.modelBatch = batch;
        this.soundPlayer = soundPlayer;
        // For all the pool balls and the table, add the models
        // of the entities to a single List for rendering.
        this.models = new ArrayList<>();
        models.add(table.getModel());
        models.add(cue.getModel());

        for (Ball3D ball : poolBalls) {
            models.add(ball.getModel());
            ball.setUpBoxes();
            // can be used to quickly simulate a ball moving and colliding
            // if (ball instanceof CueBall3D) {
            // ball.setDirection(new Vector3(1,0,0));
            // ball.setSpeed(0.2f);
            // }
        }
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Camera getCamera() {
        return camera;
    }

    public List<ModelInstance> getModels() {
        return models;
    }

    public List<Ball3D> getPoolBalls() {
        return poolBalls;
    }

    public Table3D getTable() {
        return table;
    }

    public Cue3D getCue() {
        return cue;
    }


    /**
     * Renders the scene with the scene's models, environment
     * and camera. Should be called on every game loop iteration.
     */
    public void render() {
        modelBatch.begin(camera);
        modelBatch.render(models, environment);
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
        List<Ball3D> potted = new ArrayList<>();

        for (int i = 0; i < poolBalls.size(); i++) {
            Ball3D ball = poolBalls.get(i);

            // Check collisions between the board and
            // every ball in the scene
            if (table.checkCollision(ball)) {
                soundPlayer.playTableCollisionSound();
            }

            // Check if ball is potted
            boolean potResult = table.checkIfPot(ball);
            if (potResult) {
                soundPlayer.playPotSound();
                potted.add(ball);
            }

            for (int j = i + 1; j < poolBalls.size(); j++) {
                Ball3D other = poolBalls.get(j);
                boolean collided = ball.checkCollision(other);
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
        camera.unproject(mousePosition);
        return mousePosition;
    }

    /**
     * Returns the cue-ball.
     *
     * @return CueBall3D cue-ball
     */
    public CueBall3D getCueBall() {
        return (CueBall3D) getPoolBalls().get(GameConstants.CUEBALL_ID);
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
     * Resets the cue ball to the default position, after being potted.
     * PMD errors are ignored, as this is a bug within PMD.
     * It gives an error of an undefined variable in the foreach,
     * but it's defined in the block.
     * @param ball The cue ball.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void recenterCueBall(CueBall3D ball) {
        float magnitude = 0f;

        // This will not result in an endless loop,
        // unless there is no possible location for the ball.
        // As this is not possible an endless loop also is not possible.
        while (true) {
            ball.getModel().transform.set(ball.getModel().transform.idt());

            float x = -1.75f  + ((float) Math.random() - 0.5f) * magnitude;
            float y = 0.28f;
            float z = ((float) Math.random() - 0.5f) * magnitude;
            ball.getModel().transform.setTranslation(new Vector3(x, y, z));
            ball.getHitBox().updateLocation(ball.getModel().transform);

            boolean doesCollide = false;

            for (Ball3D other : this.getPoolBalls()) {
                if (ball.equals(other)) {
                    continue;
                }

                CollisionHandler handler = ball.getCollisionHandler();
                if (handler.checkHitBoxCollision(ball.getHitBox(), other.getHitBox())) {
                    doesCollide = true;
                    break;
                }
            }

            if (!doesCollide) {
                break;
            }

            magnitude += 0.1;
        }
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
}
