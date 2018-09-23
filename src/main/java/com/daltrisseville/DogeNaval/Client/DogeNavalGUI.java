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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.daltrisseville.DogeNaval.Client.Entities.BoardVerifier;
import com.daltrisseville.DogeNaval.Client.Entities.Dog;
import com.daltrisseville.DogeNaval.Client.Entities.DogDirection;
import com.daltrisseville.DogeNaval.Client.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Client.Entities.PrivateBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Tile;
import com.daltrisseville.DogeNaval.Client.Entities.TileType;

public class DogeNavalGUI implements MouseListener, ActionListener {
	private final Color myGreen = new Color(63, 182, 63); // 51, 204, 51);
	private final Color myRed = new Color(255, 76, 76);// 255, 51, 0);
	private final Color myYellow = new Color(255, 211, 0);// 189, 145, 15);
	private final Color myGray = new Color(57, 49, 49);// 121, 134, 134);
	private final Color myBlue = new Color(3, 201, 169);
	private final Color myWhite = new Color(236, 240, 241);

	int test = 0;

	private GenericBoard board;
	private PrivateBoard adminBoard;
	private ClientInstance clientInstance;

	boolean adminMode;

	JFrame frame = new JFrame("DogeNavalClient");
	JPanel container = new JPanel();
	
	JPanel loginPage = new JPanel();
	JPanel waitPage = new JPanel();
	JPanel playerPage = new JPanel();
	JPanel adminPage = new JPanel();

	JPanel playerPage_top = new JPanel();
	JPanel adminPage_top = new JPanel();
	BoardPanel boardPanel;
	AdminBoardPanel adminPanel;

	// loginPage
	JTextField loginTextField = new JTextField(10);
	JTextField passwordTextField = new JTextField(10);
	JButton buttonLogin = new JButton("login");
	JButton buttonOne = new JButton("Test player page");
	
	// WaitPage
	JLabel labelWait=new JLabel("");

	// gamePage
	JButton buttonSecond = new JButton("N/A");
	JButton buttonTest = new JButton("Test Tile Update");
	JButton buttonSendTile = new JButton("Attack!");

	// adminPage
	JLabel labelScores = new JLabel();
	JLabel labelInfo = new JLabel();
	JButton buttonValidate = new JButton("Validate");
	JButton buttonOrientation = new JButton("Switch orientation");
	JLabel labelOrientation = new JLabel();
	JButton buttonSendBoard = new JButton("Send board to server");

	CardLayout cl = new CardLayout();

	public DogeNavalGUI(ClientInstance clientInstance) {
		this.clientInstance = clientInstance;
		initGUI();

	}

	public void initGUI() {

		frame.setMinimumSize(new Dimension(500, 400));
		frame.setSize(new Dimension(600, 600));

		container.setLayout(cl);
		playerPage.setLayout(new BorderLayout());
		adminPage.setLayout(new BorderLayout());

		board = new GenericBoard();
		boardPanel = new BoardPanel(board);

		adminBoard = new PrivateBoard();
		adminPanel = new AdminBoardPanel(adminBoard);

		loginPage.add(loginTextField);
		loginPage.add(passwordTextField);
		loginPage.add(buttonLogin);
		loginPage.add(buttonOne);
		
		waitPage.add(labelWait);

		playerPage_top.add(buttonSecond);
		playerPage_top.add(buttonTest);
		playerPage_top.add(buttonSendTile);

		adminPage_top.add(labelInfo);
		adminPage_top.add(buttonValidate);
		adminPage_top.add(buttonOrientation);
		adminPage_top.add(labelOrientation);
		adminPage_top.add(buttonSendBoard);

		loginPage.setBackground(myGreen);
		playerPage_top.setBackground(myGray);
		waitPage.setBackground(myYellow);
		
		playerPage.add(playerPage_top, BorderLayout.NORTH);
		playerPage.add(boardPanel, BorderLayout.CENTER);

		adminPage.add(labelScores,BorderLayout.WEST);
		adminPage.add(adminPage_top, BorderLayout.NORTH);
		adminPage.add(adminPanel, BorderLayout.CENTER);

		container.add(loginPage, "loginPage");
		container.add(waitPage, "waitPage");
		container.add(playerPage, "playerPage");
		container.add(adminPage, "adminPage");

		switchPage("loginPage");
		

		buttonLogin.setActionCommand("login");
		buttonOne.setActionCommand("1");

		buttonSecond.setActionCommand("2");
		buttonTest.setActionCommand("3");
		buttonSendTile.setActionCommand("attack");

		buttonValidate.setActionCommand("validate");
		buttonOrientation.setActionCommand("orientation");
		buttonSendBoard.setActionCommand("sendBoard");

		buttonLogin.addActionListener(this);
		buttonOne.addActionListener(this);

		buttonSecond.addActionListener(this);
		buttonTest.addActionListener(this);
		buttonSendTile.addActionListener(this);

		buttonValidate.addActionListener(this);
		buttonOrientation.addActionListener(this);
		buttonSendBoard.addActionListener(this);

		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.pack();
		frame.setVisible(true);

		// test
		//updateBoard(new GenericBoard());

	}

