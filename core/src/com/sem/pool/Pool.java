package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.sem.pool.scene.*;

import javax.swing.text.AttributeSet;
import java.util.ArrayList;

/**
 * Main Pool Game application class that handles
 * the 3D pool scene and all the interactions.
 * TODO: Split this off into smaller components?
 */
public class Pool extends ApplicationAdapter {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Scene3D scene;

    // State flag to keep track of whether asset loading
    // has finished.
    private transient boolean loaded;

    ModelInstance ground;
    ModelInstance ball;
    boolean collision;
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    btCollisionObject groundObject;
    btCollisionObject ballObject;
    btCollisionShape groundShape;
    btCollisionShape ballShape;

    @Override
    public void create() {
        initializeAssetLoader();

        // Initialize model batch for rendering
        modelBatch = new ModelBatch();

        // Initialize viewport to the relevant width & height
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        assetLoader.initializeModels();
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
            // TODO: Move this to it's own CameraFactory class (or separate method)
            // TODO: For now, this is only a placeholder to be able to minimally system test.
            Camera camera = new PerspectiveCamera(67,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.position.set(0f, 5f, 0f);
            camera.lookAt(0,0,0);
            camera.near = 1f;
            camera.far = 300f;

            ArrayList<Texture> ballTexures = new ArrayList<Texture>();
            BallFactory ballFactory = new BallFactory(ballTexures, assetLoader);


            Texture tableTexture = null;
            TableFactory tableFactory = new TableFactory(tableTexture, assetLoader);

            SceneFactory sceneFactory = new SceneFactory(tableFactory,
                    ballFactory, camera, modelBatch);
            // Instantiate the scene
            scene = sceneFactory.createScene();
            for (Ball3D ball: getScene().getPoolBalls()){
                ball.move(new Vector3(0,1f, 0));
            }
            // Update the camera of the scene to point to the right location
            scene.getCamera().update();

            Bullet.init();
            ballShape = new btSphereShape(0.5f);
            groundShape = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));
            collisionConfig = new btDefaultCollisionConfiguration();
            dispatcher = new btCollisionDispatcher(collisionConfig);
            ModelBuilder mb = new ModelBuilder();
            ground = new ModelInstance(mb.createBox(2.5f, 2.5f, 2.5f,
                    new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
            groundObject = new btCollisionObject();
            groundObject.setCollisionShape(groundShape);
            groundObject.setWorldTransform(ground.transform);

            ball = getScene().getPoolBalls().get(0).getModel();
            ballObject = new btCollisionObject();
            ballObject.setCollisionShape(ballShape);
            ballObject.setWorldTransform(ball.transform);
            // The assets of the game are now fully loaded
            groundObject.setWorldTransform(new Matrix4().translate(new Vector3(0,-0.613333f, 0)));
            loaded = true;
        }
    }

    /**
     * Renders the scene only if the scene has finished loading.
     */
    private void renderScene() {
        // Render the scene only if the game is loaded

        if (loaded) {
            scene.render();
            Ball3D cueBall = getScene().getPoolBalls().get(0);
            // Apply gravity on the ball
            float dt = Gdx.graphics.getDeltaTime();
            if (!checkCollision()){
                System.out.println("Moving downwards");
                cueBall.applyForce(9.81f/10 * dt, new Vector3(0,-1f, 0));
                ballObject.setWorldTransform(cueBall.getModel().transform);
            }
            else{
                System.out.println("Collision detected");
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                cueBall.move(new Vector3(1f,0,0).scl(dt));
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                cueBall.move(new Vector3(-1f,0,0).scl(dt));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)){
                cueBall.move(new Vector3(0f,0,-1f).scl(dt));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                cueBall.move(new Vector3(0,0,1f).scl(dt));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)){
                getScene().getCamera().translate(new Vector3(1f,0f,0f).scl(dt));
                getScene().getCamera().update();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)){
                getScene().getCamera().translate(new Vector3(-1f,0f,0f).scl(dt));
                getScene().getCamera().update();            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                getScene().getCamera().translate(new Vector3(0f,0f,-1f).scl(dt));
                getScene().getCamera().update();            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)){
                getScene().getCamera().translate(new Vector3(0f,0f,1f).scl(dt));
                getScene().getCamera().update();            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)){
                getScene().getCamera().rotate(Vector3.X, -60 * dt);
                getScene().getCamera().update();            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)){
                getScene().getCamera().rotate(Vector3.X, 60* dt);
                getScene().getCamera().update();            }
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                getScene().getCamera().translate(new Vector3(0f,-1f,0f).scl(dt));
                getScene().getCamera().update();            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                getScene().getCamera().translate(new Vector3(0f,1f,0f).scl(dt));
                getScene().getCamera().update();            }
            //System.out.println(cueBall.getCoordinates());
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
        // Initialize scene if uninitialized
        initializeScene();

        // Clear depth buffer & color buffer masks
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render the scene if initialized
        renderScene();
        //float[] coordinates = getScene().getPoolBalls().get(0).getModel().transform.getValues();
    }

    @Override
    public void dispose() {
        scene.dispose();
        assetLoader.dispose();
    }

    public boolean checkCollision(){
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(groundObject);

        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
        ci.dispose();
        co1.dispose();
        co0.dispose();

        return r;
    }
}