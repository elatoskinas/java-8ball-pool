package com.sem.pool.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sem.pool.MainGame;

/**
 * Launcher to launch the Game application on a Desktop platform.
 */
public class DesktopLauncher {
    /**
     * Starts the Game application on the DesktopLauncher.
     * @param arg  Optional arguments (unused in the Game application)
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new MainGame(), config);

        config.title = "8 Ball Pool";
        config.width = 1024;
        config.height = 768;

        // Anti-aliasing samples
        config.samples = 3;
    }
}
