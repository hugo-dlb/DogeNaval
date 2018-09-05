package com.daltrisseville.DogeNaval.Server.Entities;

import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.stream.Collectors;

import com.daltrisseville.DogeNaval.Server.Entities.Tile;

public class BoardVerifier {

	public static boolean verify(PrivateBoard b) {
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

		//ArrayList<Integer> expectedLengths = (ArrayList<Integer>) Arrays.stream(b.getExpectedDogList()).boxed().collect(Collectors.toList());
		ArrayList<Integer> expectedLengths=b.getExpectedDogList();
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
