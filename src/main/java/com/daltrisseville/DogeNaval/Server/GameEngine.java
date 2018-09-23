package com.daltrisseville.DogeNaval.Server;

import com.daltrisseville.DogeNaval.Server.Entities.*;
import com.daltrisseville.DogeNaval.Server.Entities.Communications.ClientResponse;

import java.util.LinkedHashMap;

public class GameEngine {

	private ServerInstance serverInstance;
	private int maximumPlayers;
	private boolean gameStarted = false;
	private boolean gameFinished = false;
	private int currentPlayerId = -1;

	private PrivateBoard privateBoard;
	private GenericBoard publicBoard;
	private LinkedHashMap<String, Player> players = new LinkedHashMap<>();

	public GameEngine(ServerInstance serverInstance, int maximumPlayers) {
		if (maximumPlayers < 2) {
			this.maximumPlayers = 2;
		} else {
			this.maximumPlayers = maximumPlayers;
		}

		this.serverInstance = serverInstance;
		this.privateBoard = new PrivateBoard();
	}

	public void addPlayer(String playerThreadUUID, Player player) throws Exception {
		if (!isGameFull()) {
			this.players.put(playerThreadUUID, player);

			if (this.isGameFull()) {
				this.gameStarted = true;

				int firstPlayerId = -1;

				// setting the player that will play first
				for (String key : this.players.keySet()) {
					Player currentPlayer = this.players.get(key);

					if (currentPlayer.getLevel().equals("USER")) {
						firstPlayerId = currentPlayer.getId();
						break;
					}
				}

				this.currentPlayerId = firstPlayerId;
			}
		} else {
			throw new Exception("Maximum number of players reached.");
		}
	}

	public void removePlayer(String playerThreadUUID) throws Exception {
		if (this.players.containsKey(playerThreadUUID)) {
			if (this.gameStarted && !this.gameFinished) {
				this.players.get(playerThreadUUID).setConnected(false);
			} else if (!this.gameStarted) {
				this.players.remove(playerThreadUUID);
			}
		} else {
			throw new Exception("Player " + playerThreadUUID + " is not part of the game.");
		}
	}

	// handle a client turn
	public void doNextStep(ClientHandler clientHandler, ClientResponse clientResponse) {
		Player player = this.getPlayerFromClientHandler(clientHandler);

		if (!this.gameFinished && this.currentPlayerId == player.getId()) {
			Tile selectedTile = clientResponse.getSelectedTile();

			if (BoardVerifier.isValidTile(privateBoard, selectedTile)) {
				if (BoardVerifier.isHit(privateBoard, selectedTile)) {
					privateBoard.getTiles()[selectedTile.getRow()][selectedTile.getCol()]
							.setTileType(TileType.Hit);
					player.upScore();
				} else {
					privateBoard.getTiles()[selectedTile.getRow()][selectedTile.getCol()]
							.setTileType(TileType.Miss);
				}
			}

			if (BoardVerifier.gameFinished(privateBoard)) {
				this.gameFinished = true;
				this.endGame();
			} else {
				this.updateCurrentPlayerId();
				this.updatePublicBoard();
				this.broadcastGameState();
			}
		}
	}

	// initialize the board from the admin response
	public void initializeBoard(ClientHandler clientHandler, ClientResponse clientResponse) {
		Player player = this.getPlayerFromClientHandler(clientHandler);

		if (player.getLevel().equals("ADMIN") && BoardVerifier.verifyBoardInit(privateBoard)) {
			this.privateBoard = clientResponse.getAdminBoard();
			this.publicBoard = clientResponse.getAdminBoard();
			this.gameStarted = true;

			this.broadcastGameState();
		}
	}

	public void broadcastGameState() {
		this.serverInstance.broadcastGameState();
	}

	private void endGame() {
		// todo reset game and players
		this.broadcastGameState();
	}

	public LinkedHashMap<String, Player> getPlayers() {
		return this.players;
	}

	public Player[] getPlayersArray() {
		Player[] players = new Player[this.players.size()];

		int i = 0;
		for (String key : this.players.keySet()) {
			players[i] = this.players.get(key);
			i++;
		}

		return players;
	}

	public boolean isGameFull() {
		// +1 because admin is not really a player
		return this.players.size() >= this.maximumPlayers + 1;
	}

	public Player getPlayerFromClientHandler(ClientHandler clientHandler) {
		return this.players.get(clientHandler.getUuid());
	}

	private void updateCurrentPlayerId() {
		// case where there is a single real player (not counting the admin)
		if (this.players.size() == 2) {
			return;
		}

		Player[] currentPlayers = (Player[])this.players.keySet().toArray();
		boolean isNext = false;

		for (int i = 0; i < currentPlayers.length; i++ ) {
			Player currentPlayer = currentPlayers[i];

			if (currentPlayer.getLevel().equals("USER")) {
				if (isNext) {
					// found the next player
					this.currentPlayerId = currentPlayer.getId();
					return;
				} else {
					if (currentPlayer.getId() == this.currentPlayerId) {
						isNext = true;
					}
				}
			}

			if (i == currentPlayers.length - 1) {
				i = 0;
			}
		}
	}

	private void updatePublicBoard() {
		this.publicBoard = this.privateBoard;
	}

	public GenericBoard getPublicBoard() {
		return this.publicBoard;
	}

	public PrivateBoard getPrivateBoard() {
		return this.privateBoard;
	}
}
