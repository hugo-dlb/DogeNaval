package com.daltrisseville.DogeNaval.Server;

import com.daltrisseville.DogeNaval.Server.Entities.*;
import com.daltrisseville.DogeNaval.Server.Entities.Communications.ServerRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;

public class GameEngine {

	private ServerInstance serverInstance;
	private int maximumPlayers;
	private boolean gameStarted = false;
	private boolean gameFinished = false;
	private int currentPlayerId = -1;

	private PrivateBoard privateBoard = null;
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

	public void doNextStep(ClientHandler clientHandler) {
		this.gameStarted = true;

		while (!this.gameFinished) {
			for (Player p : players.values()) {
				if (BoardVerifier.gameFinished(privateBoard)) {
					this.gameFinished = true;
					break;
				}
				boolean playAgain;
				do {
					playAgain = false;

					this.currentPlayerId = p.getId();

					this.broadcastGameState();

					Tile selectedTile = waitForCurrentPlayerResponse(p);

					if (BoardVerifier.isValidTile(privateBoard, selectedTile)) {
						if (BoardVerifier.isHit(privateBoard, selectedTile)) {
							privateBoard.getTiles()[selectedTile.getRow()][selectedTile.getCol()]
									.setTileType(TileType.Hit);
							playAgain = true;
							p.upScore();
						} else {
							privateBoard.getTiles()[selectedTile.getRow()][selectedTile.getCol()]
									.setTileType(TileType.Miss);
						}
					} else {
						System.out.println("wrong tile noob");
						playAgain = true;
					}

					if (BoardVerifier.gameFinished(privateBoard)) {
						playAgain = false;
						this.gameFinished = true;
					}
				} while (playAgain);

			}
		}
		
		endGame();
	}

	private Tile waitForCurrentPlayerResponse(Player p) {
		// todo
		return new Tile(0, 0);
	}

	public void broadcastGameState() {
		this.serverInstance.broadcastGameState();
	}

	private void endGame() {
		//todo
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
		return this.players.size() >= this.maximumPlayers;
	}
}
