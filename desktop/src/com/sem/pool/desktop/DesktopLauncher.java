package com.sem.pool.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sem.pool.screens.MainGame;

/**
 * Launcher to launch the Game application on a Desktop platform.
 */
public class DesktopLauncher {

    public static final String GAME_NAME =  "8 Ball Pool";
    public static int screenWidth = 1024;
    public static int screenHeight = 768;

    private static final int ANTI_ALIASING_SAMPLES = 3;

    /**
     * Starts the Game application on the DesktopLauncher.
     * @param arg  Optional arguments (unused in the Game application)
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new MainGame(), config);

        config.title = GAME_NAME;
        config.width = screenWidth;
        config.height = screenHeight;

        config.samples = ANTI_ALIASING_SAMPLES;
    }
}
