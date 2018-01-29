package com.pkproject.server;

import com.pkproject.objects.Player;
import com.pkproject.objects.Question;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerClientMessage implements Serializable{
    public static final int TURNONQUIZ = 0, TURNOFFQUIZ = 1, SAVERESULT = 3, CHECKALLRESULT = 4;
    private int idUser;
    private int type;
    private List<Question> questions;
    public ServerClientMessage(int type) {
        this.type = type;
    }
    private List<Player> players;
    private Player player;

    public int getType() {
        return type;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
