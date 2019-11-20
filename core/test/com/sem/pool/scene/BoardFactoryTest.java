package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BoardFactoryTest {
    BoardFactory factory;
    Texture texture;
    AssetLoader assetLoader;

    @BeforeEach
    public void setUp() {
        texture = Mockito.mock(Texture.class);
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new BoardFactory(texture, assetLoader);
    }

    public BoardFactory getFactory() {
        return factory;
    }

    public void setFactory(BoardFactory factory) {
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
}