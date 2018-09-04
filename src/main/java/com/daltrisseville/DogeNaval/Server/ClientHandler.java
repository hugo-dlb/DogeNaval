package com.daltrisseville.DogeNaval.Server;

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
            try {
                // ask user what he wants
                this.dataOutputStream.writeUTF("What do you want?\n" +
                        "Type Exit to terminate connection.");

                // receive the answer from client
                received = this.dataInputStream.readUTF();

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Closing connection...");
                    this.clientIsConnected = false;
                } else {
                    System.out.println("Sent 'Hello World!'.");
                    this.dataOutputStream.writeUTF("Hello world!");
                }
            } catch (SocketException socketException) {
                this.clientIsConnected = false;
            } catch (IOException exception) {
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
}