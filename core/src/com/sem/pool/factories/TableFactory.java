package com.sem.pool.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.sem.pool.scene.CollisionHandler;
import com.sem.pool.scene.HitBox;
import com.sem.pool.scene.Table3D;

/**
 * Factory class which allows the instantiation
 * of Table3D objects from the specified texture.
 */
public class TableFactory extends Base3DFactory {
    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.TABLE;

    private Texture texture;

    /**
     * Creates a new Board Factory instance with the specified
     * texture.
     *
     * @param texture  Texture to use for the board
     */
    public TableFactory(Texture texture, AssetLoader assetLoader) {
        super(assetLoader);
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Creates a 3D Board object instance.
     * The appearance of the board is set accordingly to the
     * internal texture of the TableFactory class.
     * @return  New Table3D object instance corresponding to the specified id
     */
    public Table3D createTable() {
        ModelInstance boardInstance = assetLoader.loadModel(MODEL_TYPE);

        // TODO: Set texture accordingly
        Table3D table = new Table3D(boardInstance);

        btDefaultCollisionConfiguration configuration = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(configuration);
        btCollisionAlgorithmConstructionInfo constructionInfo = new btCollisionAlgorithmConstructionInfo();
        btDispatcherInfo dispatcherInfo = new btDispatcherInfo();
        CollisionHandler collisionHandler = new CollisionHandler(configuration, dispatcher, constructionInfo, dispatcherInfo);
        table.setCollisionHandler(collisionHandler);
        return table;
    }

    /**
     * Sets up the bounding borders for the table.
     */
    public void setBoundingBoxes(Table3D table) {

        // set up bounding borders
        setUpBox(new Vector3(10f, 10f, 0.1f), new Matrix4().translate(new Vector3(0,0,1.45f)), table, new btCollisionObject(), new Vector3(0,0,1));
        setUpBox(new Vector3(10f, 10f, 0.1f), new Matrix4().translate(new Vector3(0,0,-1.45f)), table, new btCollisionObject(), new Vector3(0,0, -1));
        setUpBox(new Vector3(.1f, 10f, 10f), new Matrix4().translate(new Vector3(3.05f,0,0)), table, new btCollisionObject(), new Vector3(1, 0, 0));
        setUpBox(new Vector3(.1f, 10f, 10f), new Matrix4().translate(new Vector3(-3.05f,0,0)), table, new btCollisionObject(), new Vector3(-1,0,0));
    }


    /**
     * Sets up a single box for the table.
     * @param shape btCollisionShape for the box.
     * @param position position of the box.
     */
    public void setUpBox(Vector3 shape, Matrix4 position, Table3D table, btCollisionObject btCollisionObject, Vector3 normal) {
        System.out.println("Setting up bounding box at position: \n " + position);
        btCollisionShape btCollisionShape = new btBoxShape(shape);
        btCollisionObject.setCollisionShape( btCollisionShape);
        btCollisionObject.setWorldTransform(position);
        HitBox hitBox = new HitBox(btCollisionShape, btCollisionObject);
        hitBox.setNormal(normal);
        table.getHitBoxes().add(hitBox);
    }
}
