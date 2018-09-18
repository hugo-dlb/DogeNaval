package com.daltrisseville.DogeNaval.Client;

import com.daltrisseville.DogeNaval.Server.Authentication.User;

public class Player {

	private User user;
	private int score;
	private boolean isConnected;

	public Player(User user, int score, boolean isConnected) {
		this.user = user;
		this.score = score;
		this.isConnected = isConnected;
	}

	public String toString() {
		return this.user.getUsername() + "(" + this.score + ") points, "
				+ (this.isConnected ? "connected" : "disconnected") + ".";
	}

	public int getId() {
		return this.user.getId();
	}

	public void upScore() {
		this.score++;
	}
}
