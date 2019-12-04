package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.sem.pool.factories.AssetLoader;
import com.sem.pool.factories.BallFactory;

import com.sem.pool.factories.CameraFactory;
import com.sem.pool.factories.CueFactory;
import com.sem.pool.factories.SceneFactory;
import com.sem.pool.factories.TableFactory;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Scene3D;

import java.util.List;

/**
 * Main Pool Game application class that handles
 * the 3D pool scene and all the interactions.
 * TODO: Split this off into smaller components?
 */
public class Pool extends ApplicationAdapter {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;
    static final Vector3 cameraPosition = new Vector3(0f, 100f, 0f);

    // State flag to keep track of whether asset loading
    // has finished.
    public static transient boolean loaded;
    public transient int speed = 1;

    boolean start = false;

    @Override
    public void create() {
        initializeAssetLoader();

        // Initialize model batch for rendering
        modelBatch = new ModelBatch();

        // Initialize viewport to the relevant width & height
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize the Bullet wrapper used for collisions
        Bullet.init();
    }

    /**
     * Initializes the asset loader & loads the necessary
     * models into the asset loader.
     */
    private void initializeAssetLoader() {
        // Initialize objects to handle asset loading
        AssetManager manager = new AssetManager();
        assetLoader = new AssetLoader(manager);

        // Initialize models by queueing them for loading
        assetLoader.initializeAssets();
    }

    /**
     * Initializes the scene if loading of assets has
     * been completed and the scene is not yet initialized.
     */
    private void initializeScene() {
        // If the game has not yet been loaded, and an
        // assetLoader update event is received in current iteration,
        // then load the game.
        if (!loaded && assetLoader.getAssetManager().update()) {

            float width = Gdx.graphics.getWidth();
            float height = Gdx.graphics.getHeight();
            CameraFactory cameraFactory = new CameraFactory(width, height, cameraPosition);

            Texture cueTexture = null;
            CueFactory cueFactory = new CueFactory(cueTexture, assetLoader);

            List<Texture> ballTextures = assetLoader.getBallTextures();
            BallFactory ballFactory = new BallFactory(ballTextures, assetLoader);

            Texture tableTexture = null;
            TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);

            SceneFactory sceneFactory = new SceneFactory(tableFactory,
                    ballFactory, cameraFactory, cueFactory, modelBatch);

            // Instantiate the scene
            scene = sceneFactory.createScene();

            // Update the camera of the scene to point to the right location
            scene.getCamera().update();

            // The assets of the game are now fully loaded
            loaded = true;
        }
    }

    /**
     * Method to move the camera using the keyboard.
     */
    public void moveCamera() {
        // CAMERA MOVEMENT
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            getScene().getCamera().translate(new Vector3(1f, 0f, 0f)
                    .scl(Gdx.graphics.getDeltaTime()));
            getScene().getCamera().update();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            getScene().getCamera().translate(new Vector3(-1f, 0f, 0f)
                    .scl(Gdx.graphics.getDeltaTime()));
            getScene().getCamera().update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            getScene().getCamera().translate(new Vector3(0f, 0f, -1f)
                    .scl(Gdx.graphics.getDeltaTime()));
            getScene().getCamera().update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            getScene().getCamera().translate(new Vector3(0f, 0f, 1f)
                    .scl(Gdx.graphics.getDeltaTime()));
            getScene().getCamera().update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            getScene().getCamera().rotate(Vector3.X, -60
                    * Gdx.graphics.getDeltaTime());
            getScene().getCamera().update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) {
            getScene().getCamera().rotate(Vector3.X, 60
                    * Gdx.graphics.getDeltaTime());
            getScene().getCamera().update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            getScene().getCamera().translate(new Vector3(0f, -1f, 0f)
                    .scl(Gdx.graphics.getDeltaTime()));
            getScene().getCamera().update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            getScene().getCamera().translate(new Vector3(0f, 1f, 0f)
                    .scl(Gdx.graphics.getDeltaTime()));
            getScene().getCamera().update();
        }
        // END CAMERA MOVEMENT
    }


    /**
     * Method to move the ball using the keyboard.
     */
    // change move to translate
//    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // might be unsuppressed later.
//    public void moveBall() {
//        Ball3D cueBall = scene.getPoolBalls().get(0);
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            cueBall.move(new Vector3(1f, 0, 0).scl(speed
//                    * Gdx.graphics.getDeltaTime()));
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            cueBall.move(new Vector3(-1f, 0, 0).scl(speed
//                    * Gdx.graphics.getDeltaTime()));
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            cueBall.move(new Vector3(0f, 0, -1f).scl(speed
//                    * Gdx.graphics.getDeltaTime()));
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            cueBall.move(new Vector3(0, 0, 1f).scl(speed
//                    * Gdx.graphics.getDeltaTime()));
//        }
//    }

    /**
     * Renders the scene only if the scene has finished loading.
     */
    private void renderScene() {
        // Render the scene only if the game is loaded
        if (loaded) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                start = true;
            }
            scene.render();
            Ball3D cueBall = scene.getPoolBalls().get(0);
                if (cueBall.getSpeed() > 0 && start) {
                    getScene().getTable().checkCollision(cueBall);
                    System.out.println(cueBall.getDirection());
                    cueBall.move();
                }
            // so it doesn't collide with table.
            // TODO: Temporary code below that gets the cue shot direction
            // TODO: relative to the mouse position.
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            //System.out.println(scene.getCamera().unproject(mousePosition));
            //Vector3 shotDirection = getScene().getPoolBalls().
            //get(0).getCueShotDirection(mousePosition);*/

        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Scene3D getScene() {
        return scene;
    }

    @Override
    public void render() {
        // Initialize scene if uninitialized]
        initializeScene();

        // Clear depth buffer & color buffer masks
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render the scene if initialized
        renderScene();
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();

    }
}