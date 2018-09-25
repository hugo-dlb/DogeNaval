package com.daltrisseville.DogeNaval.Client.Entities;

import java.util.ArrayList;

/**
 * This class provides validation static functions
 */
public class BoardVerifier {

	/**
	 * Determines if a game is finished based on the given private board
	 *
	 * @param b
	 * @return
	 */
	public static boolean gameFinished(PrivateBoard b) {
		for (Dog dog : b.getDogs()) {

			if (dog.getDirection() == DogDirection.Horizontal) {
				for (int i = 0; i < dog.getLength(); i++) {
					if (b.getTiles()[dog.getyStart()][dog.getxStart() + i].getTileType() != TileType.Hit) {
						return false;
					}
				}
			} else if (dog.getDirection() == DogDirection.Vertical) {
				for (int j = 0; j < dog.getLength(); j++) {
					if (b.getTiles()[dog.getyStart() + j][dog.getxStart()].getTileType() != TileType.Hit) {
						return false;
					}
				}
			}
		}
		return true;

	}

	/**
	 * Determines if a tile is valid based on the given private board
	 *
	 * @param b
	 * @param t
	 * @return
	 */
	public static boolean isValidTile(PrivateBoard b, Tile t) {
		if (t.getRow() >= 0 && t.getRow() < b.getBoardSize() && t.getCol() >= 0 && t.getCol() < b.getBoardSize()
				&& b.getTiles()[t.getRow()][t.getCol()].getTileType() == TileType.Empty) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if a tile is hit
	 *
	 * @param b
	 * @param t
	 * @return
	 */
	public static boolean isHit(PrivateBoard b, Tile t) {
		for (Dog dog : b.getDogs()) {

			if (dog.getDirection() == DogDirection.Horizontal) {
				for (int i = 0; i < dog.getLength(); i++) {
					if (dog.getxStart() + i == t.getCol() && dog.getyStart() == t.getRow()) {
						return true;
					}
				}
			} else if (dog.getDirection() == DogDirection.Vertical) {
				for (int j = 0; j < dog.getLength(); j++) {
					if (dog.getxStart() == t.getCol() && dog.getyStart() + j == t.getRow()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Determines if a dog placement is valid
	 *
	 * @param b
	 * @param newDog
	 * @return
	 */
	public static boolean isValidDog(PrivateBoard b, Dog newDog) {
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

		for (Dog dog : b.getDogs()) {

			if (dog.getDirection() == DogDirection.Horizontal) {
				for (int i = 0; i < dog.getLength(); i++) {
					if (dog.getxStart() + i >= 0 && dog.getxStart() + i < b.getBoardSize() && dog.getyStart() >= 0
							&& dog.getyStart() < b.getBoardSize()
							&& !occupiedTiles.contains(b.getTiles()[dog.getxStart() + i][dog.getyStart()])) {
						occupiedTiles.add(b.getTiles()[dog.getxStart() + i][dog.getyStart()]);
					} else {
						return false;
					}
				}

			} else if (dog.getDirection() == DogDirection.Vertical) {
				for (int j = 0; j < dog.getLength(); j++) {
					if (dog.getxStart() >= 0 && dog.getxStart() < b.getBoardSize() && dog.getyStart() + j >= 0
							&& dog.getyStart() + j < b.getBoardSize()
							&& !occupiedTiles.contains(b.getTiles()[dog.getxStart()][dog.getyStart() + j])) {
						occupiedTiles.add(b.getTiles()[dog.getxStart()][dog.getyStart() + j]);
					} else {
						return false;
					}
				}

			}

		}

		if (newDog.getDirection() == DogDirection.Horizontal) {
			for (int i = 0; i < newDog.getLength(); i++) {
				if (newDog.getxStart() + i >= 0 && newDog.getxStart() + i < b.getBoardSize() && newDog.getyStart() >= 0
						&& newDog.getyStart() < b.getBoardSize()
						&& !occupiedTiles.contains(b.getTiles()[newDog.getxStart() + i][newDog.getyStart()])) {
					occupiedTiles.add(b.getTiles()[newDog.getxStart() + i][newDog.getyStart()]);
				} else {
					return false;
				}
			}

		} else if (newDog.getDirection() == DogDirection.Vertical) {
			for (int j = 0; j < newDog.getLength(); j++) {
				if (newDog.getxStart() >= 0 && newDog.getxStart() < b.getBoardSize() && newDog.getyStart() + j >= 0
						&& newDog.getyStart() + j < b.getBoardSize()
						&& !occupiedTiles.contains(b.getTiles()[newDog.getxStart()][newDog.getyStart() + j])) {
					occupiedTiles.add(b.getTiles()[newDog.getxStart()][newDog.getyStart() + j]);
				} else {
					return false;
				}
			}

		}
		return true;

	}

	/**
	 * Determines if a board is valid
	 *
	 * @param b
	 * @return
	 */
	public static boolean verifyBoardInit(PrivateBoard b) {
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

		// ArrayList<Integer> expectedLengths = (ArrayList<Integer>)
		// Arrays.stream(b.getExpectedDogList()).boxed().collect(Collectors.toList());
		ArrayList<Integer> expectedLengths = new ArrayList<Integer>(b.getExpectedDogList());
		if (b.getExpectedDogList().size() != b.getDogs().size()) {
			return false;
		}
		for (Dog dog : b.getDogs()) {
			if (expectedLengths.contains(dog.getLength())) {
				expectedLengths.remove((Integer) dog.getLength());
			} else {
				return false;
			}
		}

		// test positions
		for (Dog dog : b.getDogs()) {

			if (dog.getDirection() == DogDirection.Horizontal) {
				for (int i = 0; i < dog.getLength(); i++) {
					if (dog.getxStart() + i >= 0 && dog.getxStart() + i < b.getBoardSize() && dog.getyStart() >= 0
							&& dog.getyStart() < b.getBoardSize()
							&& !occupiedTiles.contains(b.getTiles()[dog.getxStart() + i][dog.getyStart()])) {
						occupiedTiles.add(b.getTiles()[dog.getxStart() + i][dog.getyStart()]);
					} else {
						return false;
					}
				}

			} else if (dog.getDirection() == DogDirection.Vertical) {
				for (int j = 0; j < dog.getLength(); j++) {
					if (dog.getxStart() >= 0 && dog.getxStart() < b.getBoardSize() && dog.getyStart() + j >= 0
							&& dog.getyStart() + j < b.getBoardSize()
							&& !occupiedTiles.contains(b.getTiles()[dog.getxStart()][dog.getyStart() + j])) {
						occupiedTiles.add(b.getTiles()[dog.getxStart()][dog.getyStart() + j]);
					} else {
						return false;
					}
				}

			}

		}

		return true;
	}

}
