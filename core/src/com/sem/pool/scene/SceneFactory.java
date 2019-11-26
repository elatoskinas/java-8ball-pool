package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import java.util.ArrayList;
import java.util.List;

public class SceneFactory {
    // Fixed count of pool balls to create:
    // Cue ball + 8-ball + 7 striped + 7 solid balls
    private static final int BALL_COUNT = 16;

    // Factories used to create Tables & Pool Balls
    private transient TableFactory tableFactory;
    private transient BallFactory ballFactory;

    private transient Camera camera;
    private transient ModelBatch modelBatch;

    /**
     * Creates a new Scene Factory instance with the
     * specified parameters to be used for scene instantiation.
     * @param tableFactory  Table Factory to use for Table instantiation
     * @param ballFactory   Ball Factory to use for Pool Ball instantiation
     * @param camera        Camera to use for the scene
     * @param modelBatch    Model Batch to use for scene rendering
     */
    public SceneFactory(TableFactory tableFactory, BallFactory ballFactory,
                        Camera camera, ModelBatch modelBatch) {
        this.tableFactory = tableFactory;
        this.ballFactory = ballFactory;
        this.camera = camera;
        this.modelBatch = modelBatch;
    }

    public TableFactory getTableFactory() {
        return tableFactory;
    }

    public void setTableFactory(TableFactory tableFactory) {
        this.tableFactory = tableFactory;
    }

    public BallFactory getBallFactory() {
        return ballFactory;
    }

    public void setBallFactory(BallFactory ballFactory) {
        this.ballFactory = ballFactory;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Instantiates the 3D scene by setting up the environment, camera
     * and models. The method instantiates all the necessary models,
     * positions them in the necessary locations and sets the camera
     * in its right location.
     */
    // ballFactory variable gets tagged as a DU anomaly, even
    // though it is initialized and used to create the pool
    // balls properly. Seems like a false positive here.
    //@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Scene3D createScene() {
        // TODO: Move this to it's own Environment factory or method.
        // Instantiate environment for the Scene
        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -1f, 0f));

        // Create pool balls
        List<Ball3D> poolBalls = new ArrayList<>();

        for (int i = 0; i < BALL_COUNT; ++i) {
            Ball3D ball = ballFactory.createBall(i);
            poolBalls.add(ball);

            // TODO: Temporary code to randomly spread out the
            // TODO: Initialized balls. To be replaced with
            // TODO: proper positioning of the balls later on.
            // TODO: Here, we also move the cue ball further away
            // TODO: with the intention of easier integration testing if needed.
            /*float xtranslate = i * (float) Math.random() * 0.2f;
            float ztranslate = i * (float) Math.random() * 0.1f;

            if (i == 0) {
                xtranslate = -1f;
                ztranslate = 0f;
            }

            ball.getModel().transform.translate(xtranslate, 0, ztranslate);*/
        }

        // Create table
        Table3D table = tableFactory.createTable();

        // Create scene with the constructed objects
        return new Scene3D(environment, camera, poolBalls, table, modelBatch);
    }
}