	public void updatePlayerBoard(GenericBoard newBoard) {
		boardPanel.setBoard(newBoard);

		boardPanel.updateUI();
	}
	public void updateAdminBoard(PrivateBoard newBoard) {
		adminPanel.setBoard(newBoard);
		System.out.println("ee");

		adminPanel.updateUI();
		
		//String scores;
		//for() players set text
		

	}

	public void startGamePanel() {

		switchPage("playerPage");
		boardPanel.addMouseListener(this);
		adminMode = false;

	}

	public void startAdminPanel() {

		switchPage("adminPage");
		adminPanel.addMouseListener(this);
		adminMode = true;
		labelInfo.setText(
				"Place dog length " + adminPanel.getBoard().getExpectedDogList().get(adminPanel.getToPlaceDog()));
		labelOrientation.setText("Horizontal");

	}

	public void switchPage(String s) {
		cl.show(container, s);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		// login
		case "login":
			String log = loginTextField.getText();
			String pwd = passwordTextField.getText();
			String toSend = ClientInstance.buildLoginResponse(log, pwd);

			String s = ClientInstance.buildAdminResponse(adminPanel.getBoard());

			try {
				clientInstance.sendDataToServer(toSend);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "1":
			startGamePanel();
			break;

		// game
		case "2":
			// switchPanel("firstPanel");
			break;
		case "3":
			 //Tests
			boardPanel.getBoard().getTiles()[test][test].setTileType(TileType.Miss);
			test++;
			updatePlayerBoard(boardPanel.getBoard());
			
			break;

		case "attack":
			if (boardPanel.getSelectedTile() != null) {
				// send to server
				try {
					System.out.println("Tile sent :" + boardPanel.getSelectedTile().toString());
					clientInstance.sendDataToServer(ClientInstance.buildTileResponse(boardPanel.getSelectedTile()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		// admin
		case "validate":

			if (!adminPanel.isAllPlaced()) {
				Dog newDog = new Dog(adminPanel.getBoard().getExpectedDogList().get(adminPanel.getToPlaceDog()),
						adminPanel.getSelectedTile().getCol(), adminPanel.getSelectedTile().getRow(),
						adminPanel.getActualDirection());
				if (BoardVerifier.isValidDog(adminPanel.getBoard(), newDog)) {
					adminPanel.getBoard().addDog(newDog);

					adminPanel.setToPlaceDog(adminPanel.getToPlaceDog() + 1);

					adminPanel.updateUI();
				} else {
					JOptionPane.showMessageDialog(null, "Dog not valid", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			if (!adminPanel.isAllPlaced()) {
				labelInfo.setText("Place dog length "
						+ adminPanel.getBoard().getExpectedDogList().get(adminPanel.getToPlaceDog()));
			} else {
				labelInfo.setText("All dogs placed, please send");
			}

			break;
		case "orientation":
			switchOrientation();
			break;
		case "sendBoard":
			try {
				if (BoardVerifier.verifyBoardInit(adminPanel.getBoard())) {
					clientInstance.sendDataToServer(ClientInstance.buildAdminResponse(adminPanel.getBoard()));
				} else {
					JOptionPane.showMessageDialog(null, "Board not valid", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		default:
		}

	}

	public void switchOrientation() {
		if (adminPanel.getActualDirection() == DogDirection.Horizontal) {
			adminPanel.setActualDirection(DogDirection.Vertical);
			labelOrientation.setText("Vertical");
		} else if (adminPanel.getActualDirection() == DogDirection.Vertical) {
			adminPanel.setActualDirection(DogDirection.Horizontal);
			labelOrientation.setText("Horizontal");

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// System.out.println(e.getX() + " - " + e.getY());
		Point p = e.getPoint();

		if (adminMode) {
			int col = p.x / adminPanel.getRectSize();
			int row = p.y / adminPanel.getRectSize();
			if (col < adminPanel.getBoardSize() && row < adminPanel.getBoardSize()) {
				adminPanel.selectTile(new Tile(row, col));
				adminPanel.updateUI();
			}

		} else {
			int col = p.x / boardPanel.getRectSize();
			int row = p.y / boardPanel.getRectSize();
			if (col < boardPanel.getBoardSize() && row < boardPanel.getBoardSize()) {
				boardPanel.selectTile(new Tile(row, col));
				boardPanel.updateUI();
			}

		}

		// System.out.println(col + " " + row);

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

}
