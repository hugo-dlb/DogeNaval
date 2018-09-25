package com.daltrisseville.DogeNaval.Server;

import com.daltrisseville.DogeNaval.Server.Authentication.AuthenticationService;
import com.daltrisseville.DogeNaval.Server.Entities.Communications.ClientResponse;
import com.daltrisseville.DogeNaval.Server.Entities.Communications.ServerRequest;
import com.daltrisseville.DogeNaval.Server.Entities.Player;
import com.daltrisseville.DogeNaval.Server.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;

/**
 * This class is a thread that handles a client connection
 */
public class ClientHandler extends Thread {

    private final ServerInstance serverInstance;
    private final String uuid;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket socket;
    private boolean clientIsConnected = true;
    private boolean clientIsAuthenticated = false;

    /**
     * Instantiates a ClientHandler with the server instance, the given client socket and its uuid
     *
     * @param serverInstance
     * @param uuid
     * @param socket
     * @param dataInputStream
     * @param dataOutputStream
     */
    public ClientHandler(ServerInstance serverInstance, String uuid, Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.serverInstance = serverInstance;
        this.uuid = uuid;
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    /**
     * Starts the thread
     */
    @Override
    public void run() {
        this.requestAuthentication();

        while (this.clientIsConnected) {
            try {
                if (!clientIsAuthenticated) {
                    this.requestAuthentication();
                } else {
                    this.startMainLoop();
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                this.clientIsConnected = false;
            }
        }

        try {
            this.socket.close();
            this.dataInputStream.close();
            this.dataOutputStream.close();
            this.serverInstance.removeClient(uuid);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Requests the credentials to the client
     */
    private void requestAuthentication() {
        while (this.clientIsConnected && !this.clientIsAuthenticated) {
            try {
                String loginRequestJSON = this.buildLoginRequest();
                this.emitData(loginRequestJSON);
                String response = this.dataInputStream.readUTF();

                Gson gson = new Gson();

                ClientResponse clientResponse = gson.fromJson(response, ClientResponse.class);
                System.out.println(response);

                AuthenticationService authenticationService = new AuthenticationService();
                User user = authenticationService.authenticatePlayer(clientResponse);

                boolean isUserAlreadyConnected = false;
                LinkedHashMap<String, Player> players = this.serverInstance.getGameEngine().getPlayers();
                for (String key : players.keySet()) {
                    Player currentPlayer = players.get(key);
                    if (currentPlayer.getId() == user.getId()) {
                        isUserAlreadyConnected = true;
                        break;
                    }
                }

                if (user != null && !isUserAlreadyConnected) {
                    Player player = new Player(user, 0, true);

                    try {
                        this.serverInstance.getGameEngine().addPlayer(this.uuid, player);
                        this.clientIsAuthenticated = true;
                    } catch (Exception exception) {
                        // do nothing
                    }
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                this.clientIsConnected = false;
            }
        }
    }

    /**
     * Handle client responses
     */
    private void startMainLoop() {
        while (this.clientIsConnected && this.clientIsAuthenticated) {
            try {
                this.serverInstance.getGameEngine().broadcastGameState();
                String response = this.dataInputStream.readUTF();
                System.out.println(response);

                Gson gson = new Gson();

                ClientResponse clientResponse = gson.fromJson(response, ClientResponse.class);

                switch (clientResponse.getEventType()) {
                    case "SEND_BOARD":
                        this.serverInstance.getGameEngine().initializeBoard(this, clientResponse);
                        System.out.println(clientResponse);
                        break;
                    case "PLAY":
                        this.serverInstance.getGameEngine().doNextStep(this, clientResponse);
                        break;
                    default:
                        this.serverInstance.getGameEngine().doNextStep(this, clientResponse);
                }
            } catch (Exception exception) {
                try {
                    this.serverInstance.getGameEngine().removePlayer(this.uuid);
                } catch (Exception nestedException) {
                    // do nothing
                }
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                this.clientIsConnected = false;
            }
        }
    }

    /**
     * Sends data to the client
     *
     * @param data
     * @throws Exception
     */
    public void emitData(String data) throws Exception {
        this.dataOutputStream.writeUTF(data);
    }

    /**
     * Returns the login request JSON string
     *
     * @return
     */
    private String buildLoginRequest() {
        ServerRequest serverRequest = new ServerRequest("LOGIN_REQUEST", false, false,
                -1, null, null, null,
                -1, this.serverInstance.getGameEngine().isGameFull(), false);

        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(serverRequest);
    }

    public String getUuid() {
        return uuid;
    }

    public boolean getClientIsConnected() {
        return this.clientIsConnected;
    }
}