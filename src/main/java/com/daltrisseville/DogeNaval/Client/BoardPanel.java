package com.daltrisseville.DogeNaval.Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.daltrisseville.DogeNaval.Client.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Tile;

public class BoardPanel extends JPanel {
	private GenericBoard board;
	private Tile selectedTile;

	private final Color myRed = new Color(255, 76, 76);
	private final Color myRed2 = new Color(139, 10, 30);
	private final Color myBlue = new Color(3, 201, 169);
	private final Color myBlue2 = new Color(41,161,156);
	private final Color myBlue3 = new Color(41,161,190);
	private final Color myWhite = new Color(236, 240, 241);

	public BoardPanel(GenericBoard b) {
		this.board = b;
		this.selectedTile = null;
	}

	public int getBoardSize() {
		return board.getBoardSize();

	}

	public void selectTile(Tile t) {
		this.selectedTile = t;
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

				switch (this.board.getTiles()[i][j].getTileType()) {
				case Empty:
					if ((i + j) % 3 == 0) {
						g.setColor(myBlue);
					} else if ((i + j) % 3 == 1) {
						g.setColor(myBlue2);
					} else {
						g.setColor(myBlue3);
					}
					break;
				case Hit:
					g.setColor(myRed);
					break;
				case Miss:
					g.setColor(myWhite);
					break;
				}
				g.fillRect(j * this.getRectSize(), i * this.getRectSize(), this.getRectSize(), this.getRectSize());

				g.setColor(Color.BLACK);
				g.drawRect(j * this.getRectSize(), i * this.getRectSize(), this.getRectSize(), this.getRectSize());
			}

		}
		if (selectedTile != null) {
			g.setColor(myRed2);
			for (int i = 0; i < 4; i++) {
				g.drawRect(selectedTile.getCol() * this.getRectSize() + i,
						selectedTile.getRow() * this.getRectSize() + i, this.getRectSize() - 2 * i,
						this.getRectSize() - 2 * i);
			}
		}
	}

	public Tile getSelectedTile() {
		return selectedTile;
	}

	public void mouseClicked(MouseEvent e) {
	}
}
