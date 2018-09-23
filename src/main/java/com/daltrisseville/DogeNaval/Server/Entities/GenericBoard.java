package com.daltrisseville.DogeNaval.Server.Entities;


public class GenericBoard {

    private Tile[][] tiles;

    private int boardSize;

    private static int BOARD_SIZE = 10;

    public GenericBoard() {

        this.boardSize = BOARD_SIZE;
        this.tiles = new Tile[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.tiles[i][j] = new Tile(i, j);
            }
        }
    }

    public GenericBoard(GenericBoard b) {
        this.boardSize = BOARD_SIZE;
        this.tiles = b.getTiles();

    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public static int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

}
