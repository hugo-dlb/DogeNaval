package com.daltrisseville.DogeNaval.Server.Entities.Communications;

import com.daltrisseville.DogeNaval.Server.Entities.PrivateBoard;
import com.daltrisseville.DogeNaval.Server.Entities.Tile;

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

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Tile getSelectedTile() {
		return selectedTile;
	}

	public void setSelectedTile(Tile selectedTile) {
		this.selectedTile = selectedTile;
	}

	public PrivateBoard getAdminBoard() {
		return adminBoard;
	}

	public void setAdminBoard(PrivateBoard adminBoard) {
		this.adminBoard = adminBoard;
	}

}
