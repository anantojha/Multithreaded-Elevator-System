package GUI;

import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Queue;
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
	private RequestsView requestsLabel;
	private JLabel currentRequest;
	
	public ControlPanelGUI(int id) throws InterruptedException {

		frame = new JFrame();
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0,0,700,550));
		panel.setLayout(new GridLayout(0, 1));
		panel.setBackground(Color.WHITE);

		status_label = new JLabel();
		status_label.setText(ElevatorStatus.INITIALIZE.toString());
		status_label.setBounds(5, 25, 200, 20);
		frame.add(status_label);
		floor_label = new JLabel();
		floor_label.setText("1");
		floor_label.setBounds(5, 50, 30, 20);
		frame.add(floor_label);

		currentRequest = new JLabel();
		currentRequest.setLocation(5,0);
		currentRequest.setSize(550,25);
		frame.add(currentRequest);
		requestsLabel = new RequestsView("");
		frame.add(requestsLabel);

		label = new JLabel(); //JLabel Creation
		closeElevatorImage = new ImageIcon("elevator_close.jpeg");
		openElevatorImage = new ImageIcon("elevator_open.jpeg");
		label.setIcon(closeElevatorImage); //Sets the image to be displayed as an icon
		Dimension size = label.getPreferredSize(); //Gets the size of the image
		label.setBounds(255, 240, size.width, size.height); //Sets the location of the image
		frame.add(label);

		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Elevator " + id);
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

	public void updateCurrentRequestLabel(Request request) throws InterruptedException {
		currentRequest.setText(request.toString());
		frame.repaint();
	}

	public void updateElevatorQueue(Queue<Request> requests) throws InterruptedException {
		String text = "<br>";

		for (Request r: requests) {
			text = text + r.toString() + "<br><br>";
		}
		requestsLabel.setText("<html>" + text + "</html>");
		requestsLabel.setVerticalTextPosition(SwingConstants.TOP);

		frame.repaint();
	}
}
