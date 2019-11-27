package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.sem.pool.GameConstants;

import java.util.ArrayList;
import java.util.List;

public class SceneFactory {
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

        for (int i = 0; i < GameConstants.BALL_COUNT; ++i) {
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

        // Position pool balls in the right places on the board
        positionPoolBalls(poolBalls);

        // Create table
        Table3D table = tableFactory.createTable();

        // Create scene with the constructed objects
        return new Scene3D(environment, camera, poolBalls, table, modelBatch);
    }

    /**
     * Positions pool balls in the right setup for the break shot.
     */
    private void positionPoolBalls(List<Ball3D> poolBalls) {
        // Position cue ball to it's right position
        poolBalls.get(0).move(GameConstants.CUE_BALL_OFFSET);

        // Keep track of the current row of balls and the
        // current ball in the row
        int row = 1;
        int count = 0;

        // Calculate spacing between balls
        BoundingBox box = new BoundingBox();
        box = poolBalls.get(0).getModel().calculateBoundingBox(box);
        float radius = box.max.x - box.getCenterX(); // Taking x coordinate is enough for radius

        // 2r is the actual spacing between balls; But we use < 2 to make them more coupled
        float spacing = radius * 1.8f;

        // Iterate through all non-cue balls
        for (int i = 1; i < poolBalls.size(); ++i) {
            Ball3D ball = poolBalls.get(i);

            ball.move(getPyramidOffset(spacing, row, count));

            count++;

            if (count == row) {
                row++;
                count = 0;
            }

            Vector3 offset = GameConstants.BALL_OFFSET;
            poolBalls.get(i).move(offset);
        }
    }

    private Vector3 getPyramidOffset(float spacing, int row, int entry) {
        float xspacing = spacing * row;
        float zspacing = spacing * (entry - 0.5f * row);

        return new Vector3(xspacing, 0, zspacing);
    }
}
