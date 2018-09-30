package com.daltrisseville.DogeNaval.Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import com.daltrisseville.DogeNaval.Client.Entities.BoardVerifier;
import com.daltrisseville.DogeNaval.Client.Entities.Dog;
import com.daltrisseville.DogeNaval.Client.Entities.DogDirection;
import com.daltrisseville.DogeNaval.Client.Entities.GenericBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Player;
import com.daltrisseville.DogeNaval.Client.Entities.PrivateBoard;
import com.daltrisseville.DogeNaval.Client.Entities.Tile;
import com.daltrisseville.DogeNaval.Client.Entities.TileType;

public class DogeNavalGUI implements MouseListener, ActionListener {
	private final Color myGreen = new Color(172, 220, 238); // 51, 204, 51);
	private final Color myRed = new Color(255, 76, 76);// 255, 51, 0);
	private final Color myYellow = new Color(253, 255, 186);// 189, 145, 15);
	private final Color myGray = new Color(57, 49, 49);// 121, 134, 134);

	private GenericBoard board;
	private PrivateBoard adminBoard;
	private ClientInstance clientInstance;

	boolean adminMode;

	JFrame frame = new JFrame("DogeNavalClient");
	JPanel container = new JPanel();
	JRootPane jrootpane;

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
	JPasswordField passwordTextField = new JPasswordField(10);
	JButton buttonLogin = new JButton("login");

	// WaitPage
	JLabel labelWait = new JLabel("");

	// gamePage
	JLabel labelInfoPlayer = new JLabel();
	JButton buttonSendTile = new JButton("Attack!");

	// adminPage
	JLabel labelInfoAdmin = new JLabel();
	JButton buttonValidate = new JButton("Validate");
	JButton buttonOrientation = new JButton("Switch orientation");
	JButton buttonSendBoard = new JButton("Start Game !");

	CardLayout cl = new CardLayout();

	public DogeNavalGUI(ClientInstance clientInstance) {
		this.clientInstance = clientInstance;
		initGUI();

	}

	public void initGUI() {

		frame.setMinimumSize(new Dimension(500, 400));
		frame.setSize(new Dimension(900, 700));

		container.setLayout(cl);
		playerPage.setLayout(new BorderLayout());
		adminPage.setLayout(new BorderLayout());

		board = new GenericBoard();
		boardPanel = new BoardPanel(board);

		adminBoard = new PrivateBoard();
		adminPanel = new AdminBoardPanel(adminBoard);

		labelWait.setFont(new Font("Serif", Font.PLAIN, 28));
		this.labelInfoAdmin.setFont(new Font("Serif", Font.PLAIN, 18));
		this.labelInfoPlayer.setFont(new Font("Serif", Font.PLAIN, 18));

		loginPage.add(loginTextField);
		loginPage.add(passwordTextField);
		loginPage.add(buttonLogin);

		waitPage.add(labelWait);

		playerPage_top.add(buttonSendTile);

		adminPage_top.add(buttonValidate);
		adminPage_top.add(buttonOrientation);
		adminPage_top.add(buttonSendBoard);

		loginPage.setBackground(myGreen);
		playerPage_top.setBackground(myGray);
		adminPage_top.setBackground(myRed);
		waitPage.setBackground(myYellow);

		playerPage.add(labelInfoPlayer, BorderLayout.WEST);
		playerPage.add(playerPage_top, BorderLayout.NORTH);
		playerPage.add(boardPanel, BorderLayout.CENTER);

		adminPage.add(labelInfoAdmin, BorderLayout.WEST);
		adminPage.add(adminPage_top, BorderLayout.NORTH);
		adminPage.add(adminPanel, BorderLayout.CENTER);

		container.add(loginPage, "loginPage");
		container.add(waitPage, "waitPage");
		container.add(playerPage, "playerPage");
		container.add(adminPage, "adminPage");

		switchPage("loginPage");

		buttonLogin.setActionCommand("login");

		buttonSendTile.setActionCommand("attack");

		buttonValidate.setActionCommand("validate");
		buttonOrientation.setActionCommand("orientation");
		buttonSendBoard.setActionCommand("sendBoard");

		buttonLogin.addActionListener(this);

		buttonSendTile.addActionListener(this);

		buttonValidate.addActionListener(this);
		buttonOrientation.addActionListener(this);
		buttonSendBoard.addActionListener(this);

		jrootpane = frame.getRootPane();
		jrootpane.setDefaultButton(buttonLogin);

		adminPanel.addMouseListener(this);
		boardPanel.addMouseListener(this);

		frame.add(container);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.pack();
		frame.setVisible(true);

	}

	public void updatePlayerBoard(GenericBoard newBoard) {
		boardPanel.setBoard(newBoard);

		boardPanel.updateUI();

		updatePlayerLabel();
	}

