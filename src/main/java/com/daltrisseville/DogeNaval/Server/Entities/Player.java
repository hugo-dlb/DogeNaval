package com.daltrisseville.DogeNaval.Server.Entities;

/**
 * This class represents a player in the game
 */
public class Player {

    private User user;
    private int score;
    private boolean connected;

    public Player(User user, int score, boolean connected) {
        this.user = user;
        this.score = score;
        this.connected = connected;
    }

    public String toString() {
        return this.user.getUsername() + "(" + this.score + ") points, " + (this.connected ? "connected" : "disconnected") + ".";
    }

    public int getId() {
        return this.user.getId();
    }

    public String getLevel() {
        return this.user.getLevel();
    }

    public void upScore() {
        this.score++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
