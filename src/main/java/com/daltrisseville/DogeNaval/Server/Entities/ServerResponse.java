package com.daltrisseville.DogeNaval.Server.Entities;

public class ServerResponse {

    private boolean success;
    private String errorCode;
    private String eventType = "GAME_STATE_DATA";
    private boolean connected;
    private boolean gameStarted;
    private boolean gameFinished;
    private int currentPlayerId;

    private PublicBoard publicBoard = null;
    private Player[] players = null;

    public ServerResponse(boolean success, String errorCode, boolean connected, boolean gameStarted,
                          boolean gameFinished, int currentPlayerId, PublicBoard publicBoard,
                          Player[] players) {
        this.success = success;
        this.errorCode = errorCode;
        this.connected = connected;
        this.gameStarted = gameStarted;
        this.gameFinished = gameFinished;
        this.currentPlayerId = currentPlayerId;
        this.publicBoard = publicBoard;
        this.players = players;
    }
}
