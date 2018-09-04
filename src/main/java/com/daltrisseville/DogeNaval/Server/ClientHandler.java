package com.daltrisseville.DogeNaval.Server;

import com.daltrisseville.DogeNaval.Server.Authentication.AuthenticationService;
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
    private boolean clientIsAuthentified = false;

    public ClientHandler(ServerInstance parentServer, String uuid, Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.parentServer = parentServer;
        this.uuid = uuid;
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        String received;
        while (this.clientIsConnected) {
            System.out.println("test");
            try {
                System.out.println("1");
                if (!clientIsAuthentified) {
                    System.out.println("2");
                    // handle authentication
                    String loginRequestJSON = this.buildLoginRequest(true);
                    this.dataOutputStream.writeUTF(loginRequestJSON);
                    System.out.println("2.1");
                    received = this.dataInputStream.readUTF();
                    System.out.println("2.2");

                    try {
                        System.out.println("4");
                        Gson gson = new Gson();
                        ClientLoginEvent clientLoginEvent = gson.fromJson(received, ClientLoginEvent.class);

                        AuthenticationService authenticationService = new AuthenticationService();
                        boolean loggedIn = authenticationService.authenticatePlayer(clientLoginEvent);

                        if (loggedIn) {
                            System.out.println("5");
                            System.out.println("OK");
                            this.clientIsAuthentified = true;
                        } else {
                            System.out.println("6");
                        }
                    } catch (Exception e) {
                        System.out.println("7");
                        System.out.println(e.getMessage());
                        // do nothing
                    }
                } else {
                    System.out.println("3");
                    // todo
                    this.dataOutputStream.writeUTF("What do you want?\n" +
                            "Type Exit to terminate connection.");

                    // receive the answer from client
                    received = this.dataInputStream.readUTF();

                    if (received.equals("Exit")) {
                        System.out.println("Client " + this.socket + " sends exit...");
                        System.out.println("Closing connection...");
                        this.clientIsConnected = false;
                    } else {
                        // for testing purposes only
                        Gson gson = new GsonBuilder().serializeNulls().create();

                        Player p1 = new Player(1, "Hugo", 0, true);
                        Player p2 = new Player(2, "Arthur", 0, true);
                        Player[] players = {p1, p2};
                        ServerResponse responseObject = new ServerResponse(true, null, true, false, false, -1, null, players);

                        String responseJSON = gson.toJson(responseObject);
                        this.dataOutputStream.writeUTF(responseJSON);
                    }
                }
            } catch (SocketException socketException) {
                System.out.println("socketException");
                this.clientIsConnected = false;
            } catch (IOException exception) {
                System.out.println("IOException");
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

    public void emitData(String data) throws IOException {
        this.dataOutputStream.writeUTF(data);
    }

    private String buildLoginRequest(boolean success) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("success", success);
        responseObject.addProperty("eventType", "LOGIN_REQUEST");

        Gson gson = new Gson();
        return gson.toJson(responseObject);
    }
}