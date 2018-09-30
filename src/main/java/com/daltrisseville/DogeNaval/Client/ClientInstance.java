package com.daltrisseville.DogeNaval.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.daltrisseville.DogeNaval.Client.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Player;
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
	boolean launched;
	boolean connected;
	boolean adminCreating;

	Player[] players;
	boolean myTurn = false;

	DogeNavalGUI gui;

	public ClientInstance() {
		gui = new DogeNavalGUI(this);
		try {
			this.launched = false;
			this.adminCreating = false;
			this.connected = false;
			initConnexion();
			// start();
			awaitServerUpdate();
			closeRessources();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initConnexion() throws IOException {
		// scanner = new Scanner(System.in);

		// getting localhost ip

		// ip = InetAddress.getByName("185.126.228.91");
		ip = InetAddress.getByName("localhost");
		boolean nn = ip.isReachable(5000);
		System.out.println(nn);

		// establish the connection with server port 5056
		s = new Socket(ip, 5056);
		System.out.println("socketpassed");
		// obtaining input and out streams
		dataInputStream = new DataInputStream(s.getInputStream());
		dataOutputStream = new DataOutputStream(s.getOutputStream());
	}

	public void closeRessources() throws IOException {
		// closing resources
		// scanner.close();
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
			this.players = sr.getPlayers();
			this.myTurn = sr.getPlayerId() == sr.getCurrentPlayerId();

			if (sr.getEventType().equals("GAME_STATE")) {
				if (sr.isAdmin()) {
					if (!sr.isGameStarted() && !this.adminCreating) {

						this.launched = false;
						System.out.println("startAdminPanel clientside");
						this.adminCreating = true;

						gui.startAdminPanel();

					} else if (this.adminCreating) {
						this.launched = false;
						System.out.println("new player connects");
						gui.updateAdminLabel();
					} else if (!this.launched) {

						System.out.println("startGameForAdmin");
						this.launched = true;
					} else if (sr.isGameFinished()) {
						String message = "<html>Scores : <br>";
						for (Player p : players) {
							if (!p.getLevel().equals("ADMIN")) {
								message += p.toString() + "<br>";
							}

						}
						message += "</html>";
						JOptionPane.showMessageDialog(null, message, "Final scores", JOptionPane.PLAIN_MESSAGE);
					} else {
						PrivateBoard newBoard = sr.getPrivateBoard();
						gui.updateAdminBoard(newBoard);

					}

				} else {// not admin

					if (!sr.isGameStarted()) {

						gui.goToLobby(sr.getPlayers());
						this.launched = false;
					} else if (!this.launched) {
						System.out.println("startGamePanel");

						gui.startGamePanel();
						this.launched = true;
					} else if (sr.isGameFinished()) {

						Player best = null;
						int i = 0;
						String message = "<html>Scores : <br>";
						for (Player p : players) {
							if (!p.getLevel().equals("ADMIN")) {
								message += p.toString() + "<br>";
							}
							if (p.getScore() > i) {
								best = p;
								i = p.getScore();

							}
						}
						
						String s = best.getId() == sr.getPlayerId()? "Gagné !!!" : "Perdu NOOB";
						message+="<br>"+s+"</html>";

						JOptionPane.showMessageDialog(null, message, s, JOptionPane.PLAIN_MESSAGE);
					} else {
						GenericBoard newBoard = sr.getPublicBoard();
						gui.updatePlayerBoard(newBoard);
					}

				}

			} else if (sr.getEventType().equals("LOGIN_REQUEST")) {

				if (sr.isGameStarted()) {
					JOptionPane.showMessageDialog(null, "Sorry! Game already started!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (sr.isGameFull()) {
					JOptionPane.showMessageDialog(null, "Sorry! Game full", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!this.connected) {
					this.connected = true;
				} else {
					JOptionPane.showMessageDialog(null, "Wrong login/password", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
	}

	public void setAdminCreating(boolean adminCreating) {
		this.adminCreating = adminCreating;
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

	public boolean isConnected() {
		return connected;
	}

	public boolean isAdminCreating() {
		return adminCreating;
	}

	public Player[] getPlayers() {
		return players;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setLaunched(boolean launched) {
		this.launched = launched;
	}

	public static void main(String[] args) {
		ClientInstance cli = new ClientInstance();

	}
}
