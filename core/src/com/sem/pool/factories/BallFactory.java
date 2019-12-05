package com.sem.pool.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.sem.pool.game.GameConstants;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CollisionHandler;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.EightBall3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.List;

/**
 * Factory class which allows the instantiation
 * of Ball3D objects from the specified texture set.
 */
public class BallFactory extends Base3DFactory {
    // Fixed constant path to use for the ball model asset
    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.BALL;

    // Name of the ball model material (defined in model itself)
    protected static final String BALL_MATERIAL_NAME = "ball";

    private List<Texture> textures;

    /**
     * Creates a new Ball Factory instance with the specified
     * textures. The indices of the provided List directly
     * correspond to the Ball IDs.
     * E.g. 0 - cue ball, 1 - solid 1, 9 - striped 1, etc.
     *
     * @param textures  List of textures for the balls
     */
    public BallFactory(List<Texture> textures, AssetLoader assetLoader) {
        super(assetLoader);
        this.textures = textures;
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public void setTextures(List<Texture> textures) {
        this.textures = textures;
    }

    /**
     * Creates a 3D Pool Ball object instance with the specified id.
     * The appearance of the ball is set accordingly to the id and
     * the internal textures parameter of the BallFactory.
     * @param id  Id of the ball to instantiate
     * @return  New Ball3D object instance corresponding to the specified id
     */
    public Ball3D createBall(int id) {
        // we assert ID is a valid ID.
        assert (id >= GameConstants.CUEBALL_ID && id < GameConstants.BALL_COUNT);
        ModelInstance ballInstance = assetLoader.loadModel(MODEL_TYPE);
        // If textures List empty, do not change textures at all
        if (!textures.isEmpty()) {
            // Wrap the index around the textures length to avoid
            // index out of bounds exceptions (in case there are less textures)
            int index = id % textures.size();

            // Get the texture at index and create a new diffuse texture attribute
            // from the texture; This applies the texture to the material's
            // diffuse component directly so that the object's color is now the
            // texture instead.
            Texture texture = textures.get(index);
            TextureAttribute attribute = TextureAttribute.createDiffuse(texture);

            // For the created ball instance, get the material and set
            // the newly created texture attribute.
            ballInstance.getMaterial(BALL_MATERIAL_NAME).set(attribute);
        }
        return returnBall(id, ballInstance);
    }

    /**
     * Returns the proper type of ball given an id.
     * @param id id of the ball.
     * @param ballInstance model instance of the ball.
     * @return An instance of Ball3D.
     */
    private Ball3D returnBall(int id, ModelInstance ballInstance) {
        if (id == GameConstants.CUEBALL_ID) {
            Ball3D cueBall = new CueBall3D(id, ballInstance);
            setUpCollisionHandler(cueBall);
            return cueBall;
        } else if (id == GameConstants.EIGHTBALL_ID) {
            Ball3D eightBall = new EightBall3D(id, ballInstance);
            setUpCollisionHandler(eightBall);
            return eightBall;
        } else {
            Ball3D regularBall = new RegularBall3D(id, ballInstance);
            setUpCollisionHandler(regularBall);
            return regularBall;
        }
    }

    /**
     * Method called to set up the collision handler for a ball.
     * @param ball ball that needs collision handler.
     */
    private void setUpCollisionHandler(Ball3D ball) {
        // configuration for the collisions
        btDefaultCollisionConfiguration configuration = new  btDefaultCollisionConfiguration();
        // dispatcher for the collisions
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(configuration);
        // info regarding construction of collision algorithm
        btCollisionAlgorithmConstructionInfo constructionInfo =
                new btCollisionAlgorithmConstructionInfo();
        // info regarding dispatcher
        btDispatcherInfo dispatcherInfo = new btDispatcherInfo();

        // creation of collision handler
        CollisionHandler collisionHandler = new CollisionHandler(configuration, dispatcher,
                constructionInfo, dispatcherInfo);
        ball.setCollisionHandler(collisionHandler);
    }
}
