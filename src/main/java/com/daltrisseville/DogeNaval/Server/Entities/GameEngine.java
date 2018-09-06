package com.daltrisseville.DogeNaval.Server.Entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameEngine {

	private int maximumPlayers;
	private boolean gameStarted = false;
	private boolean gameFinished = false;
	private int currentPlayerId = -1;

	private PrivateBoard privateBoard = null;
	private LinkedHashMap<String, Player> players = new LinkedHashMap<>();

	public GameEngine(int maximumPlayers) {
		if (maximumPlayers < 1) {
			this.maximumPlayers = 1;
		} else {
			this.maximumPlayers = maximumPlayers;
		}

		this.privateBoard = new PrivateBoard();
	}

	public void addPlayer(String playerThreadUUID, Player player) throws Exception {
		if (this.players.size() < this.maximumPlayers) {
			this.players.put(playerThreadUUID, player);
		} else {
			throw new Exception("Maximum number of players reached.");
		}
	}

	public void removePlayer(String playerThreadUUID) throws Exception {
		if (this.players.containsKey(playerThreadUUID)) {
			this.players.remove(playerThreadUUID);
		} else {
			throw new Exception("Player " + playerThreadUUID + " is not part of the game.");
		}
	}

	public void start() {
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

					sendDataToEveryone(); // nnniie

					Tile selectedTile = waitForCurrentPlayerResponse(p); // niiee

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
		// Player firstPlayer = new ArrayList<>(this.players.values()).get(0);
		// this.currentPlayerId = firstPlayer.getId();
	}

	private Tile waitForCurrentPlayerResponse(Player p) {
		// todo
		return new Tile(0, 0);
	}

	private void sendDataToEveryone() {
		// todo
	}
	private void endGame(){
		//todo
	}

}
