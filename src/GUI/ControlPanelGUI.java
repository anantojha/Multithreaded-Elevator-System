package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

/**
 * @author Lasitha
 *
 */
public class ControlPanelGUI extends JFrame {

	int count = 0;
	private JLabel label; 
	private JFrame frame;
	private JPanel panel;
	
	public ControlPanelGUI() {

		frame = new JFrame();
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0,0,500,500));
		panel.setLayout(new GridLayout(0, 1));
		panel.setBackground(Color.WHITE);

		label = new JLabel(); //JLabel Creation
		label.setIcon(new ImageIcon("elevator.jpeg")); //Sets the image to be displayed as an icon
		Dimension size = label.getPreferredSize(); //Gets the size of the image
		label.setBounds(50, 30, size.width, size.height); //Sets the location of the image
		frame.add(label);

		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Elevator 1");
		frame.pack();
		frame.setVisible(true);
	}

	public void moveElevator(int diff) throws InterruptedException {
		label.setLocation(0, diff);
		Thread.sleep(10);
		frame.repaint();
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ControlPanelGUI panel = new ControlPanelGUI();
		for(int i = 0; i < 200; i++) {
			panel.moveElevator(i);
		}

		for(int i = 200; i > 0; i--) {
			panel.moveElevator(i);
		}


	}

}
