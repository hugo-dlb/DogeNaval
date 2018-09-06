package com.daltrisseville.DogeNaval.Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PublicBoard extends GenericBoard {
	
	private final Color myGreen = new Color(63, 182, 63); // 51, 204, 51);
	private final Color myRed = new Color(255, 76, 76);// 255, 51, 0);
	private final Color myYellow = new Color(255, 211, 0);// 189, 145, 15);
	private final Color myGray = new Color(57, 49, 49);// 121, 134, 134);
	
	public int getRectSize() {
		// return this.rectSize;

		Rectangle r = this.getBounds();
		int rectSize;
		if (r.width / this.getBoardSize() > r.height / this.getBoardSize()) {
			rectSize = r.height / this.getBoardSize();
		} else {
			rectSize = r.width / this.getBoardSize();
		}
		return rectSize;
	}

	@Override
	public int getWidth() {
		return this.getRectSize() * this.getBoardSize();
	}

	@Override
	public int getHeight() {
		return this.getRectSize() * this.getBoardSize();
	}

	@Override
	public Dimension getPreferredSize() { // Preferred size of the component
		// return new Dimension(getWidth(), getHeight());
		System.out.println(this.getBounds().width + "ee" + getHeight());
		return new Dimension(this.getBounds().width, getHeight());
	}

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < this.boardSize; i++) {
			for (int j = 0; j < this.boardSize; j++) {
				g.setColor(myGreen);
				g.fillRect(j * this.getRectSize(), i * this.getRectSize(), this.getRectSize(), this.getRectSize());

				g.setColor(Color.BLACK);
				g.drawRect(j * this.getRectSize(), i * this.getRectSize(), this.getRectSize(), this.getRectSize());
			}

		}

	}

}
