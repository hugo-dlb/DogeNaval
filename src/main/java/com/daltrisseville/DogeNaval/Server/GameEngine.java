package com.daltrisseville.DogeNaval.Server;

import com.daltrisseville.DogeNaval.Server.Entities.*;
import com.daltrisseville.DogeNaval.Server.Entities.Communications.ClientResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * This class contains the core game logic of DogeNaval.
 */
public class GameEngine {

    private ServerInstance serverInstance;
    private int maximumPlayers;
    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private boolean adminInitializedBoard = false;
    private int currentPlayerId = -1;

    private PrivateBoard privateBoard;
    private GenericBoard publicBoard;
    private LinkedHashMap<String, Player> players = new LinkedHashMap<>();

    /**
     * Instantiates the GameEngine with the server instance and a given number of maximum players in the game
     *
     * @param serverInstance
     * @param maximumPlayers
     */
    public GameEngine(ServerInstance serverInstance, int maximumPlayers) {
        if (maximumPlayers < 2) {
            this.maximumPlayers = 2;
        } else {
            this.maximumPlayers = maximumPlayers;
        }

        this.serverInstance = serverInstance;
        this.privateBoard = new PrivateBoard();
    }

    /**
     * Adds a player to the game
     *
     * @param playerThreadUUID
     * @param player
     * @throws Exception
     */
    public void addPlayer(String playerThreadUUID, Player player) throws Exception {
        if (!isGameFull()) {
            this.players.put(playerThreadUUID, player);

            if (this.isGameFull() && adminInitializedBoard) {
                this.gameStarted = true;
                this.initializeCurrentPlayerId();
            }
        } else {
            throw new Exception("Maximum number of players reached.");
        }
    }

    /**
     * Removes a player from the game
     *
     * @param playerThreadUUID
     * @throws Exception
     */
    public void removePlayer(String playerThreadUUID) throws Exception {
        if (this.players.containsKey(playerThreadUUID)) {
            if (this.gameStarted && !this.gameFinished) {
                this.players.get(playerThreadUUID).setConnected(false);
            } else if (!this.gameStarted) {
                this.players.remove(playerThreadUUID);
            }
            this.broadcastGameState();
        } else {
            throw new Exception("Player " + playerThreadUUID + " is not part of the game.");
        }
    }

    /**
     * Handles a move (turn) from a client
     *
     * @param clientHandler
     * @param clientResponse
     */
    public void doNextStep(ClientHandler clientHandler, ClientResponse clientResponse) {
        Player player = this.getPlayerFromClientHandler(clientHandler);
        boolean playAgain = false;

        if (!this.gameFinished && this.currentPlayerId == player.getId()) {
            Tile selectedTile = clientResponse.getSelectedTile();

            if (BoardVerifier.isValidTile(privateBoard, selectedTile)) {
                if (BoardVerifier.isHit(privateBoard, selectedTile)) {
                    privateBoard.getTiles()[selectedTile.getRow()][selectedTile.getCol()]
                            .setTileType(TileType.Hit);
                    player.upScore();
                    playAgain = true;
                } else {
                    privateBoard.getTiles()[selectedTile.getRow()][selectedTile.getCol()]
                            .setTileType(TileType.Miss);
                }
            }

            if (BoardVerifier.gameFinished(privateBoard)) {
                this.gameFinished = true;
                this.endGame();
            } else {
                if (!playAgain) {
                    this.updateCurrentPlayerId();
                }
                this.updatePublicBoard();
                this.broadcastGameState();
            }
        }
    }

    /**
     * Initializes the board from the admin response
     */
    public void initializeBoard(ClientHandler clientHandler, ClientResponse clientResponse) {
        Player player = this.getPlayerFromClientHandler(clientHandler);

        PrivateBoard privateBoard = clientResponse.getAdminBoard();

        if (player.getLevel().equals("ADMIN") && BoardVerifier.verifyBoardInit(privateBoard)) {
            this.adminInitializedBoard = true;
            this.privateBoard = clientResponse.getAdminBoard();
            this.publicBoard = clientResponse.getAdminBoard();
            if (this.isGameFull()) {
                this.initializeCurrentPlayerId();
                this.gameStarted = true;
            }
            this.broadcastGameState();
        }
    }

    /**
     * Broadcasts the game state to all clients
     */
    public void broadcastGameState() {
        this.serverInstance.broadcastGameState();
    }

    /**
     * Resets the game
     */
    private void endGame() {
        this.broadcastGameState();
        this.gameStarted = false;
        this.gameFinished = false;
        this.adminInitializedBoard = false;
        this.currentPlayerId = -1;
        this.privateBoard = new PrivateBoard();
        this.publicBoard = new PrivateBoard();

        for (String key : this.players.keySet()) {
            this.players.get(key).setScore(0);
        }
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

    private void initializeCurrentPlayerId() {
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

    /**
     * Updates the current player id
     */
    private void updateCurrentPlayerId() {
        // case where there is a single real player (not counting the admin)
        if (this.players.size() == 2) {
            return;
        }

        boolean isNext = false;
        Player[] currentPlayers = new Player[this.players.size()];

        int i = 0;
        for (String key : this.players.keySet()) {
            Player currentPlayer = this.players.get(key);
            currentPlayers[i] = currentPlayer;
            i++;
        }

        for (i = 0; i < currentPlayers.length; i++) {
            Player currentPlayer = currentPlayers[i];

            if (currentPlayer.getLevel().equals("USER") && this.isPlayerConnected(currentPlayer)) {
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
                i = -1;
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

    public boolean getGameStarted() {
        return this.gameStarted;
    }

    public boolean getGameFinished() {
        return this.gameFinished;
    }

    public int getCurrentPlayerId() {
        return this.currentPlayerId;
    }

    private String getPlayerUUID(Player player) {
        for (String key : this.players.keySet()) {
            Player currentPlayer = this.players.get(key);
            if (player.getId() == currentPlayer.getId()) {
                return key;
            }
        }

        return null;
    }

    /**
     * Checks if a player is connected
     *
     * @param player
     * @return
     */
    private boolean isPlayerConnected(Player player) {
        String playerUUID = this.getPlayerUUID(player);
        HashMap<String, ClientHandler> clients = this.serverInstance.getClients();

        for (String key : clients.keySet()) {
            ClientHandler currentClient = clients.get(key);
            if (currentClient.getUuid() == playerUUID) {
                return currentClient.getClientIsConnected();
            }
        }

        return false;
    }
}
