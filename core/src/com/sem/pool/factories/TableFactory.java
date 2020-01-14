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
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.sem.pool.scene.CollisionHandler;
import com.sem.pool.scene.HitBox;
import com.sem.pool.scene.Table3D;

import java.util.ArrayList;

/**
 * Factory class which allows the instantiation
 * of Table3D objects from the specified texture.
 */
public class TableFactory extends Base3DFactory {
    protected static final AssetLoader.ModelType MODEL_TYPE = AssetLoader.ModelType.TABLE;
    
    protected static final Vector3 POT_BOX_DIMENSION = new Vector3(0.175f,10f,0.175f);

    public static final ArrayList<Vector3> POT_LOCATIONS;

    static {
        POT_LOCATIONS = new ArrayList<>();
        POT_LOCATIONS.add(new Vector3(3.05f, 0, 1.45f));
        POT_LOCATIONS.add(new Vector3(-3.05f, 0, 1.45f));
        POT_LOCATIONS.add(new Vector3(3.05f, 0, -1.45f));
        POT_LOCATIONS.add(new Vector3(-3.05f, 0, 1.45f));
        POT_LOCATIONS.add(new Vector3(-0.0125f, 0, 1.525f));
        POT_LOCATIONS.add(new Vector3(-0.0125f, 0, -1.525f));
    }

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
    @Override
    public Table3D createObject() {
        ModelInstance boardInstance = assetLoader.loadModel(MODEL_TYPE);

        // TODO: Set texture accordingly
        Table3D table = new Table3D(boardInstance);

        setUpCollisionHandler(table);
        Table3D.potHitBoxes = new ArrayList<>();
        return table;
    }

    /**
     * Method called to set up the collision handler for a table.
     * @param table table that needs collision handler.
     */
    private void setUpCollisionHandler(Table3D table) {
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
        table.setCollisionHandler(collisionHandler);
    }

    /**
     * Sets up the bounding borders for the table by creating four HitBoxes objects to
     * create walls that keep the ball on the table.
     * These four hitBoxes are created by calling setUpBox which takes as arguments:
     * The dimension of the box, position of the box, collisionObject for the box,
     * and normal for the hit box since for the table we need a vector to
     * reflect off for collisions.
     * @param table The table object for which the bounding boxes are created.
     */
    protected void setBoundingBoxes(Table3D table) {
        // set up bounding borders
        setUpBox(new Vector3(10f, 10f, 0.1f), new Matrix4().trn(new Vector3(0,0,1.45f)),
                table, new btCollisionObject(), new Vector3(0,0,1));

        setUpBox(new Vector3(10f, 10f, 0.1f), new Matrix4().trn(new Vector3(0,0,-1.45f)),
                table, new btCollisionObject(), new Vector3(0,0, -1));

        setUpBox(new Vector3(.1f, 10f, 10f), new Matrix4().trn(new Vector3(3.05f,0,0)),
                table, new btCollisionObject(), new Vector3(1, 0, 0));

        setUpBox(new Vector3(.1f, 10f, 10f), new Matrix4().trn(new Vector3(-3.05f,0,0)),
                table, new btCollisionObject(), new Vector3(-1,0,0));
    }

    /**
     * Sets up a single bounding box for the table.
     * @param shape btCollisionShape for the box, should in this case always be a cube.
     * @param position position where the box will be placed.
     * @param table the table object for which the bounding box should be created.
     * @param btCollisionObject the collision object required to create a HitBox instance.
     * @param normal the normal vector of the bounding box that is to be created.
     */
    protected void setUpBox(Vector3 shape, Matrix4 position, Table3D table,
                         btCollisionObject btCollisionObject, Vector3 normal) {
        btCollisionShape btCollisionShape = new btBoxShape(shape);
        btCollisionObject.setCollisionShape(btCollisionShape);
        btCollisionObject.setWorldTransform(position);
        HitBox hitBox = new HitBox(btCollisionShape, btCollisionObject);
        hitBox.setNormal(normal);
        table.getHitBoxes().add(hitBox);
    }

    /**
     * Sets up the hit boxes for the potting areas of the table.
     * @param table The table object for which the pot hit boxes.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // Suppressed as PMD flags pot
    // as a UR anomaly / being undefined
    // Checking for UR anomalies has been removed in updated versions of PMD: https://pmd.github.io/2019/10/31/PMD-6.19.0/
    protected void setUpPotHitBoxes(Table3D table) {
        for (Vector3 position: TableFactory.POT_LOCATIONS) {
            setUpPotBox(new Matrix4().trn(position), table, new btCollisionObject());
        }
    }

    /**
     * Sets up a single pot box for the table.
     * All pot boxes have the same collisionShape but different locations.
     * There should be 6 pot boxes, 4 corners and 2 in the up and lower middle.
     * @param position position where the box will be placed.
     * @param table the table object for which the pot box should be created.
     * @param btCollisionObject the collision object required to create a HitBox instance.
     */
    protected void setUpPotBox(Matrix4 position, Table3D table,
                               btCollisionObject btCollisionObject) {
        btCollisionShape collisionShape = new btCylinderShape(POT_BOX_DIMENSION);
        btCollisionObject.setCollisionShape(collisionShape);
        btCollisionObject.setWorldTransform(position);
        HitBox hitBox = new HitBox(collisionShape, btCollisionObject);
        table.getPotHitBoxes().add(hitBox);
    }


}


