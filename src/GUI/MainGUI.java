package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.ElevatorSubsystem.ElevatorContext;
import Elevator.Enums.Direction;
import Elevator.Enums.ElevatorStatus;

public class MainGUI extends JFrame {
	
	ElevatorContext ec;
	
	int count = 0;
	private JFrame frame;
	private Object[][] data;
	DefaultTableModel tm;
	JTable table;
	ArrayList<Elevator> ev_al = new ArrayList<Elevator>();
	private String[] columnNames = new String[] {
			"ID",
            "Status",
            "Direction",
            "Current Floor",
            "Destination Floor"};
	
	public MainGUI() {
		ec = new ElevatorContext(2, Direction.UP);
		
		data = new Object[][] {};

		
		frame = new JFrame();
		tm = new DefaultTableModel(data, columnNames);
		
		table = new JTable(tm){
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
	public void paintTable() {
		table.repaint();
	}
	public void updateTable() {
		
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[0].length; j++) {
				table.setValueAt(data[i][j], i, j);
			}
		}
		table.repaint();
		
	}
	public void updateData() {
		Object[][] temp = new Object[ev_al.size()][5];
		for(int i = 0; i < ev_al.size(); i++) {
		//	temp[i][0] = ev_al.get(i).getID();
		//	temp[i][1] = ev_al.get(i).getState().getCurrentState();
		//	if(ev_al.get(i).getState().getCurrentState() == "IDLE") {
		//		temp[i][2] = "";
		//	}else {
		//		temp[i][2] = ev_al.get(i).getContext().getDirection();
		//	}
		//	temp[i][3] = ev_al.get(i).getContext().getCurrentFloor();
		//	temp[i][4] = ev_al.get(i).getDestinationFloor();
;
		}
		data = temp;
		//Reset the table model
		tm = new DefaultTableModel(data, columnNames);
		table.setModel(tm);
		updateTable();
	}
	public void addElevator(Elevator ev) {
		ev_al.add(ev);
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MainGUI m = new MainGUI();

	}

}
