package com.daltrisseville.DogeNaval.Server.Entities;

import com.daltrisseville.DogeNaval.Client.TileType;

public class Tile {
	private int row;
	private int col;
	private TileType tileType;
	
	public Tile(int i, int j) {
		this.row = i;
		this.col = j;
		this.tileType=TileType.Empty;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public TileType getTileType() {
		return tileType;
	}

	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}

}

