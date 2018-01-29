package com.pkproject.objects;

import java.io.Serializable;

public class Player implements Serializable{
    private String nameOfPlayer;
    private int result;
    static int allNumberOfPlayer;

    public Player() {
        nameOfPlayer = "Player: " + ++allNumberOfPlayer;
    }
    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }


}
