package com.daltrisseville.DogeNaval.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.daltrisseville.DogeNaval.Client.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Client.Entities.PrivateBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Tile;
import com.daltrisseville.DogeNaval.Client.Entities.Communications.ClientResponse;
import com.daltrisseville.DogeNaval.Client.Entities.Communications.ServerRequest;
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

	boolean launched;

	DogeNavalGUI gui;

	public ClientInstance() {
		gui = new DogeNavalGUI(this);
		try {
			this.launched = false;
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

	public void awaitServerUpdate() throws IOException {
		while (true) {
			System.out.println("Wait For Server Update");
			String received = dataInputStream.readUTF();
			System.out.println(received);

			Gson gson = new GsonBuilder().serializeNulls().create();
			ServerRequest sr = gson.fromJson(received, ServerRequest.class);

			if (sr.getEventType().equals("GAME_STATE")) {
				if (sr.isAdmin()) {
					if (!this.launched) {
						gui.startAdminPanel();
						this.launched = true;
					} else {
						PrivateBoard newBoard = sr.getPrivateBoard();
						gui.updateAdminBoard(newBoard);
					}

				} else {// not admin

					// in progress
					if (!this.launched) {
						gui.startGamePanel();
						this.launched = true;
					} else {
						GenericBoard newBoard = sr.getPublicBoard();
						gui.updatePlayerBoard(newBoard);
					}

				}

			} else if (sr.getEventType().equals("LOGIN_REQUEST")) {

				if (sr.isGameStarted()) {
					JOptionPane.showMessageDialog(null, "Game full", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Wrong login/password", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} 

		}
	}

	public static String buildAdminResponse(PrivateBoard b) {

		ClientResponse clientResponse = new ClientResponse("SEND_BOARD", null, null, null, b);

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

	public boolean isLaunched() {
		return launched;
	}

	public void setLaunched(boolean launched) {
		this.launched = launched;
	}

	public static void main(String[] args) {
		ClientInstance cli = new ClientInstance();

	}
}

