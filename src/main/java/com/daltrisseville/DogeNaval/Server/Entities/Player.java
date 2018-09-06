package com.daltrisseville.DogeNaval.Server.Entities;

public class Player {

    private int id;
    private String username;
    private int score;
    private boolean isConnected;

    public Player(int id, String username, int score, boolean isConnected) {
        this.id = id;
        this.username = username;
        this.score = score;
        this.isConnected = isConnected;
    }

    public String toString() {
        return this.username + "(" + this.score + ") points, " + (this.isConnected ? "connected" : "disconnected") + ".";
    }

    public int getId() {
        return this.id;
    }
    
    public void upScore(){
    	this.score++;
    }
}
