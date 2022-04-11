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
	public void noFaultDetected() {
		state = ElevatorStatus.INITIALIZE;
		faultDetected = false;
		
		state = state.nextState();
		assertEquals(ElevatorStatus.IDLE, state);
		
		state = state.nextState(faultDetected);
		assertEquals(ElevatorStatus.RUNNING, state);
	}
	
	@Test
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
