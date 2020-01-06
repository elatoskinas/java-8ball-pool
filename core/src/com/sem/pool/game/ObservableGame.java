package com.sem.pool.game;

public interface ObservableGame {
    public void addObserver(GameObserver observer);

    public void removeObserver(GameObserver observer);

    public Iterable<GameObserver> getObservers();

    public void onMotionStart();

    public void onMotionStop();

    public void endGame();
}
