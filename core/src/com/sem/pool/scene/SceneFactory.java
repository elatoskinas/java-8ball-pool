package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.GameConstants;

import java.util.ArrayList;
import java.util.List;

public class SceneFactory {

    // Factories used to create Tables & Pool Balls & Camera & Cue
    private transient TableFactory tableFactory;
    private transient BallFactory ballFactory;
    private transient CameraFactory cameraFactory;
    private transient CueFactory cueFactory;

    private transient ModelBatch modelBatch;

    // Initial offsets for the pool balls to set up for break shot
    private static final Vector3 BALL_OFFSET = new Vector3(1f, 0.28f, 0f);
    private static final Vector3 CUE_BALL_OFFSET = new Vector3(-1.75f, 0.28f, 0f);

    /**
     * Creates a new Scene Factory instance with the
     * specified parameters to be used for scene instantiation.
     * @param tableFactory  Table Factory to use for Table instantiation
     * @param ballFactory   Ball Factory to use for Pool Ball instantiation
     * @param cameraFactory  Camera Factory to use for Camera instantiation
     * @param cueFactory  Cue Factory to use for Cue instantiation
     * @param modelBatch    Model Batch to use for scene rendering
     */
    public SceneFactory(TableFactory tableFactory, BallFactory ballFactory,
                        CameraFactory cameraFactory, CueFactory cueFactory, ModelBatch modelBatch) {
        this.tableFactory = tableFactory;
        this.ballFactory = ballFactory;
        this.cameraFactory = cameraFactory;
        this.cueFactory = cueFactory;
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

    public CameraFactory getCameraFactory() {
        return cameraFactory;
    }

    public void setCameraFactory(CameraFactory cameraFactory) {
        this.cameraFactory = cameraFactory;
    }

    public CueFactory getCueFactory() {
        return cueFactory;
    }

    public void setCueFactory(CueFactory cueFactory) {
        this.cueFactory = cueFactory;
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
        }

        // Position pool balls in the right places on the board
        positionPoolBalls(poolBalls);

        // Create camera
        Camera camera = cameraFactory.createCamera();

        // Create table
        Table3D table = tableFactory.createTable();

        // Create cue
        Cue3D cue = cueFactory.createCue();

        // Set cue to cueBall position
        cue.toBeginPosition(poolBalls.get(0));

        // Create scene with the constructed objects
        return new Scene3D(environment, camera, poolBalls, table, cue, modelBatch);
    }

    /**
     * Positions pool balls in the right setup for the break shot.
     */
    // The method seems to produce quite a lot of false positives
    // for DU anomalies (for row and count), possibly due to the
    // introduced loop.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private void positionPoolBalls(List<Ball3D> poolBalls) {
        // Position cue ball to it's right position
        poolBalls.get(0).move(CUE_BALL_OFFSET);

        // Keep track of the current row of balls and the
        // current ball in the row
        int row = 1;
        int count = 0;
        
        // Calculate spacing between balls. The spacing should be 2r
        // (distance from the center of one ball to the other), but
        // we use a slightly smaller value (1.8) to make the balls
        // closer together to each other.
        // TODO: Set 1.8 as a final float in the class
        float radius = poolBalls.get(0).getRadius();
        float spacing = radius * 1.8f;

        // Iterate through all non-cue balls
        for (int i = 1; i < poolBalls.size(); ++i) {
            Ball3D ball = poolBalls.get(i);

            // Move the pool balls by the calculated pyramid
            // offset which is based on the current row
            // and element count in the row for all
            // the pool balls. Furthermore, move the ball
            // by the predetermined offset to position it
            // at one side of the board.
            ball.move(getPyramidOffset(spacing, row, count));
            ball.move(BALL_OFFSET);

            // Increase row elemet count
            count++;

            // Count is equal to row; Reset count,
            // increment row (move on to next row
            // of the pyramid)
            // TODO: We can (maybe) replace this with an iterator/generator design
            //       pattern for more cleaner code (?)
            if (count == row) {
                row++;
                count = 0;
            }
        }
    }

    private Vector3 getPyramidOffset(float spacing, int row, int entry) {
        float xspacing = spacing * row;
        float zspacing = spacing * (entry - 0.5f * row);

        return new Vector3(xspacing, 0, zspacing);
    }
}
