package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.sem.pool.factories.AssetLoader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for the SoundPlayer class.
 * Sounds were taken from https://freesound.org/home/.
 */
public class SoundPlayerTest {

    /**
     * Tests that a sound is played when the sound is not playing yet.
     */
    @Test
    void testPlaySound() {
        // Gdx.audio and Gdx.files are mocked to make sure the tests can run,
        // we also use mocks to control the behaviour and avoid loading files

        AssetLoader mockedLoader = Mockito.mock(AssetLoader.class);
        AssetManager mockedManager = Mockito.mock(AssetManager.class);
        Mockito.when(mockedLoader.getAssetManager()).thenReturn(mockedManager);
        Mockito.when(mockedManager.update()).thenReturn(true);
        Music mockedMusic = Mockito.mock(Music.class);
        Mockito.doNothing().when(mockedLoader).initializeAssets();
        Mockito.when(mockedLoader.getBallSound()).thenReturn(mockedMusic);
        Mockito.when(mockedLoader.getPotSound()).thenReturn(mockedMusic);
        Mockito.when(mockedLoader.getCueSound()).thenReturn(mockedMusic);
        Mockito.when(mockedLoader.getTableSound()).thenReturn(mockedMusic);


        SoundPlayer soundPlayer = new SoundPlayer(mockedLoader);
        // any play method would suffice as the Gdx.audio.newMusic always returns the same thing.
        soundPlayer.playBallCollisionSound();
        Mockito.verify(mockedMusic, Mockito.times(1)).play();
        soundPlayer.playTableCollisionSound();
        Mockito.verify(mockedMusic, Mockito.times(2)).play();
        soundPlayer.playPotSound();
        Mockito.verify(mockedMusic, Mockito.times(3)).play();
        soundPlayer.playCueSound();
        Mockito.verify(mockedMusic, Mockito.times(4)).play();
    }

}
