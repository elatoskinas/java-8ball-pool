package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Class used to play sounds
 * Later fields could be added for things such as volume.
 */
public class SoundPlayer {

    /**
     * Constructor for a SoundPlayer, with currently no fields.
     */
    public SoundPlayer() {

    }

    /**
     * Method used to play the sound of the passed Music object.
     * @param sound Music object which will be played if the sound is not already being played.
     */
    private void playSound(Music sound) {
        if (!sound.isPlaying()) {
            sound.play();
        }
    }

    /**
     * Method used to play the sound of a cue hitting the ball.
     */
    public void playCueSound() {
        if (Gdx.audio != null) {
            Music cueSound = Gdx.audio.newMusic(
                    Gdx.files.internal("sounds/cueshot.mp3"));
            playSound(cueSound);
        }
    }

    /**
     * Method used to play the sound of two balls colliding.
     */
    public void playBallCollisionSound() {
        if (Gdx.audio != null) {
            Music ballSound = Gdx.audio.newMusic(
                    Gdx.files.internal("sounds/ballandballcollision.mp3"));
            playSound(ballSound);
        }
    }

    /**
     * Method used to play the sound of a table hitting the ball.
     */
    public void playTableCollisionSound() {
        if (Gdx.audio != null) {
            Music ballSound = Gdx.audio.newMusic(
                    Gdx.files.internal("sounds/ballandtablecollision.mp3"));
            playSound(ballSound);
        }
    }

    /**
     * Method used to play the sound of the ball being potted.
     */
    public void playPotSound() {
        Music potSound = Gdx.audio.newMusic(
                Gdx.files.internal("sounds/ballpot.mp3"));
        playSound(potSound);
    }
}
