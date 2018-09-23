package com.daltrisseville.DogeNaval.Client.Entities.Communications;

import com.daltrisseville.DogeNaval.Client.Entities.PrivateBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Tile;

public class ClientResponse {
	private String eventType;
	private String username;
	private String password;
	private Tile selectedTile;
	private PrivateBoard adminBoard;

	public ClientResponse(String eventType, String username, String password, Tile selectedTile,
			PrivateBoard adminBoard) {
		this.eventType = eventType;
		this.username = username;
		this.password = password;
		this.selectedTile = selectedTile;
		this.adminBoard = adminBoard;
	}

	public String getEventType() {
		return eventType;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Tile getSelectedTile() {
		return selectedTile;
	}

	public PrivateBoard getAdminBoard() {
		return adminBoard;
	}

}
