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

public class ClientHandler extends Thread {

    private final ServerInstance serverInstance;
    private final String uuid;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket socket;
    private boolean clientIsConnected = true;
    private boolean clientIsAuthenticated = false;

    public ClientHandler(ServerInstance serverInstance, String uuid, Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.serverInstance = serverInstance;
        this.uuid = uuid;
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

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

                if (user != null) {
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

    public void emitData(String data) throws Exception {
        this.dataOutputStream.writeUTF(data);
    }

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
}