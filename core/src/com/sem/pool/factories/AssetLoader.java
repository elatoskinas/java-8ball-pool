package com.sem.pool.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.sem.pool.game.GameConstants;

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
    protected static final String CUE_TEXTURE_PATH = "models/Textures/cue.jpg";
    protected static final String TABLE_TEXTURE_PATH = "models/Textures/table.jpg";

    protected static final String BALL_SOUND_PATH = "sounds/ballandballcollision.mp3";
    protected static final String TABLE_SOUND_PATH = "sounds/ballandtablecollision.mp3";
    protected static final String CUE_SOUND_PATH = "sounds/cueshot.mp3";
    protected static final String POT_SOUND_PATH = "sounds/ballpot.mp3";


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

        private final String path;

        ModelType(String path) {
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
        assetManager.load(POT_SOUND_PATH, Music.class);
        assetManager.load(BALL_SOUND_PATH, Music.class);
        assetManager.load(TABLE_SOUND_PATH, Music.class);
        assetManager.load(CUE_SOUND_PATH, Music.class);
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
     * Returns the currently loaded cue texture, if present, and null otherwise.
     * @return  Cue texture object
     */
    public Texture getCueTexture() {
        // TODO: Currently, this method is not of much use, as textures are not used for cue/table.
        //       However, this was created with the idea in mind that the Game could be extended
        //       to allow different cue/table textures in the future, making this method very easily
        //       extendible and applicable. Moreover, we can make it so that the AssetLoader handles
        //       holding the textures with the current approach.
        return loadTexture(CUE_TEXTURE_PATH);
    }

    /**
     * Returns the currently loaded table texture, if present, and null otherwise.
     * @return  Board texture object
     */
    public Texture getTableTexture() {
        // TODO: @see getCueTexture() TODO. Same situation.
        return loadTexture(TABLE_TEXTURE_PATH);
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
     * Loads the texture from the specified path.
     * @param path  Path of texture to load from
     * @return      Loaded texture if present, and null otherwise.
     */
    private Texture loadTexture(String path) {
        if (assetManager.contains(path)) {
            return assetManager.get(path, Texture.class);
        }

        return null;
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

    /**
     * Returns the sound for a ball and ball collision.
     * @return the sound for a ball and ball collision.
     */
    public Music getBallSound() {
        Music sound = assetManager.get(BALL_SOUND_PATH, Music.class);
        return sound;
    }

    /**
     * Returns the sound for a ball and table collision.
     * @return the sound for a ball and table collision.
     */
    public Music getTableSound() {
        Music sound = assetManager.get(TABLE_SOUND_PATH, Music.class);
        return sound;
    }

    /**
     * Returns the sound for a ball and cue collision.
     * @return the sound for a ball and cue collision.
     */
    public Music getCueSound() {
        Music sound = assetManager.get(CUE_SOUND_PATH, Music.class);
        return sound;
    }

    /**
     * Returns the sound for a ball that is potted.
     * @return the sound for a ball that is potted.
     */
    public Music getPotSound() {
        Music sound = assetManager.get(POT_SOUND_PATH, Music.class);
        return sound;
    }
}
