package com.pkproject.objects;

public class StateGame {
    public boolean startQuiz;
    public boolean endGame;
    public boolean running;

    public StateGame() {
      startQuiz = false;
      endGame = false;
      running = false;
    }

    public boolean isStartQuiz() {
        return startQuiz;
    }

    public void setStartQuiz(boolean startQuiz) {
        this.startQuiz = startQuiz;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
