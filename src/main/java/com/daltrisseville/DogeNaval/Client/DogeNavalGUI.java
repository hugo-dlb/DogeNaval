package com.daltrisseville.DogeNaval.Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.daltrisseville.DogeNaval.Server.Entities.ClientLoginEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DogeNavalGUI implements MouseListener, ActionListener {
	private final Color myGreen = new Color(63, 182, 63); // 51, 204, 51);
	private final Color myRed = new Color(255, 76, 76);// 255, 51, 0);
	private final Color myYellow = new Color(255, 211, 0);// 189, 145, 15);
	private final Color myGray = new Color(57, 49, 49);// 121, 134, 134);
	private final Color myBlue = new Color(3, 201, 169);
	private final Color myWhite = new Color(236, 240, 241);

	int test = 0;

	private GenericBoard board;
	private ClientInstance clientInstance;

	JFrame frame = new JFrame("DogeNavalClient");
	JPanel container = new JPanel();
	JPanel firstPage = new JPanel();
	JPanel secondPage = new JPanel();
	JPanel adminPage=new JPanel();

	JPanel secondPage_top = new JPanel();
	JPanel adminPage_top=new JPanel();
	BoardPanel boardPanel;
	BoardPanel adminPanel;
	
	
	
JButton loginButton=new JButton("login");
	JButton buttonOne = new JButton("Switch to second panel/workspace");
	JButton buttonSecond = new JButton("Switch to first panel/workspace");
	JButton buttonTest = new JButton("Test");
	JButton buttonSendTile=new JButton("Attack!");
	
	JTextField loginTextField = new JTextField(10);
	JTextField passwordTextField = new JTextField(10);

	CardLayout cl = new CardLayout();
	
	
	public DogeNavalGUI(ClientInstance clientInstance) {
		this.clientInstance=clientInstance;
		initGUI();
		
		
	}

	public void initGUI() {
		/*
		for(int i=0;i<100;i++) {
			JFrame thisframe = new JFrame("DogeNavalClient");
			thisframe.setMinimumSize(new Dimension(500+i*9, 400+i*7));
			thisframe.setSize(new Dimension(500, 770));
			thisframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			thisframe.pack();
			thisframe.setVisible(true);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		frame.setMinimumSize(new Dimension(400, 400));
		frame.setSize(new Dimension(500, 770));
		

		container.setLayout(cl);
		secondPage.setLayout(new BorderLayout());
		adminPage.setLayout(new BorderLayout());

		board = new GenericBoard();
		boardPanel = new BoardPanel(board);
		adminPanel=null;
		

		firstPage.add(loginTextField);
		firstPage.add(passwordTextField);
		firstPage.add(loginButton);
		firstPage.add(buttonOne);
		secondPage_top.add(buttonSecond);
		secondPage_top.add(buttonTest);
		secondPage_top.add(buttonSendTile);

		firstPage.setBackground(myGreen);
		secondPage_top.setBackground(myGray);

		secondPage.add(secondPage_top, BorderLayout.NORTH);
		secondPage.add(boardPanel, BorderLayout.CENTER);
		
		adminPage.add(adminPage_top,BorderLayout.CENTER);

		container.add(firstPage, "1");
		container.add(secondPage, "2");
		container.add(adminPage, "adminPage");
		
		switchPanel("firstPanel");

		boardPanel.addMouseListener(this);

		buttonOne.setActionCommand("1");
		buttonSecond.setActionCommand("2");
		buttonTest.setActionCommand("3");
		loginButton.setActionCommand("login");
		buttonSendTile.setActionCommand("attack");
		buttonOne.addActionListener(this);
		buttonSecond.addActionListener(this);
		buttonTest.addActionListener(this);
		loginButton.addActionListener(this);
		buttonSendTile.addActionListener(this);

		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		
		
		// test
		updateBoard(new GenericBoard());

	}

	public void updateBoard(GenericBoard newBoard) {
		boardPanel.setBoard(newBoard);

		boardPanel.updateUI();

	}
	
	public void switchPanel(String s) {
		
		switch(s) {
		case "firstPanel":
			cl.show(container, "1");
		break;
		case "secondPanel":
			cl.show(container, "2");
			break;
		case "adminPanel":
			cl.show(container, "admin");
			break;
		}
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "1":
			switchPanel("secondPanel");
			break;
		case "2":

			switchPanel("firstPanel");

			break;
		case "3":
			// Tests
			boardPanel.getBoard().getTiles()[test][test].setTileType(TileType.Miss);
			test++;
			updateBoard(boardPanel.getBoard());
			break;
		case "login":
			String log=loginTextField.getText();
			String pwd=passwordTextField.getText();
			String toSend=clientInstance.buildLoginResponse(log,pwd);
			System.out.println(toSend);
			try {
				clientInstance.sendDataToServer(toSend);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "attack":
			if(boardPanel.getSelectedTile()!=null) {
				//send to server
				try {
					System.out.println("Tile sent :"+boardPanel.getSelectedTile().toString());
					clientInstance.sendDataToServer(buildTileResponse(boardPanel.getSelectedTile()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			break;
		default:
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println(e.getX() + " - " + e.getY());
		Point p = e.getPoint();
		int col = p.x / boardPanel.getRectSize();
		int row = p.y / boardPanel.getRectSize();
		if (col < boardPanel.getBoardSize() && row < boardPanel.getBoardSize()) {
			boardPanel.selectTile(new Tile(row,col));
			boardPanel.updateUI();
		}

		System.out.println(col + " " + row);

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
    private String buildTileResponse(Tile t) {
    	Gson gson = new Gson();
    	JsonObject s = (JsonObject)gson.toJsonTree(t);
    	s.addProperty("eventType", "PLAY");
    	
    	
        return s.toString();
    }
    

}
