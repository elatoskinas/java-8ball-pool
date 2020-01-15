package com.sem.pool.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.sem.pool.scene.Table3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


/**
 * Test class containing unit tests for the TableFactory class and
 * some integration testing to test integration between TableFactory and
 * Table3D classes.
 */
class TableFactoryTest {
    transient TableFactory factory;
    transient Texture texture;
    transient AssetLoader assetLoader;

    /**
     * Sets up the test fixture by creating mock
     * dependencies & instantiating a TableFactory with
     * the mocked dependencies.
     */
    @BeforeEach
    public void setUp() {
        texture = Mockito.mock(Texture.class);
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new TableFactory(texture, assetLoader);
    }

    /**
     * Test to verify that the dependencies are set correctly
     * upon instantiating a TableFactory object.
     */
    @Test
    public void testConstructor() {
        assertEquals(texture, factory.getTexture());
    }

    /**
     * Test to verify the setter of the Texture for the
     * TextureFactory.
     */
    @Test
    public void testSetTexture() {
        Texture sampleTexture = Mockito.mock(Texture.class);

        factory.setTexture(sampleTexture);
        assertEquals(sampleTexture, factory.getTexture());
    }

    /**
     * Test case to verify that creating a Table via the TableFactory
     * creates a Table with the appropriate table model.
     */
    @Test
    public void testCreateBoard() {
        Bullet.init();
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Mockito.when(assetLoader.loadModel(TableFactory.MODEL_TYPE)).thenReturn(model);

        Table3D board = factory.createObject();

        Table3D expectedBoard = new Table3D(model);
        assertEquals(expectedBoard.getModel(), board.getModel());
    }

    /**
     * Test the setUpBoxes method.
     */
    @Test
    public void testSetUpBoxes() {
        btCollisionObject mockedCollisionObject = Mockito.mock(btCollisionObject.class);
        Matrix4 mockedMatrix = Mockito.mock(Matrix4.class);
        Matrix4 matrixSpy = Mockito.spy(mockedMatrix);
        Mockito.when(matrixSpy.translate(Mockito.any())).thenReturn(null);
        factory.setUpBox(Mockito.mock(Vector3.class), mockedMatrix,
                factory.createObject(), mockedCollisionObject, new Vector3(0,0,1));
        Mockito.verify(mockedCollisionObject).setWorldTransform(Mockito.any());
    }
}
