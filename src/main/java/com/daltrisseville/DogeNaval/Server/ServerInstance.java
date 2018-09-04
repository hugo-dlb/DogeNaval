package com.daltrisseville.DogeNaval.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class ServerInstance {

    private final static int SERVER_PORT = 5056;

    private HashMap<String,Thread> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerInstance serverInstance = new ServerInstance();
        serverInstance.start();
    }

    private void start() throws IOException {
        System.out.println("Starting...");
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        // running infinite loop for getting client request
        while (true) {
            Socket clientSocket = null;

            try {
                // socket object to receive incoming client requests
                clientSocket = serverSocket.accept();

                // obtaining input and out streams
                DataInputStream clientSocketDataInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream clientSocketDataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                this.addClient(clientSocket, clientSocketDataInputStream, clientSocketDataOutputStream);
            } catch (Exception e) {
                clientSocket.close();
                e.printStackTrace();
            }
        }
    }

    private void addClient(Socket clientSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        String uuid = UUID.randomUUID().toString();

        // create a new thread
        Thread clientHandlerThread = new ClientHandler(this, uuid, clientSocket, dataInputStream, dataOutputStream);

        // invoking the start() method
        clientHandlerThread.start();

        clients.put(uuid, clientHandlerThread);
        System.out.println("Client " + uuid + " just connected.");
    }

    public void removeClient(String uuid) {
        this.clients.remove(uuid);
        System.out.println("Client " + uuid + " disconnected.");
    }
}