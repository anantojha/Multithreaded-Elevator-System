package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Elevator.ElevatorSubsystem.ElevatorContext;
import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;

public class MainGUI extends JFrame {
	
	ElevatorContext ec;
	
	int count = 0;
	private JFrame frame;
	private Object[][] data;
	private String[] columnNames;
	JTable table;
	
	public MainGUI(int id) {
		ec = new ElevatorContext(id, 0, Direction.UP);
		
		data = new Object[][] {
			    {"1", "INITIALIZE",
				     "UP", new Integer(5), 6},
				    {"2", "INITIALIZE",
				     "UP", new Integer(3), 6},
				    {"3", "INITIALIZE",
				     "UP", new Integer(2), 6},
				    {"4", "INITIALIZE",
				     "UP", new Integer(2), 6},
				    {"5", "INITIALIZE",
				     "UP", new Integer(1), 6}
				};
		columnNames = new String[] {
				"ID",
	            "Status",
	            "Direction",
	            "Current Floor",
	            "Destination Floor"};
		
		frame = new JFrame();
		
		
		table = new JTable(data, columnNames){
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {                
                    return false;               
            };
        };
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(new Dimension(500, 500));
        table.setFillsViewportHeight(true);

        
		JScrollPane scrollPane = new JScrollPane(table);

        
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Elevator Table");
		frame.pack();
		frame.setVisible(true);
	}
	
	public void updateTable() {

		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[0].length; j++) {
				table.setValueAt(data[i][j], i, j);
			}
		}
		
	}
	public void addElevator(ElevatorContext ev) {
		Object[][] temp = new Object[data.length+1][data[0].length];
		
		for(int i = 0; i < data.length+1; i++) {
			if(i+1 == data.length+1) {
				temp[i][0] = ev.getID();
				temp[i][1] = ev.getStatus().toString();
				temp[i][2] = ev.getDirection();
				temp[i][3] = ev.getCurrentFloor();
				temp[i][4] = 0;
				break;
			}
			for(int j = 0; j < data[0].length; j++) {
				temp[i][j] = data[i][j];
			}
		}
		data = temp;
		updateTable();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainGUI m = new MainGUI(2);
		m.updateTable();
		m.addElevator(new ElevatorContext(1,10, Direction.UP));

	}

}
