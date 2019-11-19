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
    AssetManager assetManager;

    @BeforeEach
    public void setup() {
        textures = new ArrayList<Texture>();
        assetManager = Mockito.mock(AssetManager.class);
        factory = new BallFactory(textures, assetManager);
    }

    @Test
    public void testCreateBall() {
        final int id = 0;

        // Create spy instance of the Model to be able to make the asset manager
        // return it. To prevent null pointer exceptions in the internal code
        // of the AssetManager loading, we use a spy instead of a mock.
        final Model model = Mockito.spy(Model.class);
        Mockito.when(assetManager.get(BallFactory.MODEL_PATH, Model.class)).thenReturn(model);

        Ball3D ball = factory.createBall(id);

        Ball3D expectedBall = new Ball3D(id, model);
        assertEquals(expectedBall, ball);
    }
}