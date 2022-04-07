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
	private JLabel floor_label;
	private JLabel status_label;
	private JFrame frame;
	private JPanel panel;
	private int[] pos = {240, 216, 192, 168, 144, 120, 96, 72, 48, 24, 0};
	
	public ControlPanelGUI() throws InterruptedException {

		frame = new JFrame();
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0,0,500,500));
		panel.setLayout(new GridLayout(0, 1));
		panel.setBackground(Color.WHITE);

		status_label = new JLabel();
		status_label.setText(" null  ");
		status_label.setBounds(0, 0, 15, 15);
		frame.add(status_label);
		floor_label = new JLabel();
		floor_label.setText(" null  ");
		floor_label.setBounds(0, 20, 15, 15);
		frame.add(floor_label);

		label = new JLabel(); //JLabel Creation
		label.setIcon(new ImageIcon("elevator.jpeg")); //Sets the image to be displayed as an icon
		Dimension size = label.getPreferredSize(); //Gets the size of the image
		label.setBounds(75, 240, size.width, size.height); //Sets the location of the image
		frame.add(label);

		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Elevator 1");
		frame.pack();
		frame.setVisible(true);
	}

	public void moveElevator(String status, int floor) throws InterruptedException {
		floor_label.setText(String.valueOf(floor));
		status_label.setText(String.valueOf(status));
		label.setLocation(label.getLocation().x, pos[floor]);
		frame.repaint();
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ControlPanelGUI panel = new ControlPanelGUI();


	}

}
