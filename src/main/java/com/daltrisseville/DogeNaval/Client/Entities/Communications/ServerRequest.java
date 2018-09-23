package com.daltrisseville.DogeNaval.Client.Entities.Communications;

import com.daltrisseville.DogeNaval.Client.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Player;
import com.daltrisseville.DogeNaval.Client.Entities.PrivateBoard;

public class ServerRequest {

	private String eventType;
	private boolean gameStarted;
	private boolean gameFinished;
	private int currentPlayerId;
	private GenericBoard publicBoard;
	private PrivateBoard privateBoard;
	private Player[] players;
	private int playerId;
	private boolean gameFull;
	private boolean isAdmin;

	public ServerRequest(String eventType, boolean gameStarted, boolean gameFinished, int currentPlayerId,
			GenericBoard publicBoard, PrivateBoard privateBoard, Player[] players, int playerId, boolean gameFull,
			boolean isAdmin) {
		this.eventType = eventType;
		this.gameStarted = gameStarted;
		this.gameFinished = gameFinished;
		this.currentPlayerId = currentPlayerId;
		this.publicBoard = publicBoard;
		this.privateBoard = privateBoard;
		this.players = players;
		this.playerId = playerId;
		this.gameFull = gameFull;
		this.isAdmin = isAdmin;
	}

	public String getEventType() {
		return eventType;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	public GenericBoard getPublicBoard() {
		return publicBoard;
	}

	public PrivateBoard getPrivateBoard() {
		return privateBoard;
	}

	public Player[] getPlayers() {
		return players;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isGameFull() {
		return gameFull;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

}
