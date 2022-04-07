package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Elevator.ElevatorSubsystem.Elevator;
import Elevator.ElevatorSubsystem.ElevatorController;
import Elevator.SchedulerSubsystem.Scheduler;

/**
 * @author Lasitha
 *
 */
public class ControlPanelGUI implements ActionListener {

	int count = 0;
	private JLabel label; 
	private JFrame frame;
	private JPanel panel;
	
	public ControlPanelGUI() {

		frame = new JFrame();

		JButton button = new JButton("Start");
		button.addActionListener(this);
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0,0,500,1000));
		panel.setLayout(new GridLayout(0, 1));
		panel.add(button);

		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Elevator Control Panel GUI");
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ControlPanelGUI();
		//ElevatorController e = new ElevatorController();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        Scheduler c = new Scheduler();
        Thread client = new Thread(c, "Client Thread");
        client.start();

        while (true) {
            try {
				c.elevatorHandle();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
	}

}
