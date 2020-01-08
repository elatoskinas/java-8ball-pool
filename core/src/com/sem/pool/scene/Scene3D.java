package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
    private final transient Environment environment;
    private final transient Camera camera;
    private final transient List<ModelInstance> models;

    // Game elements
    private final transient List<Ball3D> poolBalls;
    private final transient Table3D table;
    private final transient Cue3D cue;

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
                   Table3D table, Cue3D cue, ModelBatch batch) {
        this.environment = environment;
        this.camera = camera;
        this.poolBalls = poolBalls;
        this.table = table;
        this.cue = cue;
        this.modelBatch = batch;

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
     * Returns the List of balls that have been potted immediately
     * after the collision, or an empty List if no Ball has been
     * potted.
     * TODO: Refactor this to it's own class, preferably
     *       This method should probably not even belong in the Scene class.
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
                if (Gdx.audio != null) {
                    Music tableSound = Gdx.audio.newMusic(
                            Gdx.files.internal("sounds/ballandtablecollision.mp3"));
                    ball.playCollisionSound(tableSound);
                }
            }

            // Check if ball is potted
            boolean potResult = table.checkIfPot(ball);
            if (potResult) {
                if (Gdx.audio != null) {
                    Music potSound = Gdx.audio.newMusic(
                            Gdx.files.internal("sounds/ballpot.mp3"));
                    ball.playCollisionSound(potSound);
                }
                potted.add(ball);
            }

            for (int j = i + 1; j < poolBalls.size(); j++) {
                Ball3D other = poolBalls.get(j);
                if (ball.checkCollision(other)) {
                    if (Gdx.audio != null) {
                        Music ballSound = Gdx.audio.newMusic(
                                Gdx.files.internal("sounds/ballandballcollision.mp3"));
                        ball.playCollisionSound(ballSound);
                    }
                }
            }
        }

        return potted;
    }

    /**
     * Get the unprojected mouseposition.
     * @return Vector3 mouseposition
     */
    public Vector3 getUnprojectedMousePosition() {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);
        return mousePosition;
    }


}
