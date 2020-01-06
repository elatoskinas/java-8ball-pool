package com.sem.pool.game;

import java.util.Collection;

public interface ObservableGame {
    public void addObserver(GameObserver observer);

    public void removeObserver(GameObserver observer);

    public Collection<GameObserver> getObservers();

    public void startGame();

    public void startMotion();

    public void stopMotion();

    public void endGame();
}
