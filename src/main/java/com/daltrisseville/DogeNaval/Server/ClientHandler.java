package com.daltrisseville.DogeNaval.Server;

import com.daltrisseville.DogeNaval.Server.Authentication.AuthenticationService;
import com.daltrisseville.DogeNaval.Server.Authentication.User;
import com.daltrisseville.DogeNaval.Server.Authentication.UserHandler;
import com.daltrisseville.DogeNaval.Server.Entities.ClientLoginEvent;
import com.daltrisseville.DogeNaval.Server.Entities.ClientResponse;
import com.daltrisseville.DogeNaval.Server.Entities.ServerResponse;
import com.daltrisseville.DogeNaval.Server.Entities.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

class ClientHandler extends Thread {

    private final ServerInstance parentServer;
    private final String uuid;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket socket;
    private boolean clientIsConnected = true;
    private boolean clientIsAuthenticated = false;

    public ClientHandler(ServerInstance parentServer, String uuid, Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.parentServer = parentServer;
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
            } catch (SocketException socketException) {
                System.out.println("socketException");
                this.clientIsConnected = false;
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        try {
            this.socket.close();
            this.dataInputStream.close();
            this.dataOutputStream.close();
            this.parentServer.removeClient(uuid);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String buildResponse(boolean success, String eventType) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("success", success);
        responseObject.addProperty("eventType", eventType);

        Gson gson = new Gson();
        return gson.toJson(responseObject);
    }

    private void requestAuthentication() {
        while (this.clientIsConnected && !this.clientIsAuthenticated) {
            String loginRequestJSON = this.buildResponse(true, "LOGIN_REQUEST");
            try {
                this.dataOutputStream.writeUTF(loginRequestJSON);
                String response = this.dataInputStream.readUTF();
                System.out.println(response);

                try {
                    Gson gson = new Gson();

                    try {
                        ClientLoginEvent clientLoginEvent = gson.fromJson(response, ClientLoginEvent.class);

                        AuthenticationService authenticationService = new AuthenticationService();
                        boolean loggedIn = authenticationService.authenticatePlayer(clientLoginEvent);

                        if (loggedIn) {
                            this.clientIsAuthenticated = true;
                        }
                    } catch (Exception exception) {
                        // do nothing
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                } catch (Exception exception) {
                    // do nothing
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                this.clientIsConnected = false;
            }
        }
    }

    private void startMainLoop() throws Exception {
        while (this.clientIsConnected && this.clientIsAuthenticated) {
            this.dataOutputStream.writeUTF("What do you want?\n" +
                    "Type Exit to terminate connection.");

            // receive the answer from client
            String response = this.dataInputStream.readUTF();

            if (response.equals("Exit")) {
                System.out.println("Client " + this.socket + " sends exit...");
                System.out.println("Closing connection...");
                this.clientIsConnected = false;
            } else {
                // for testing purposes only
                Gson gson = new GsonBuilder().serializeNulls().create();

                UserHandler userHandler = new UserHandler();
                User hugo = userHandler.createUser("hugo", "toto");
                User arthur = userHandler.createUser("arthur", "toto");
                Player p1 = new Player(hugo, 0, true);
                Player p2 = new Player(arthur, 0, true);
                Player[] players = {p1, p2};
                ServerResponse responseObject = new ServerResponse(true, null, true, false, false, -1, null, players);

                String responseJSON = gson.toJson(responseObject);
                this.dataOutputStream.writeUTF(responseJSON);
            }
        }
    }
}