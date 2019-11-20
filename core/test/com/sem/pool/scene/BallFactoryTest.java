package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BallFactoryTest {
    BallFactory factory;
    List<Texture> textures;
    AssetLoader assetLoader;

    @BeforeEach
    public void setup() {
        textures = new ArrayList<Texture>();
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new BallFactory(textures, assetLoader);
    }

    @Test
    public void testConstructor() {
        assertEquals(textures, factory.getTextures());
    }

    @Test
    public void testCreateBall() {
        final int id = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Mockito.when(assetLoader.loadModel(BallFactory.MODEL_TYPE)).thenReturn(model);

        Ball3D ball = factory.createBall(id);

        Ball3D expectedBall = new Ball3D(id, model);
        assertEquals(expectedBall, ball);
    }

    @Test
    public void testSetTextures() {
        List<Texture> newTextures = new ArrayList<>();
        Texture sampleTexture = Mockito.mock(Texture.class);
        newTextures.add(sampleTexture);

        factory.setTextures(textures);

        assertEquals(newTextures, factory.getTextures());
    }
}