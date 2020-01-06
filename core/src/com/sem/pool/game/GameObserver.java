package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;

public interface GameObserver {
    public void onGameStarted();
    public void onBallPotted(Ball3D ball);
    public void onMotion();
    public void onMotionStop();
}
