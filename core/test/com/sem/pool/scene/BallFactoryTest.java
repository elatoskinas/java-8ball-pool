package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.utils.Array;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testCreateBall() {
        final int id = 0;
        final ModelInstance model = Mockito.spy(ModelInstance.class);

        Mockito.when(assetLoader.loadModel(BallFactory.MODEL_TYPE)).thenReturn(model);

        Ball3D ball = factory.createBall(id);

        Ball3D expectedBall = new Ball3D(id, model);
        assertEquals(expectedBall, ball);
    }
}