package com.sem.pool.screens;

import com.badlogic.gdx.Screen;
import com.sem.pool.game.GameObserver;
import com.sem.pool.game.Player;
import com.sem.pool.scene.Ball3D;

import java.util.List;

public abstract class GameScreen implements Screen, GameObserver {
    @Override
    public void resize(int width, int height) {
        System.out.println("Resizing: " + width + ", " + height);
    }

    @Override
    public void hide() {
        System.out.println("Hiding game");
    }

    @Override
    public void onGameStarted() {
        System.out.println("Game started!");
    }

    @Override
    public void onBallPotted(Ball3D ball) {
        System.out.println(ball.getId() + " Potted!");
    }

    @Override
    public void onMotion() {
        System.out.println("Game is now in motion");
    }

    @Override
    public void onMotionStop(Ball3D lastTouched) {
        System.out.println("Game motion stopped");
    }

    @Override
    public void onGameEnded(Player winner, List<Player> players) {
        System.out.println(winner.getId() + " won!");
    }
}
