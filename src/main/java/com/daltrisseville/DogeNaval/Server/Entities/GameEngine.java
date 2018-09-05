package com.daltrisseville.DogeNaval.Server.Entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameEngine {

    private int maximumPlayers;
    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private int currentPlayerId = -1;

    private PrivateBoard privateBoard = null;
    private LinkedHashMap<String,Player> players = new LinkedHashMap<>();

    public GameEngine(int maximumPlayers) {
        if (maximumPlayers < 1) {
            this.maximumPlayers = 1;
        } else {
            this.maximumPlayers = maximumPlayers;
        }

        this.privateBoard = new PrivateBoard();
    }

    public void addPlayer(String playerThreadUUID, Player player) throws Exception {
        if (this.players.size() < this.maximumPlayers) {
            this.players.put(playerThreadUUID, player);
        } else {
            throw new Exception("Maximum number of players reached.");
        }
    }

    public void removePlayer(String playerThreadUUID) throws Exception {
        if (this.players.containsKey(playerThreadUUID)) {
            this.players.remove(playerThreadUUID);
        } else {
            throw new Exception("Player " + playerThreadUUID + " is not part of the game.");
        }
    }

    public void start() {
        this.gameStarted = true;
        Player firstPlayer = new ArrayList<>(this.players.values()).get(0);
        this.currentPlayerId = firstPlayer.getId();
    }
    
    

    private void moveToNextStep() {
        // todo
    }
}
