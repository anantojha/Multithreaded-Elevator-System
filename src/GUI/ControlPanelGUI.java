package GUI;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.ElevatorSubsystem.ElevatorController;
import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
	private JPanel contentPane;
	private JPanel elevatorPanel;
	private int[] pos = { 575, 550, 525, 500, 475, 450, 425, 400, 375, 350, 325, 300, 275, 250, 225, 200, 175, 150, 125, 100,
			75, 50 };
	private ImageIcon closeElevatorImage;
	private ImageIcon openElevatorImage;
	private RequestsView requestsLabel;
	private JLabel currentRequest;

	private Map<Integer, JLabel> statusLabelList;
	private Map<Integer, JLabel> floorLabelList;
	private Map<Integer, JLabel> doorLabelList;

	public ControlPanelGUI(int numElevators) throws InterruptedException {

		// Create GUI frame
		frame = new JFrame();
		frame.setTitle("Elevator Control Panel");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// Create GUI panel
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setBackground(Color.WHITE);
		// contentPane.setBorder(BorderFactory.createEmptyBorder(0,0,800,550));
		// contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		statusLabelList = new HashMap<Integer, JLabel>();
		floorLabelList = new HashMap<Integer, JLabel>();
		doorLabelList = new HashMap<Integer, JLabel>();

		elevatorPanel = new JPanel();
		elevatorPanel.setLayout(new BoxLayout(elevatorPanel, BoxLayout.X_AXIS));

		closeElevatorImage = new ImageIcon("elevator_close.jpeg");
		openElevatorImage = new ImageIcon("elevator_open.jpeg");
		closeElevatorImage = new ImageIcon(closeElevatorImage.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
		openElevatorImage = new ImageIcon(openElevatorImage.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));

		for (int i = 1; i < numElevators + 1; i++) {
			addElevatorPanel(i);
		}
		
		JPanel requestsPanel = new JPanel();
		requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
		requestsPanel.setBackground(Color.WHITE);

		
		for (int i = 1; i < numElevators + 1; i++) {
			JPanel r = new JPanel();
			r.setLayout(null);
			r.setBackground(Color.WHITE);
			r.setBorder(BorderFactory.createTitledBorder("Elevator " + i + " Requests"));
			requestsPanel.add(r);
		}
		
		
		// UI Data Table Here:
		JPanel dataTable = new JPanel();
		dataTable.setLayout(new GridLayout());
		dataTable.setBorder(BorderFactory.createTitledBorder("Data Table"));
		dataTable.setSize(100, 100);
		
		elevatorPanel.add(requestsPanel);
		contentPane.add(elevatorPanel);
		//contentPane.add(dataTable);

		frame.add(contentPane);
//		frame.pack();
		frame.setVisible(true);
	}

	public void addElevatorPanel(int id) {
		JPanel j = new JPanel();
		j.setLayout(null);
		j.setBackground(Color.WHITE);
		j.setBorder(BorderFactory.createTitledBorder("Elevator: " + id));

		status_label = new JLabel();
		status_label.setText(ElevatorStatus.INITIALIZE.toString());
		status_label.setBounds(10, 15, 200, 20);
		statusLabelList.put(id, status_label);
		j.add(status_label);
		
		for (int i = 0; i < 22; i++) {
			JLabel floorRect = new JLabel();
			floorRect.setText(Integer.toString(i + 1));
			floorRect.setForeground(Color.lightGray);
			floorRect.setBounds(10, pos[i], 200, 25);
			j.add(floorRect);
		}

		floor_label = new JLabel();
		floor_label.setName(Integer.toString(id));
		floor_label.setText("Floor: 1");
		floor_label.setBounds(10, 30, 200, 20);
		floorLabelList.put(id, floor_label);
		j.add(floor_label);

		label = new JLabel(); // JLabel Creation
		label.setIcon(closeElevatorImage); // Sets the image to be displayed as an icon
		doorLabelList.put(id, label);

		Dimension size = label.getPreferredSize(); // Gets the size of the image
		label.setBounds(20, pos[0], size.width, size.height); // Sets the location of the image
		j.add(label);
		elevatorPanel.add(j);
	}


	public static void main(String[] args) throws InterruptedException {
		new ControlPanelGUI(4);
	}

	public void updateFloor(int id, int floor) {
		floorLabelList.get(id).setText("Floor: " + Integer.toString(floor));
		JLabel doorLabel = doorLabelList.get(id);
		doorLabel.setLocation(doorLabel.getLocation().x, pos[floor - 1]);
		frame.repaint();
	}

	public void updateStatus(int id, String state) {
		statusLabelList.get(id).setText(state);
		if (state == ElevatorStatus.OPEN_DOOR.toString()) {
			doorLabelList.get(id).setIcon(openElevatorImage);
		}

		if (state == ElevatorStatus.CLOSE_DOOR.toString()) {
			doorLabelList.get(id).setIcon(closeElevatorImage);
		}
		frame.repaint();
	}

	public void print(String str) {
		System.out.println(str);
	}
}
