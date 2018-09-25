package com.daltrisseville.DogeNaval.Server.Entities;

/**
 * This class represents a tile of the board
 */
public class Tile {
	private int row;
	private int col;
	private TileType tileType;
	
	public Tile(int i, int j) {
		this.row = i;
		this.col = j;
		this.tileType=TileType.Empty;
	}
	public String toString() {
		return this.col+" - "+this.row;
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

