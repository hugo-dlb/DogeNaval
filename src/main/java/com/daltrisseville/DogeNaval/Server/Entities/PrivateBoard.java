package com.daltrisseville.DogeNaval.Server.Entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;


public class PrivateBoard {
	private Tile[][] tiles;
	private ArrayList<Dog> dogs;
	private int boardSize;
	// private int[] expectedDogList;
	private ArrayList<Integer> expectedDogList;
	private static int BOARD_SIZE = 10;
	private static Integer[] BOARD_DOG_LIST = { 3, 4, 5 };

	public PrivateBoard() {
		this.boardSize = BOARD_SIZE;
		// this.expectedDogList = BOARD_DOG_LIST;
		this.expectedDogList = new ArrayList<Integer>();
		for (int i : BOARD_DOG_LIST) {
			expectedDogList.add(i);
		}
		this.dogs = new ArrayList<Dog>();
		this.tiles = new Tile[boardSize][boardSize];

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				this.tiles[i][j] = new Tile(i, j);
			}
		}
	}

	public void addDog(int x, int y, int length, DogDirection d) {
		Dog dog = new Dog(length, x, y, d);
		dogs.add(dog);
	}

	public ArrayList<Integer> getExpectedDogList() {
		return expectedDogList;
	}

	public static int getBOARD_SIZE() {
		return BOARD_SIZE;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public ArrayList<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(ArrayList<Dog> dogs) {
		this.dogs = dogs;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

}
