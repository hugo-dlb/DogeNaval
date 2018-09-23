package com.daltrisseville.DogeNaval.Server.Entities.Communications;

import com.daltrisseville.DogeNaval.Server.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Server.Entities.Player;

public class ServerRequest {

	private String eventType;
	private boolean gameStarted;
	private boolean gameFinished;
	private int currentPlayerId;
	private GenericBoard publicBoard;
	private GenericBoard privateBoard;
	private Player[] players;
	private int playerId;
	private boolean gameFull;
	private boolean isAdmin;

	public ServerRequest(String eventType, boolean gameStarted,
                         boolean gameFinished, int currentPlayerId, GenericBoard publicBoard, GenericBoard privateBoard, Player[] players, int playerId, boolean gameFull, boolean isAdmin) {
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

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

	public void setGameFinished(boolean gameFinished) {
		this.gameFinished = gameFinished;
	}

	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	public void setCurrentPlayerId(int currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	public GenericBoard getPublicBoard() {
		return publicBoard;
	}

	public void setPublicBoard(GenericBoard publicBoard) {
		this.publicBoard = publicBoard;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

}
