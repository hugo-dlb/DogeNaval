package com.daltrisseville.DogeNaval.Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class BoardPanel extends JPanel{
	private GenericBoard board;
	
	private final Color myGreen = new Color(63, 182, 63); // 51, 204, 51);
	private final Color myRed = new Color(255, 76, 76);// 255, 51, 0);
	private final Color myYellow = new Color(255, 211, 0);// 189, 145, 15);
	private final Color myGray = new Color(57, 49, 49);// 121, 134, 134);
	
	public BoardPanel(GenericBoard b){
		this.board=b;
	}
	public int getRectSize() {
		// return this.rectSize;

		Rectangle r = this.getBounds();
		int rectSize;
		if (r.width / this.board.getBoardSize() > r.height / this.board.getBoardSize()) {
			rectSize = r.height / this.board.getBoardSize();
		} else {
			rectSize = r.width / this.board.getBoardSize();
		}
		return rectSize;
	}

	@Override
	public int getWidth() {
		return this.getRectSize() * this.board.getBoardSize();
	}

	@Override
	public int getHeight() {
		return this.getRectSize() * this.board.getBoardSize();
	}

	@Override
	public Dimension getPreferredSize() { // Preferred size of the component
		// return new Dimension(getWidth(), getHeight());
		System.out.println(this.getBounds().width + "ee" + getHeight());
		return new Dimension(this.getBounds().width, getHeight());
	}

	public GenericBoard getBoard() {
		return board;
	}
	public void setBoard(GenericBoard board) {
		this.board = board;
	}
	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < this.board.getBoardSize(); i++) {
			for (int j = 0; j < this.board.getBoardSize(); j++) {
				
				switch(this.board.getTiles()[i][j].getTileType()){
				case Empty:
					g.setColor(myGreen);
					break;
				case Hit:
					g.setColor(myRed);
					break;
				case Miss:
					g.setColor(myGray);
					break;
				}
				g.fillRect(j * this.getRectSize(), i * this.getRectSize(), this.getRectSize(), this.getRectSize());

				g.setColor(Color.BLACK);
				g.drawRect(j * this.getRectSize(), i * this.getRectSize(), this.getRectSize(), this.getRectSize());
			}

		}

	}
}