	public void updateAdminBoard(PrivateBoard newBoard) {
		System.out.println("updateAdmin");
		adminPanel.setBoard(newBoard);

		adminPanel.updateUI();

		updateAdminLabel();

	}

	public void updateAdminLabel() {
		String s = "<html>";
		if (clientInstance.isAdminCreating()) {
			if (!adminPanel.isAllPlaced()) {
				s += "Place dog length " + adminPanel.getBoard().getExpectedDogList().get(adminPanel.getToPlaceDog());
			} else {
				s += "All dogs placed, please send";
			}
			s += "<br>";
			s += "<br>";

			if (adminPanel.getActualDirection() == DogDirection.Horizontal) {
				s += "Horizontal";
			} else if (adminPanel.getActualDirection() == DogDirection.Vertical) {
				s += "Vertical";

			}
		}

		s += "<br>";
		s += "<br>";
		s += "Player List";
		s += "<br>";
		for (Player p : clientInstance.getPlayers()) {
			if (!p.getLevel().equals("ADMIN")) {
				s += p.toString() + "<br>";
			}

		}
		s += "</html>";
		this.labelInfoAdmin.setText(s);

	}

	public void updatePlayerLabel() {
		String s = "<html>";
		s += clientInstance.isMyTurn() ? "Your turn!" : "Not your turn";
		s += "<br>";
		s += "<br>";
		s += "Player List";
		s += "<br>";
		for (Player p : clientInstance.getPlayers()) {
			if (!p.getLevel().equals("ADMIN")) {
				s += p.toString() + "<br>";
			}

		}
		s += "</html>";
		this.labelInfoPlayer.setText(s);
	}

	public void goToLobby(Player[] players) {

		switchPage("waitPage");
		jrootpane.setDefaultButton(null);

		String s = "";
		s += "<html>Game not started. Please wait... <br>";
		for (Player p : players) {
			s += p.toString() + "<br>";
		}
		s += "</html>";
		labelWait.setText(s);
	}

	public void startGamePanel() {
		System.out.println("startGame");

		switchPage("playerPage");
		jrootpane.setDefaultButton(buttonSendTile);
		adminMode = false;

		board = new GenericBoard();
		boardPanel.setBoard(board);

		updatePlayerBoard(board);

	}

	public void startAdminPanel() {
		System.out.println("startradmpanel");
		switchPage("adminPage");
		jrootpane.setDefaultButton(buttonValidate);

		adminBoard = new PrivateBoard();

		adminPanel.setBoard(adminBoard);
		adminPanel.setToPlaceDog(0);
		adminPanel.setAllPlaced(false);

		this.buttonOrientation.setEnabled(true);
		this.buttonSendBoard.setEnabled(true);
		this.buttonValidate.setEnabled(true);

		adminMode = true;

		updateAdminBoard(adminBoard);

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

		case "attack":
			if (boardPanel.getSelectedTile() != null) {
				// send to server
				try {
					// System.out.println("Tile sent :" + boardPanel.getSelectedTile().toString());
					clientInstance.sendDataToServer(ClientInstance.buildTileResponse(boardPanel.getSelectedTile()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		// admin
		case "validate":
			System.out.println("validate");

			if (!adminPanel.isAllPlaced()) {
				Dog newDog = new Dog(adminPanel.getBoard().getExpectedDogList().get(adminPanel.getToPlaceDog()),
						adminPanel.getSelectedTile().getCol(), adminPanel.getSelectedTile().getRow(),
						adminPanel.getActualDirection());
				if (BoardVerifier.isValidDog(adminPanel.getBoard(), newDog)) {
					adminPanel.getBoard().addDog(newDog);

					adminPanel.setToPlaceDog(adminPanel.getToPlaceDog() + 1);

					adminPanel.updateUI();
					this.updateAdminLabel();
				} else {
					JOptionPane.showMessageDialog(null, "Dog not valid", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			break;
		case "orientation":
			switchOrientation();
			break;
		case "sendBoard":
			try {
				if (BoardVerifier.verifyBoardInit(adminPanel.getBoard())) {
					if (clientInstance.getPlayers().length == 3) {
						clientInstance.setAdminCreating(false);

						this.buttonOrientation.setEnabled(false);
						this.buttonSendBoard.setEnabled(false);
						this.buttonValidate.setEnabled(false);

						clientInstance.sendDataToServer(ClientInstance.buildAdminResponse(adminPanel.getBoard()));
					} else {
						JOptionPane.showMessageDialog(null, "Not enough players!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Board not valid", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		default:
		}
	}

	public void switchOrientation() {
		if (adminPanel.getActualDirection() == DogDirection.Horizontal) {
			adminPanel.setActualDirection(DogDirection.Vertical);
		} else if (adminPanel.getActualDirection() == DogDirection.Vertical) {
			adminPanel.setActualDirection(DogDirection.Horizontal);

		}
		updateAdminLabel();

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		System.out.println(e.getX() + " - " + e.getY());
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
