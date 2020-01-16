package com.sem.pool.scene;

import com.badlogic.gdx.audio.Music;
import com.sem.pool.factories.AssetLoader;

/**
 * Class used to play sounds.
 */
public class SoundPlayer {
    private transient AssetLoader assetLoader;
    private transient Music ballSound;
    private transient Music cueSound;
    private transient Music potSound;
    private transient Music tableSound;


    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    /**
     * Constructor for a SoundPlayer.
     */
    public SoundPlayer(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        assetLoader.initializeAssets();
        // load sound files.
        if (assetLoader.getAssetManager().update()) {
            potSound = assetLoader.getPotSound();
            cueSound = assetLoader.getCueSound();
            tableSound = assetLoader.getTableSound();
            ballSound = assetLoader.getBallSound();
        } else {
            assetLoader.getAssetManager().finishLoading();
            potSound = assetLoader.getPotSound();
            cueSound = assetLoader.getCueSound();
            tableSound = assetLoader.getTableSound();
            ballSound = assetLoader.getBallSound();
        }
    }

    /**
     * Method used to play the sound of the passed Music object.
     * @param sound Music object which will be played if the sound is not already being played.
     */
    private void playSound(Music sound) {
        if (sound != null) {
            sound.play();
        }
    }

    /**
     * Method used to play the sound of a cue hitting the ball.
     */
    public void playCueSound() {
        playSound(cueSound);
    }

    /**
     * Method used to play the sound of two balls colliding.
     */
    public void playBallCollisionSound() {
        playSound(ballSound);

    }

    /**
     * Method used to play the sound of a table hitting the ball.
     */
    public void playTableCollisionSound() {
        playSound(tableSound);
    }

    /**
     * Method used to play the sound of the ball being potted.
     */
    public void playPotSound() {
        playSound(potSound);
    }
}
