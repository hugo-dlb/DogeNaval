package yolo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DogeNavalGUI implements MouseListener, ActionListener{
	Board board;
	JButton testbutton1;
	JButton testbutton2;

	private DogeNavalGUI() {
		JFrame frame = new JFrame("DogeNaval");
		frame.setMinimumSize(new Dimension(400, 400));
		frame.setSize(new Dimension(500, 770));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		board = new Board();

		testbutton1 = new JButton("New Client");
		testbutton2 = new JButton("Remove Client");
		testbutton1.setActionCommand("newClientButton");
		testbutton2.setActionCommand("delClientButton");
		testbutton1.addActionListener(this);
		testbutton2.addActionListener(this);

		topPanel.setLayout(new GridLayout(2, 3));
		topPanel.add(testbutton1);
		topPanel.add(testbutton2);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(board);
		frame.pack();
		frame.setVisible(true);

		board.addMouseListener(this);
		
	}
	
	private void Sopln(String s) {
		System.out.println(s);
	}
	public static void main(String[] args) {
		new DogeNavalGUI();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("eee");
		board.updateUI();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
