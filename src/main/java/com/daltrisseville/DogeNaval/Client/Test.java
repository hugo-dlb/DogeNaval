package com.daltrisseville.DogeNaval.Client;

public class Test {

	public static void main(String[] args) {
		Board testBoard = new Board();
		testBoard.addDog(0, 0, 3, DogDirection.Horizontal);
		testBoard.addDog(0, 1, 4, DogDirection.Vertical);
		testBoard.addDog(0, 5, 7, DogDirection.Vertical);

		boolean b = BoardVerifier.verify(testBoard);
		System.out.println(b);

	}

}
