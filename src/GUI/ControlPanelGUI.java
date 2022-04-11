package GUI;

import Elevator.Enums.ElevatorStatus;
import Elevator.FloorSubsystem.Request;
import Elevator.Global.SystemConfiguration;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
	private JPanel requestsPanel;
	private int[] pos = { 575, 550, 525, 500, 475, 450, 425, 400, 375, 350, 325, 300, 275, 250, 225, 200, 175, 150, 125, 100,
			75, 50 };
	private ImageIcon closeElevatorImage;
	private ImageIcon openElevatorImage;

	private Map<Integer, JLabel> statusLabelList;
	private Map<Integer, JLabel> floorLabelList;
	private Map<Integer, JLabel> doorLabelList;
	private Map<Integer, Object[]> ev_row;
	//Table variables
	private Object[][] data;
	DefaultTableModel tm;
	private String[] columnNames;
	JTable table;

	public ControlPanelGUI(int numElevators) throws InterruptedException {
		
		// Create GUI frame
		frame = new JFrame();
		frame.setTitle("Elevator Control Panel");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1200, 1000);
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

		requestsPanel = new JPanel();
		requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
		requestsPanel.setBackground(Color.WHITE);


		for (int i = 1; i < numElevators + 1; i++) {
			JPanel r = new JPanel();
			r.setLayout(new GridLayout(0, 1));
			r.setBorder(BorderFactory.createTitledBorder("Elevator " + i + " Requests Queue"));
			r.setBackground(Color.WHITE);
			JTextArea n = new JTextArea();
			//n.setMinimumSize(1, 1);
			n.setEditable(false);
			n.setPreferredSize(new Dimension(0,1));
			n.setSize(0, 1);

			r.add(new JScrollPane(n));
			requestsPanel.add(r);
		}
		
		// Create GUI Data Table
		
		ev_row = new HashMap<Integer, Object[]>();
		data = new Object[numElevators][6];
		tm = new DefaultTableModel(data, columnNames);
		columnNames = new String[] {
					"ID",
		            "Status",
		            "Direction",
		            "Current Floor",
		            "Source Floor",
		            "Destination Floor"};
		JPanel dataTable = new JPanel();
		table = new JTable(tm){
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {                
                    return false;               
            };
   
        };
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFocusable(false);
        table.setCellSelectionEnabled(false);
        table.setEnabled(false);   

		dataTable.setLayout(new GridLayout());
		dataTable.setBorder(BorderFactory.createTitledBorder("Data Table"));
		dataTable.setSize(100, 100);
		
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(new Dimension(100, 100));
		
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane, BorderLayout.CENTER);

		elevatorPanel.add(requestsPanel);

		contentPane.add(elevatorPanel);
		contentPane.add(scrollPane);

		frame.add(contentPane);
		//frame.pack();
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
		new ControlPanelGUI(SystemConfiguration.ELEVATORS);
	}

	public void updateFloor(int id, int floor) {
		floorLabelList.get(id).setText("Floor: " + floor);
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
	public void initialize(int id, String state) {
		ev_row.put(id, new Object[] { id, state, "", 1, null, null });
		data[id - 1] = ev_row.get(id);
		tm = new DefaultTableModel(data, columnNames);//THIS IS THE ISSUE
		table.setModel(tm);
	}

	public void updateData(int id, String state, String direction, Integer current, Integer source, Integer destination) {
		
		ev_row.put(id, new Object[] { id, state, direction, current, source, destination });
		
		// Since we are creating the elevators in ascending order (id = 1,2,3,4...)
		// We do not need dynamic table searching, as the elevators will be added in
		// order
		// This means that at row 0, elevator 1's information should be there
		// A better solution would be to implement dynamic searching, but that seems a
		// bit excessive for our scope
		// data[id-1] = ev_row.get(id);
		data[id - 1] = ev_row.get(id);
		updateTable(id);
		// Reset the table model
		//tm = new DefaultTableModel(data, columnNames);//THIS IS THE ISSUE
		//table.setModel(tm);
		tm.fireTableDataChanged();

	}
	public void updateTable(int id) {

		table.setValueAt(ev_row.get(id)[0], id-1, 0);
		table.setValueAt(ev_row.get(id)[1], id-1, 1);
		table.setValueAt(ev_row.get(id)[2], id-1, 2);
		table.setValueAt(ev_row.get(id)[3], id-1, 3);
		table.setValueAt(ev_row.get(id)[4], id-1, 4);
		table.setValueAt(ev_row.get(id)[5], id-1, 5);

	}

	public void updateElevatorQueue(int id, Queue<Request> requests){
		String text = "";
		int counter = 1;

		for (Request r: requests) {
			text = text + counter + ": " + r.toRequestDisplayString() + "\n\n";
			counter++;
		}

		if(requestsPanel.getComponent(id-1) != null)
			((JTextArea)((JScrollPane)((JPanel) requestsPanel.getComponent(id-1)).getComponent(0)).getViewport().getComponent(0)).setText(text);

		frame.repaint();
	}


	public void print(String str) {
		System.out.println(str);
	}
}
