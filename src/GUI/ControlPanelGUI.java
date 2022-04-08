package GUI;

import Elevator.Enums.ElevatorStatus;

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
	private ImageIcon closeElevatorImage;
	private ImageIcon openElevatorImage;
	
	public ControlPanelGUI() throws InterruptedException {

		frame = new JFrame();
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0,0,500,500));
		panel.setLayout(new GridLayout(0, 1));
		panel.setBackground(Color.WHITE);

		status_label = new JLabel();
		status_label.setText(ElevatorStatus.INITIALIZE.toString());
		status_label.setBounds(0, 0, 200, 20);
		frame.add(status_label);
		floor_label = new JLabel();
		floor_label.setText("1");
		floor_label.setBounds(0, 25, 30, 20);
		frame.add(floor_label);

		label = new JLabel(); //JLabel Creation
		closeElevatorImage = new ImageIcon("elevator_close.jpeg");
		openElevatorImage = new ImageIcon("elevator_open.jpeg");
		label.setIcon(closeElevatorImage); //Sets the image to be displayed as an icon
		Dimension size = label.getPreferredSize(); //Gets the size of the image
		label.setBounds(75, 240, size.width, size.height); //Sets the location of the image
		frame.add(label);

		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Elevator 1");
		frame.pack();
		frame.setVisible(true);
	}

	public void moveElevator(int floor) throws InterruptedException {
		floor_label.setText(String.valueOf(floor));
		label.setLocation(label.getLocation().x, pos[floor]);
		frame.repaint();
	}

	public void updateElevatorLabels(ElevatorStatus status) throws InterruptedException {
		status_label.setText(status.toString());
		if(status == ElevatorStatus.OPEN_DOOR){
			label.setIcon(openElevatorImage);
		}

		if (status == ElevatorStatus.CLOSE_DOOR){
			label.setIcon(closeElevatorImage);
		}
		frame.repaint();
	}

}
