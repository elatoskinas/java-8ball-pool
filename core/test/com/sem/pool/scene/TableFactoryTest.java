package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TableFactoryTest {
    TableFactory factory;
    Texture texture;
    AssetLoader assetLoader;

    @BeforeEach
    public void setUp() {
        texture = Mockito.mock(Texture.class);
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new TableFactory(texture, assetLoader);
    }

    public TableFactory getFactory() {
        return factory;
    }

    public void setFactory(TableFactory factory) {
        this.factory = factory;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public void setAssetLoader(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    @Test
    public void testConstructor() {
        assertEquals(texture, factory.getTexture());
    }

    @Test
    public void testSetTexture() {
        Texture sampleTexture = Mockito.mock(Texture.class);

        factory.setTexture(sampleTexture);
        assertEquals(sampleTexture, factory.getTexture());
    }

    @Test
    public void testCreateBoard() {
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Mockito.when(assetLoader.loadModel(TableFactory.MODEL_TYPE)).thenReturn(model);

        Table3D board = factory.createBoard();

        Table3D expectedBoard = new Table3D(model);
        assertEquals(expectedBoard.getModel(), board.getModel());
    }
}