package com.daltrisseville.DogeNaval.Client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientInstance implements MouseListener, ActionListener {
	Scanner scanner;
	InetAddress ip;
	Socket s;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	
	private final Color myGreen = new Color(63, 182, 63); // 51, 204, 51);
	private final Color myRed = new Color(255, 76, 76);// 255, 51, 0);
	private final Color myYellow = new Color(255, 211, 0);// 189, 145, 15);
	private final Color myGray = new Color(57, 49, 49);// 121, 134, 134);

	JFrame frame = new JFrame("CardLayout demo");
	JPanel panelCont = new JPanel();
	JPanel panelFirst = new JPanel();
	JPanel panelSecond = new JPanel();
	JButton buttonOne = new JButton("Switch to second panel/workspace");
	JButton buttonSecond = new JButton("Switch to first panel/workspace");
	CardLayout cl = new CardLayout();

	public ClientInstance() {
		try {
			// scanner = new Scanner(System.in);

			// getting localhost ip
			// ip = InetAddress.getByName("localhost");

			// establish the connection with server port 5056
			// s = new Socket(ip, 5056);

			// obtaining input and out streams
			// dataInputStream = new DataInputStream(s.getInputStream());
			// dataOutputStream = new DataOutputStream(s.getOutputStream());

			initGUI();

			// start();

			// closing resources
			// scanner.close();
			// dataInputStream.close();
			// dataOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initGUI() {
		frame.setMinimumSize(new Dimension(400, 400));
		frame.setSize(new Dimension(500, 770));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panelCont.setLayout(cl);

		panelFirst.add(buttonOne);
		panelSecond.add(buttonSecond);
		panelFirst.setBackground(myRed);
		panelSecond.setBackground(myGreen);

		panelCont.add(panelFirst, "1");
		panelCont.add(panelSecond, "2");
		cl.show(panelCont, "1");

		buttonOne.setActionCommand("1");
		buttonSecond.setActionCommand("2");
		buttonOne.addActionListener(this);
		buttonSecond.addActionListener(this);

		frame.add(panelCont);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);


	}

	public void start() throws IOException {

		// the following loop performs the exchange of
		// information between client and client handler
		while (true) {
			System.out.println(dataInputStream.readUTF());
			String toSend = scanner.nextLine();
			dataOutputStream.writeUTF(toSend);

			// If client sends exit,close this connection
			// and then break from the while loop
			if (toSend.equals("Exit")) {
				System.out.println("Closing this connection : " + s);
				s.close();
				System.out.println("Connection closed");
				break;
			}

			// printing date or time as requested by client
			String received = dataInputStream.readUTF();
			System.out.println(received);
		}

	}

	public static void main(String[] args) throws IOException {
		ClientInstance clientinstance = new ClientInstance();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()){
		case "1":
			cl.show(panelCont, "2");
			break;
		case "2":
			cl.show(panelCont, "1");
			break;
		default:
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("eee");

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
