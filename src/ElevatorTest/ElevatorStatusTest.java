/**
 * 
 */
package ElevatorTest;

import static org.junit.Assert.*;
import org.junit.Test;
import Elevator.Enums.ElevatorStatus;

/**
 * @author Lasitha
 *
 */
public class ElevatorStatusTest {
	ElevatorStatus state;
	Boolean faultDetected;
	Boolean destinationReached;
	
	
	@Test
	/*
	 * test to verify the correct state flow when destination floor is reached
	 * A fault detected should cause a transition from CLOSE_DOOR to IDLE rather than
	 * CLOSE_DOOR TO RUNNING
	 * */
	public void destinationReached() {
		state = ElevatorStatus.INITIALIZE;
		faultDetected = false;
		destinationReached = true;
		
		state = state.nextState();
		assertEquals(ElevatorStatus.IDLE, state);
		
		state = state.nextState(faultDetected);
		assertEquals(ElevatorStatus.RUNNING, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.ARRIVED, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.OPEN_DOOR, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.CLOSE_DOOR, state);
		
		state = state.nextState(destinationReached);
		assertEquals(ElevatorStatus.IDLE, state);
	}
	
	@Test
	/*
	 * test to verify the correct state flow when destination floor is not reached yet
	 * A fault detected should cause a transition from CLOSE_DOOR to RUNNING rather than
	 * CLOSE_DOOR TO IDLE
	 * */
	public void destinationNotReached() {
		state = ElevatorStatus.INITIALIZE;
		faultDetected = false;
		destinationReached = false;
		
		state = state.nextState();
		assertEquals(ElevatorStatus.IDLE, state);
		
		state = state.nextState(faultDetected);
		assertEquals(ElevatorStatus.RUNNING, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.ARRIVED, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.OPEN_DOOR, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.CLOSE_DOOR, state);
		
		state = state.nextState(destinationReached);
		assertEquals(ElevatorStatus.RUNNING, state);
		
	}
	
	@Test
	/*
	 * test to verify the correct state flow when no fault is detected
	 * A fault detected should cause a transition from IDLE to RUNNING rather than
	 * IDLE TO FAULT_DETECTED
	 * */
	public void noFaultDetected() {
		state = ElevatorStatus.INITIALIZE;
		faultDetected = false;
		
		state = state.nextState();
		assertEquals(ElevatorStatus.IDLE, state);
		
		state = state.nextState(faultDetected);
		assertEquals(ElevatorStatus.RUNNING, state);
	}
	
	@Test
	/*
	 * test to verify the correct state flow when a fault is detected
	 * A fault detected should cause a transition from IDLE to FAULT_DETECTED rather than
	 * IDLE TO RUNNING
	 * */
	public void faultDetected() {
		state = ElevatorStatus.INITIALIZE;
		faultDetected = true;
		
		state = state.nextState();
		assertEquals(ElevatorStatus.IDLE, state);
		
		state = state.nextState(faultDetected);
		assertEquals(ElevatorStatus.FAULT_DETECTED, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.RESETTING, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.RESUMING, state);
		
		state = state.nextState();
		assertEquals(ElevatorStatus.RUNNING, state);
	}
}
