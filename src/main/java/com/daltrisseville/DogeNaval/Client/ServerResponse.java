package com.daltrisseville.DogeNaval.Client;

public class ServerResponse {
	private boolean success;
	private String errorCode;
	private String eventType = "GAME_STATE_DATA";
	private boolean connected;
	private boolean gameStarted;
	private boolean gameFinished;
	private int currentPlayerId;

	private GenericBoard publicBoard = null;
	private Player[] players = null;
	
	private boolean adminOk;

	public ServerResponse(boolean success, String errorCode, boolean connected, boolean gameStarted,
			boolean gameFinished, int currentPlayerId, GenericBoard publicBoard, Player[] players, boolean adminOk) {
		this.success = success;
		this.errorCode = errorCode;
		this.connected = connected;
		this.gameStarted = gameStarted;
		this.gameFinished = gameFinished;
		this.currentPlayerId = currentPlayerId;
		this.publicBoard = publicBoard;
		this.players = players;
		
		this.adminOk=adminOk;
	}

	public boolean isAdminOk() {
		return adminOk;
	}

	public void setAdminOk(boolean adminOk) {
		this.adminOk = adminOk;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
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
