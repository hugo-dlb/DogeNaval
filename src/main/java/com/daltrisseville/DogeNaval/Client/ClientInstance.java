package com.daltrisseville.DogeNaval.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class ClientInstance {
	Scanner scanner;
	InetAddress ip;
	Socket s;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	boolean myTurn;

	DogeNavalGUI gui;

	public ClientInstance() {
		gui = new DogeNavalGUI(this);
		try {
			initConnexion();
			// start();
			awaitServerUpdate();
			closeRessources();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initConnexion() throws IOException {
		scanner = new Scanner(System.in);

		// getting localhost ip
		ip = InetAddress.getByName("localhost");

		// establish the connection with server port 5056
		s = new Socket(ip, 5056);

		// obtaining input and out streams
		dataInputStream = new DataInputStream(s.getInputStream());
		dataOutputStream = new DataOutputStream(s.getOutputStream());
	}

	public void closeRessources() throws IOException {
		// closing resources
		scanner.close();
		dataInputStream.close();
		dataOutputStream.close();
	}

	public void sendDataToServer(String toSend) throws IOException {
		dataOutputStream.writeUTF(toSend);
	}

	public void start() throws IOException {

		// read the first message sent by the server (LOGIN_REQUEST)
		String connectionWelcomeMessage = dataInputStream.readUTF();
		System.out.println(connectionWelcomeMessage);

		// the following loop performs the exchange of
		// information between client and client handler
		while (true) {
			String toSend = scanner.nextLine();
			sendDataToServer(toSend);

			// If client sends exit,close this connection
			// and then break from the while loop
			if (toSend.equals("Exit")) {
				System.out.println("Closing this connection : " + s);
				s.close();
				System.out.println("Connection closed");
				break;
			}

			// printing date or time as requested by client
			String received = dataInputStream.readUTF();
			System.out.println(received);
		}

	}

	public void awaitServerUpdate() throws IOException {
		while (true) {
			System.out.println("Wait For Server Update");
			String received = dataInputStream.readUTF();
			System.out.println(received);

			Gson gson = new GsonBuilder().serializeNulls().create();
			ServerResponse serverResponse = gson.fromJson(received, ServerResponse.class);

			switch (serverResponse.getEventType()) {
			case "":
				break;

			}

			// gui.startAdminPanel();
			if (serverResponse.isAdminOk()) { // start admin board
				gui.startAdminPanel();
			} else {
				// gui.startAdminPanel();
			}
		}
	}

	public static String buildAdminResponse(PrivateBoard b) {

		ClientResponse clientResponse = new ClientResponse("SENDBOARD", null, null, null, b);

		Gson gson = new GsonBuilder().serializeNulls().create();
		JsonObject s = (JsonObject) gson.toJsonTree(clientResponse);

		return s.toString();
	}

	public static String buildLoginResponse(String log, String pwd) {
		ClientResponse clientResponse = new ClientResponse("LOGIN", log, pwd, null, null);

		Gson gson = new GsonBuilder().serializeNulls().create();
		JsonObject s = (JsonObject) gson.toJsonTree(clientResponse);

		return s.toString();

	}

	public static String buildTileResponse(Tile t) {
		ClientResponse clientResponse = new ClientResponse("PLAY", null, null, t, null);

		Gson gson = new GsonBuilder().serializeNulls().create();
		JsonObject s = (JsonObject) gson.toJsonTree(clientResponse);

		return s.toString();
	}

	public static void main(String[] args) {
		ClientInstance cli = new ClientInstance();

	}
}
