package com.sem.pool.scene;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
    void testPlaySoundIsNotPlaying() {
        // Gdx.audio and Gdx.files are mocked to make sure the tests can run,
        // we also use mocks to control the behaviour and avoid loading files
        Gdx.audio = Mockito.mock(Audio.class);
        Gdx.files = Mockito.mock(Files.class);
        SoundPlayer soundPlayer = new SoundPlayer();
        Music mockedMusic = Mockito.mock(Music.class);
        Mockito.when(Gdx.audio.newMusic(Mockito.any())).thenReturn(mockedMusic);
        // any play method would suffice as the Gdx.audio.newMusic always returns the same thing.
        soundPlayer.playBallCollisionSound();
        Mockito.verify(mockedMusic, Mockito.times(1)).play();
    }

    /**
     * Tests that a sound is not played when the sound is already playing.
     */
    @Test
    void testPlaySoundIsPlaying() {
        // Gdx.audio and Gdx.files are mocked to make sure the tests can run,
        // we also use mocks to control the behaviour and avoid loading files
        Gdx.audio = Mockito.mock(Audio.class);
        Gdx.files = Mockito.mock(Files.class);

        SoundPlayer soundPlayer = new SoundPlayer();

        Music mockedMusic = Mockito.mock(Music.class);
        Mockito.when(mockedMusic.isPlaying()).thenReturn(true);

        Mockito.when(Gdx.audio.newMusic(Mockito.any())).thenReturn(mockedMusic);

        // any play method would suffice as the Gdx.audio.newMusic always returns the same thing.
        soundPlayer.playTableCollisionSound();
        soundPlayer.playPotSound();
        soundPlayer.playCueSound();
        Mockito.verify(mockedMusic, Mockito.times(0)).play();
    }
}
