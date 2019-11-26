package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Mockito.when(assetLoader.loadModel(TableFactory.MODEL_TYPE)).thenReturn(model);

        Table3D board = factory.createTable();

        Table3D expectedBoard = new Table3D(model);
        assertEquals(expectedBoard.getModel(), board.getModel());
    }
}