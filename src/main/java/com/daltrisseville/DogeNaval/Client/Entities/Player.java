package com.daltrisseville.DogeNaval.Client.Entities;

import com.daltrisseville.DogeNaval.Server.Entities.User;

public class Player {

	private User user;
	private int score;
	private boolean connected;

	public Player(User user, int score, boolean isConnected) {
		this.user = user;
		this.score = score;
		this.connected = isConnected;
	}

	public String toString() {
		return this.user.getUsername() + "(" + this.score + ") points, "
				+ (this.connected ? "connected" : "disconnected") + ".";
	}

	public int getId() { return this.user.getId(); }

	public void upScore() {
		this.score++;
	}

	public void setConnected (boolean connected) {
		this.connected = connected;
	}
}
