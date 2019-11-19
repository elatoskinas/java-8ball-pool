package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
        final Model model = Mockito.mock(Model.class);

        Ball3D expectedBall = new Ball3D(id, model);
        Ball3D ball = factory.createBall(id);

        assertEquals(expectedBall, ball);
    }
}