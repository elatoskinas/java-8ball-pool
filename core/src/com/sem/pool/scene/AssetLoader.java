package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.sem.pool.GameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for loading assets to the scene.
 * Simplifies the usage of the AssetManager for loading scenes
 * by providing a simpler to use interface.
 */
public class AssetLoader {
    // Paths to models for loading
    protected static final String BALL_MODEL_PATH = "models/ball.obj";
    protected static final String TABLE_MODEL_PATH = "models/table.obj";
    protected static final String CUE_MODEL_PATH = "models/cue.obj";
    protected static final String BALL_TEXTURE_PATH = "models/Textures/ball%d.jpg";

    // Parameters for loading 3D models
    private transient ObjLoader.ObjLoaderParameters objectLoaderParameters;

    // Underlying AssetManager (LibGDX dependency) to use
    // for loading assets
    private transient AssetManager assetManager;

    /**
     * Enum to represent loadable model types.
     * Simplifies the specification of paths for models to load.
     */
    public enum ModelType {
        BALL(BALL_MODEL_PATH),
        TABLE(TABLE_MODEL_PATH),
        CUE(CUE_MODEL_PATH);

        private String path;

        private ModelType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * Creates a new Asset Loader from the specified Asset Manager
     * object instance.
     * @param assetManager  Asset Manager to use to load assets (LibGDX dependency)
     */
    public AssetLoader(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Initialize object loader parameters to flip the texture coordinates.
        // Otherwise, the Blender object textures will appear wrong due to
        // the textures being flipped by default in LibGDX.
        objectLoaderParameters = new ObjLoader.ObjLoaderParameters(true);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ObjLoader.ObjLoaderParameters getObjectLoaderParameters() {
        return objectLoaderParameters;
    }

    /**
     * Initializes all of the assets needed for a Pool game, including
     * models and textures.
     * To be called during the create() stage of the game lifecycle.
     */
    // type.getPath() in the loop seems to cause an UR anomaly.
    // Using a value other than the ModelType's getPath seems
    // to not cause the issue. Thus, we suppress it here.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void initializeAssets() {
        for (ModelType type : ModelType.values()) {
            assetManager.load(type.getPath(), Model.class, objectLoaderParameters);
        }

        for (int i = 0; i < GameConstants.BALL_COUNT; ++i) {
            String texturePath = getBallTexturePath(i);
            assetManager.load(texturePath, Texture.class);
        }
    }

    /**
     * Returns a List of Pool Ball textures, where each entry's
     * index in the List corresponds to the specific ball, i.e.:
     * 0 - cue ball
     * 1 - solid 1
     * 8 - 8-ball
     * 9 - striped 1
     * @return List of pool ball textures containing 16 texture entries
     */
    public List<Texture> getBallTextures() {
        List<Texture> ballTextures = new ArrayList<>();

        for (int i = 0; i < GameConstants.BALL_COUNT; ++i) {
            // Get the i-th ball texture from the asset manager
            String texturePath = getBallTexturePath(i);
            Texture ballTexture = assetManager.get(texturePath, Texture.class);

            ballTextures.add(ballTexture);
        }

        return ballTextures;
    }

    /**
     * Returns the texture path for the ball of the specified id.
     * @param id  Id of the ball
     * @return    Texture path (String) for the ball
     */
    private String getBallTexturePath(int id) {
        // Get the texture path for the i-th texture (replace %d placeholder with i)
        return String.format(BALL_TEXTURE_PATH, id);
    }

    /**
     * Creates a model of the specified type.
     * @param type  Type of the model to load
     * @return  new Model Instance corresponding to the specified type.
     */
    public ModelInstance loadModel(ModelType type) {
        Model model = assetManager.get(type.getPath(), Model.class);

        return new ModelInstance(model);
    }

    /**
     * Disposes the asset loader. To be used when the lifecycle of
     * an application using the asset loader ends.
     */
    public void dispose() {
        assetManager.dispose();
    }
}
